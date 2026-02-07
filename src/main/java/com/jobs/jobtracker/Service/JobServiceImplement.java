package com.jobs.jobtracker.Service;

import com.jobs.jobtracker.DTO.CreateJobDTO;
import com.jobs.jobtracker.DTO.JobResponseDTO;
import com.jobs.jobtracker.DTO.UpdateJobDTO;
import com.jobs.jobtracker.Model.Job;
import com.jobs.jobtracker.Model.User;
import com.jobs.jobtracker.Repository.ApplicationRepository;
import com.jobs.jobtracker.Repository.JobRepository;
import com.jobs.jobtracker.Repository.UserRepository;
import com.jobs.jobtracker.exceptions.ResourceNotFoundException;
import com.jobs.jobtracker.exceptions.UnauthorizedException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class JobServiceImplement {
    @Autowired private JobRepository jobRepo;
    @Autowired private UserRepository userRepo;
    @Autowired private ApplicationRepository applicationRepo;


    @Transactional
    public JobResponseDTO create(CreateJobDTO createJob, User recruiter) {
        var job = new Job();
        job.setCompany(createJob.getCompany());
        job.setJobTitle(createJob.getJobTitle());
        job.setApplyLink(createJob.getApplyLink());
        job.setIsActive(true);
        job.setLocation(createJob.getLocation());
        job.setSalaryRange(createJob.getSalaryRange());

        job.setRecruiter(recruiter);

        log.info("Job created by recruiter" + job.getJobTitle());
        job = jobRepo.save(job);
        return mapToJobResponseDTO(job);
    }


    @Transactional
    public JobResponseDTO update( Long jobId,UpdateJobDTO updateJob, User recruiter) {
        var job = jobRepo.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("job not found with id"));

        if(!job.getRecruiter().getId().equals(recruiter.getId())){
            throw new UnauthorizedException("You are not authorize to make changes in this job");
        }
        if(updateJob.getJobTitle() != null) job.setJobTitle(updateJob.getJobTitle());
        if(updateJob.getSalaryRange() != null) job.setSalaryRange(updateJob.getSalaryRange());
        if(updateJob.getDeadline() != null) job.setDeadline(updateJob.getDeadline().atStartOfDay());
        if(updateJob.getIsActive() != null) job.setIsActive(updateJob.getIsActive());


        Job jobupdate =jobRepo.save(job);

        return mapToJobResponseDTO(jobupdate);

    }

    public Page<JobResponseDTO> getAllActiveJob(Pageable pageable) {
        return jobRepo.findByIsActiveTrue(pageable).map(this::mapToJobResponseDTO);
    }

    public Page<JobResponseDTO> getJobByRecruiter_Id(Long recruiterId, Pageable pageable) {
        return jobRepo.findByRecruiter_Id(recruiterId, pageable)
                .map(this::mapToJobResponseDTO);
    }

//
//    public Page<JobResponseDTO> searchJob(String keyword, Pageable pageable) {
//        return jobRepo.searchJob(keyword, pageable)
//                .map(this::mapToJobResponseDTO);
//    }


    @Transactional
    public void delete(Long jobId, User recruiter) {
        var job = jobRepo.findById(jobId)
                .orElseThrow(()-> new ResourceNotFoundException("job not found"));

        if(!job.getRecruiter().getId().equals(recruiter.getId())){
            throw new UnauthorizedException("you are not authorize to delete this job");
        }
        jobRepo.delete(job);
        log.info("job deleted: " ,jobId);

    }


    public JobResponseDTO getJobById(Long jobId) {
        var job = jobRepo.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));
        return  mapToJobResponseDTO(job);
    }

//    private JobResponseDTO (Job job) {
//        long applicationCount = applicationRepo.countApplicationsByJob(job.getId());
//        return new JobResponseDTO();
//    }


private JobResponseDTO mapToJobResponseDTO(Job job) {

    long applicationCount = applicationRepo.countApplicationsByJob(job.getId());

    return new JobResponseDTO(
            job.getId(),
            job.getCompany(),
            job.getJobTitle(),
            job.getSalaryRange(),
            job.getLocation(),
            job.getNotes(),
            job.getDeadline(),
            job.getApplyLink(),
            job.getIsActive(),
            job.getCreatedAt(),
            job.getUpdatedAt(),
        (int) applicationCount

    );
}

}
