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

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest) {
        authService.signup(registerRequest);
        return new ResponseEntity<>("User Registration Successful",
                HttpStatus.OK);
    }

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
