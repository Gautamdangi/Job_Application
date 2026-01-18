package com.jobs.jobtracker.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpResponse {
    private long userId;
    private String userName;
    private  String userEmail;
    private String message;
}
