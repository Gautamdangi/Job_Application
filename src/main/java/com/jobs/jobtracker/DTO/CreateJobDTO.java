package com.jobs.jobtracker.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class CreateJobDTO {

    @NonNull
    private String company;
    private String jobTitle;
    private String Link;



}
