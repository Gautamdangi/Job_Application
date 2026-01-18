package com.jobs.jobtracker.DTO;

import com.jobs.jobtracker.Model.Status;
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
