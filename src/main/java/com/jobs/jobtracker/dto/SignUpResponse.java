package com.jobs.jobtracker.dto;

import com.jobs.jobtracker.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpResponse {

    private long Id;
    private String userName;
    private  String userEmail;
    private Role role;

    private String message;
    private String token;
}
