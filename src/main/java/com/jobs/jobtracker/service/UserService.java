package com.jobs.jobtracker.service;

import com.jobs.jobtracker.dto.LoginRequest;
import com.jobs.jobtracker.dto.LoginResponse;
import com.jobs.jobtracker.dto.SignUpRequest;
import com.jobs.jobtracker.dto.SignUpResponse;

public interface UserService {


    LoginResponse loginUser(LoginRequest login);

    SignUpResponse  registerUser(SignUpRequest signUp);

}
