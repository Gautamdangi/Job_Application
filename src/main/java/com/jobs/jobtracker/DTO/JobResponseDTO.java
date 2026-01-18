package com.jobs.jobtracker.DTO;

import com.jobs.jobtracker.Model.Jobs;
import com.jobs.jobtracker.Model.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobResponseDTO {
    private Long Id;
    private String company;
    private String jobTitle;
    private String salaryRange;
    private String notes;
    private LocalDateTime deadline;
    private String link;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;



}
