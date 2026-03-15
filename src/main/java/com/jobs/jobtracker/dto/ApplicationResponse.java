package com.jobs.jobtracker.dto;

import com.jobs.jobtracker.model.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationResponse {
    private Long Id;
    private Long jobId;
    private String jobTitle;
    private String company;
    private String userName;
    private String userEmail;
    private String coverLetter;
    private String resumePath;
    private Status status;
    private String recruiterNotes;
    private LocalDateTime interviewDate;
    private LocalDateTime appliedAt;
    private LocalDateTime updateAt;











}
