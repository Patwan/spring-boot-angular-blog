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

    //Learn more about Spring Security. The class below takes username as input and returns UserDetails
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
