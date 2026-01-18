package com.jobs.jobtracker.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SignUpRequest {


    String userName;
    String userEmail;
    String password;


}
