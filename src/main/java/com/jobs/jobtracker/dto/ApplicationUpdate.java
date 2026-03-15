package com.jobs.jobtracker.dto;

import com.jobs.jobtracker.model.Status;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ApplicationUpdate {
    private Status status;
    private String recruiterNotes;
    private LocalDateTime interviewDate;

}
