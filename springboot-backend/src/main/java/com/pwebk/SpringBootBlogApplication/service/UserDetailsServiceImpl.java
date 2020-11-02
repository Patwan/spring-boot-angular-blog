package com.pwebk.SpringBootBlogApplication.service;

import com.pwebk.SpringBootBlogApplication.model.User;
import com.pwebk.SpringBootBlogApplication.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

import static java.util.Collections.singletonList;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    //This class primary role is implementing UserDetailsService Spring security Interface. An interface
    //contains abstract methods (which dont have a body) hence we add the logic when implemting the interface.
    // TO overide a method you press Ctrl + O inside the class.
    //The method has a method called loadUserByUsername whose return type in UserDetails (thi is part
    // of Spring Security framework). This means that Spring Security will accept UserDetails object
    //as its return type.
    //The method takes in username parameter as a parameter. Thie means Spring Security pass to us the
    //username and it expects us to return the User based on that username.
    //Inside the method we will reach out to mysql database. To achieve this we have to use JPA.
    //JPA works with repositories. Hence we will create a package called repository and create a class
    //called Userrepository. UserRepository will connect with user entity (inside the models package).


    // The class below takes username as input and returns UserDetails
    //object. Inside the method we require the UserRepository to retrieve user details based on username.
    //If the suer does not exist we throw a UsernameNotFoundExeption that is provided by Spring.
    //Next, we the object we create another object (this is a warpper) with the same name User. This class
    //is provided by Spring framework which implements UserDetails interface, here we map UserDetails to User
    //class. Lastly we provide an authority called as simple granted authority for a role called User.
    //This is now the core part of User Authentication. Learn more about Spring Security
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        User user = userOptional
                .orElseThrow(() -> new UsernameNotFoundException("No user " +
                        "Found with username : " + username));

        return new org.springframework.security
                .core.userdetails.User(user.getUsername(), user.getPassword(),
                user.isEnabled(), true, true,
                true, getAuthorities("USER"));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(String role){
        return singletonList(new SimpleGrantedAuthority(role));
    }
}
