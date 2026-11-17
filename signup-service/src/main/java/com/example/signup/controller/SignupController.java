package com.example.signup.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/signup")
public class SignupController {

    @PostMapping
    public String signup(@RequestBody SignupRequest request) {
        return "Signup successful: " + request.getName();
    }
}