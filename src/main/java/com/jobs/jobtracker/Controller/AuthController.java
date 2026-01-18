package com.jobs.jobtracker.Controller;

import com.jobs.jobtracker.DTO.LoginRequest;
import com.jobs.jobtracker.DTO.SignUpRequest;
import com.jobs.jobtracker.DTO.SignUpResponse;
import com.jobs.jobtracker.Service.UserServiceImplement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    UserServiceImplement userServiceImpl;

    @PostMapping("/registerUser")
    public ResponseEntity<SignUpResponse> registerUser(@RequestBody SignUpRequest request) {
        SignUpResponse response = userServiceImpl.registerUser(request);
        return ResponseEntity.ok(userServiceImpl.registerUser(request));


    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> loginUser(@RequestBody LoginRequest login) {
        String Token = String.valueOf(userServiceImpl.loginUser(login));
        return  ResponseEntity.ok(Map.of("token",Token));

    }
}
