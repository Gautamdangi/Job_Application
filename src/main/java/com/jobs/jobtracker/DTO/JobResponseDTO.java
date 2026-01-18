package com.jobs.jobtracker.DTO;

import com.jobs.jobtracker.Model.Jobs;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobResponseDTO {
    private Long id;
    private String company;
    private String jobTitle;

    private String status;
    private String notes;


    public JobResponseDTO(Jobs updated) {
        
    }
}
