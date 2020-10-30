package com.pwebk.SpringBootBlogApplication.config;

import com.pwebk.SpringBootBlogApplication.security.JwtAuthenticationFilter;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;


    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    //Configure method takes HttpSecurity Object as input. Inside this method, we disbale CSRF token
    // for our backend,CSRF attacks can occur when we are using sessions and
    //cookies, next we allow all incoming requests to our backend using authorizeRequests method
    //and make sure any other request that does not match y=this pattern should be authenticated
    //by using antMatchers method
    //Learn more about Spring Security
    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/auth/**")
                .permitAll()
                .anyRequest()
                .authenticated();
        httpSecurity.addFilterBefore(jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class);
    }

    //AuthenticationManagerBuilder class is inbuit in Spring an is used for authentication requests.
    //Inside it we reach out to userDetailsService interface which is used for loading User data from
    //different sources as seen in readme2.md and provide user data to Spring.
    //Since userDetailsService is an interface we wil create an implementation class called
    //UserDetailsServiceImpl in service package
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    //The method below is used for encrypting passwords.
    // We use Bean annotation as the PasswordEncoder is an interface and return an instance of
    // BCryptPasswordEncoder. We ivoke this method in AuthService class where we encrypt the password.
    @Bean
    PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }
}
