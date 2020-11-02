package com.pwebk.SpringBootBlogApplication.service;

import com.pwebk.SpringBootBlogApplication.dto.AuthenticationResponse;
import com.pwebk.SpringBootBlogApplication.dto.LoginRequest;
import com.pwebk.SpringBootBlogApplication.dto.RegisterRequest;
import com.pwebk.SpringBootBlogApplication.exceptions.SpringRedditException;
import com.pwebk.SpringBootBlogApplication.model.NotificationEmail;
import com.pwebk.SpringBootBlogApplication.model.User;
import com.pwebk.SpringBootBlogApplication.model.VerificationToken;
import com.pwebk.SpringBootBlogApplication.repository.UserRepository;
import com.pwebk.SpringBootBlogApplication.repository.VerificationTokenRepository;
import com.pwebk.SpringBootBlogApplication.security.JwtProvider;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;


//We use Transactional annotation as we are relating with a relational database
@Service
@AllArgsConstructor
@Transactional
public class AuthService {

    //We are using Constructor Injection that is recommended by Spring, you can also use
    //field injection (using Autowired annotation)
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    //This method is responsible for registering a user and persisting data in a database
    // table, we pass in RegisterRequest dto (data transfer object that is holding the registerpayload
    // after being added from the controller).
    //The return Type of the method is null meaning its returning nothing.
    // Inside the method we instantiate the User entity (an enity is used to hold data
    // temporalily for storage. It only holds data but does not store).
    // We then use the setter methods to map data from RegisterRequest dto object to the User entity.
    //Before saving the password, we MUST encode it using a hashing algorithm. We use Bcrypt algorithym
    //that is provided by Spring security. We will add this in SecurityConfig class
    // Next we save the user entity to database via UserRepository (uses JPA ORM).
    //Next we invoke the private method that generates a token
    //Finally we invoke MailService class and call te sendMail method. In this method we instantiate
    //NotificationEmail entity and pass in the message that wil be send via mail and pass the token
    public void signup(RegisterRequest registerRequest){
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(false);

        userRepository.save(user);

        String token = generateVerificationToken(user);
        mailService.sendMail(new NotificationEmail("Please Activate your Account",
                user.getEmail(), "Thank you for signing up to Spring Reddit, " +
                "please click on the below url to activate your account : " +
                "http://localhost:8080/api/auth/accountVerification/" + token));
    }

    //Generate a verification token right after we save a user to the database and send that token as part
    //of the verification email.Once the user is verified we enable the user to login to our application
    //by setting setEnabled as true.
    //This method is invoked in the above method after a user is saved.
    private String generateVerificationToken(User user) {

        //Generate random UUID that acts as the token
        String token = UUID.randomUUID().toString();

        //Instantiate the VerificationToken entity and save the token using setToken setter method
        //and save user using setUser setter method.
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        //We then persist/push to the database via its respective repository.
        verificationTokenRepository.save(verificationToken);
        return token;
    }

    //In thus method we pass the token we pass from AuthController, we then pass it to
    // verificationTokenRepository and invoke findByToken method and pass in the token.
    //We assign it to a variable of type VerificationToken and this is optional.
    //Incase the entity does not exits we can call orElseThrow method and throw the custom exception
    //with message as invalid token.
    //If the token exists, we have to quesry the user who is associated with this token and enable that
    //user. We invoke the method and pass in the token
    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken =verificationTokenRepository.findByToken(token);
        verificationToken.orElseThrow(() -> new SpringRedditException("Invalid Token"));
        fetchUserAndEnable(verificationToken.get());
    }

    //In this method we pass in the verificationToken we got in the method above, inside the method we
    //take the token then reach out to the user (we did relationship in the model of ONE TO ONE meaning
    // each user has a token) then fetch the username of the User.
    //Next we reach out to the userRepository and find by tye username we got. If there is an error
    //we throw custom error.
    //Next we set Enabled column to true of the user whose token matched, finally we save the user.
    @Transactional
    private void fetchUserAndEnable(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(()->
                new SpringRedditException("User not found with name -" + username));
        user.setEnabled(true);
        userRepository.save(user);
    }

    //This method will contain logic to create UsernamePassword AuthenticationToken.Then use AuthenticationManager
    //to perform login. To do that we autowire AuthenticationManager into AuthService class at te top.
    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        ));

        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);
        return new AuthenticationResponse(token, loginRequest.getUsername());
    }
}
