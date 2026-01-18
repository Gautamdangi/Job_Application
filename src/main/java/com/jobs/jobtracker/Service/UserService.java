package com.jobs.jobtracker.Service;

import com.jobs.jobtracker.DTO.LoginRequest;
import com.jobs.jobtracker.DTO.LoginResponse;
import com.jobs.jobtracker.DTO.SignUpRequest;
import com.jobs.jobtracker.DTO.SignUpResponse;

public interface UserService {


    LoginResponse loginUser(LoginRequest login);

    SignUpResponse  registerUser(SignUpRequest signUp);

}
