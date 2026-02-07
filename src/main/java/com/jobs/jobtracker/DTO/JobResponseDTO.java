package com.jobs.jobtracker.DTO;

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
    private  String location;
    private String notes;
    private LocalDateTime deadline;
    private String applyLink;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer applicationCount;



}
