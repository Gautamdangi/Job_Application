package com.jobs.jobtracker.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

//@Builder  -> use
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateJobDTO {

    // only fields that are allowed to change/update
    private String jobTitle;
    private String salaryRange;
    private LocalDate deadline;
    private Boolean isActive;

}
