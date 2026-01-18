package com.jobs.jobtracker.Service;


import com.jobs.jobtracker.DTO.CreateJobDTO;
import com.jobs.jobtracker.DTO.JobResponseDTO;
import com.jobs.jobtracker.DTO.UpdateJobDTO;

public interface JobService {

    JobResponseDTO create(CreateJobDTO dto);
    JobResponseDTO update(UpdateJobDTO dto);

    void delete(Long id);

    JobResponseDTO getById(Long id);



}
