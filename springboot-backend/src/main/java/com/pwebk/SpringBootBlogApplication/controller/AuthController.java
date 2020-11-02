package com.pwebk.SpringBootBlogApplication.controller;

import com.pwebk.SpringBootBlogApplication.dto.AuthenticationResponse;
import com.pwebk.SpringBootBlogApplication.dto.LoginRequest;
import com.pwebk.SpringBootBlogApplication.dto.RegisterRequest;
import com.pwebk.SpringBootBlogApplication.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    //This signup method is responsible for authentication (registering a user).
    //Once data comes it we map the data to RegisterRequest dto (data transfer object which holds
    //the data temporalily and the data can be reused by different components).

    //It has has @PostMapping Spring web annotation means its a POST request.
    //The Return type is ResponseEnity of type String. ResponseEntity is meant to represent the entire
    // HTTP response.you can control anything that goes into it: status code, headers, and body.

    //Inside the method we reach out to authService and then its method called signup and pass
    //the registerRequest dto (data transfer object) that contains payload.
    //Once we register, we return status by instantiating ResponseEntity class and pass in the body
    //and Status Code(Ok meaning 200)
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest) {
        authService.signup(registerRequest);
        return new ResponseEntity<>("User Registration Successful",
                HttpStatus.OK);
    }

    //This method is responsible for account verification when a user clicks on the email that was sent
    //to his email address. We use GetMapping web annotation and pass the url we want to get data from.
    //Next we create a mehod called verifyAcount (whose returnType will be ResponseEntity of type string)
    //Inside the method we add pathVariale web annotation and pass the variable from the URL we want to capture.
    //If fine , we reach out to verifyAccount method in authService class and pass the token method.
    //Finally we return response by instantiating responseEntity  class (part of Spring http Object), we pass
    //in response message and the status.
    @GetMapping("accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token){
        authService.verifyAccount(token);
        return new ResponseEntity<>("Account activated successfully", HttpStatus.OK);
    }

    //This method is responsible for login in a user, we pass LoginRquest dto (data transfer object) that
    //holds username and password.
    //Inside it we reach out to login method that is part of authservice and pass in the loginRequest.
    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest){
        return authService.login(loginRequest);
    }
}
