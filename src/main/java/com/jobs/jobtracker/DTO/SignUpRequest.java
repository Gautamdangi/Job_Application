package com.jobs.jobtracker.DTO;


import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@AllArgsConstructor
@Data

public class SignUpRequest {


    private String userName;
    public String userEmail;
      private String role;
    private String  phoneNum;
    private Boolean isActive;
    private String password;

}
