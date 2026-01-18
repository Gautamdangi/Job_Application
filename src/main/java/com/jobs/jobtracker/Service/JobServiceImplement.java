package com.jobs.jobtracker.Service;

import com.jobs.jobtracker.DTO.CreateJobDTO;
import com.jobs.jobtracker.DTO.JobResponseDTO;
import com.jobs.jobtracker.DTO.UpdateJobDTO;
import com.jobs.jobtracker.Model.Jobs;
import com.jobs.jobtracker.Model.User;
import com.jobs.jobtracker.Repository.ApplicationRepository;
import com.jobs.jobtracker.Repository.JobsRepository;
import com.jobs.jobtracker.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class JobServiceImplement {
    @Autowired private JobsRepository jobRepo;
    @Autowired private UserRepository userRepo;
    @Autowired private ApplicationRepository ApplicationRepo;


    @Transactional
    public JobResponseDTO create(CreateJobDTO createJob, User recruiter) {
        var job = new Jobs();
        job.setCompany(createJob.getCompany());
        job.setJobTitle(createJob.getJobTitle());
        job.setSalaryRange(createJob.getSalaryRange());
        job.setLink(createJob.getLink());
        job.setLocation(createJob.getLocation());
        job.setDeadline(createJob.getDeadline());
        job.setIsActive(true);
        job.setRecruiter(recruiter);

        Jobs jobSaved = jobRepo.save(job);
        return new JobResponseDTO(
                job.getId(),
                job.getCompany(),
                job.getJobTitle(),
                job.getSalaryRange(),
                job.getNotes(),
                job.getDeadline(),
                job.getLink(),
                job.getIsActive(),
                job.getCreatedAt(),
                job.getUpdatedAt()

        );
    }


    @Transactional
    public JobResponseDTO update( Long jobId,UpdateJobDTO updateJob, User recriter) {
        var jobs = jobRepo.findById(jobId)
                .orElseThrow(() -> new RuntimeException("job not found with id"));

        if(!jobs.getRecruiter().getId().equals(recriter.getId())){
            throw new RuntimeException("You are not authorize to make changes in this job");
        }
        if(updateJob.getJobTitle() != null) jobs.setJobTitle(updateJob.getJobTitle());
        if(updateJob.getSalaryRange() != null) jobs.setSalaryRange(updateJob.getSalaryRange());
        if(updateJob.getDeadline() != null) jobs.setDeadline(updateJob.getDeadline().atStartOfDay());
        if(updateJob.getIsActive() != null) jobs.setIsActive(updateJob.getIsActive());


        Jobs jobupdated =jobRepo.save(jobs);

        return new JobResponseDTO(
                jobs.getId(),
                jobs.getCompany(),
                jobs.getJobTitle(),
                jobs.getSalaryRange(),
                jobs.getNotes(),
                jobs.getDeadline(),
                jobs.getLink(),
                jobs.getIsActive(),
                jobs.getCreatedAt(),
                jobs.getUpdatedAt()
        );

    }

    public Page<JobResponseDTO> getAllActiveJobs(Pageable pageable) {
        return jobRepo.findByIsActiveTrue(pageable).map(this::JobResponseDTO);
    }

    public Page<JobResponseDTO> getJobsByRecruiter(Long recruiterId, Pageable pageable) {
        return jobRepo.findByRecruiterId(recruiterId, pageable)
                .map(this::JobResponseDTO);
    }

    private JobResponseDTO JobResponseDTO(Jobs jobs) {
        return new JobResponseDTO(
                jobs.getId(),
                jobs.getCompany(),
                jobs.getJobTitle(),
                jobs.getSalaryRange(),
                jobs.getNotes(),
                jobs.getDeadline(),
                jobs.getLink(),
                jobs.getIsActive(),
                jobs.getCreatedAt(),
                jobs.getUpdatedAt()

        );
    }

    public Page<JobResponseDTO> searchJobs(String keyword, Pageable pageable) {
        return jobRepo.searchJobs(keyword, pageable)
                .map(this::JobResponseDTO);
    }


    @Transactional
    public void delete(Long jobId, User recruiter) {
        var jobs = jobRepo.findById(jobId)
                .orElseThrow(()-> new RuntimeException("job not found"));

        if(!jobs.getRecruiter().getId().equals(recruiter.getId())){
            throw new RuntimeException("you are not authorize to delete this job");
        }
        jobRepo.delete(jobs);

    }


    public JobResponseDTO getJobById(Long jobId) {
        var jobs = jobRepo.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + jobId));
        return new JobResponseDTO();
    }


    //DTO -> Entity
//@Transactional
//@Override
//public JobResponseDTO create(CreateJobDTO dto) {
//
//
//        // validation if job already applied
////        if(jobRepo.findBy(dto.getCompany(),dto.getJobTitle())){// in this we cant find id because we are not manually setting id so that only company name and role
////        throw new ILLegalStateException("Job already applied")
//// }
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String userName = auth.getName();
//
//        System.out.println("Authenticated user: " + userName);
//
//        User user = userRepo.findByUserName(userName)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        Jobs job = new Jobs();
//        job.setCompany(dto.getCompany());
//        job.setJobTitle(dto.getJobTitle());
//        job.setLink(dto.getLink());
//        job.setStatus(Status.Applied);
//                job.setUser(user) ;// Set the user
//        job.setAppliedDate(LocalDate.now());
//
//        //persist/saved
//        Jobs saved = jobRepo.save(job);
//
//// Entity -> DTO
//        return new JobResponseDTO(
//                saved.getId(),
//                saved.getCompany(),
//                saved.getJobTitle(),
//                saved.getLink(),
//                saved.getStatus().name() // name returns enum const
//        );
//    }
//    @Transactional
//    @Override
//    public JobResponseDTO update(UpdateJobDTO dto) {
//
//        //check whether the job is present or not in db
//        Jobs job = Objects.requireNonNull(jobRepo.findById(dto.getId()))
//                .orElseThrow(()->new RuntimeException("Job not Found"));
//
//        if (dto.getJobTitle() != null) {
//            job.setJobTitle(dto.getJobTitle());
//
//        }
////        if(!dto.getJobTitle().equals(job.getJobTitle())){
////
////        }
//
//        if(dto.getStatus()!=null) {
//            job.setStatus(Status.valueOf(dto.getStatus()));
//        }
//        if(dto.getNotes() != null) {
//            job.setNotes(dto.getNotes());
//        }
//
//     Jobs updated = jobRepo.save(job);
//
//
//        return new JobResponseDTO(updated);
//    }
//
//
//
//   @Transactional
//    public void delete(Long id) {
//    if(!jobRepo.existsById(id)) {
//        throw new RuntimeException("Job not found");
//    }
//    jobRepo.deleteById(id);
//   }
//
//    public JobResponseDTO getById(Long id) {
//        Jobs job = jobRepo.findById(id)
//                .orElseThrow(()->new RuntimeException("Job not found"));
//
//
//        return new JobResponseDTO(
//                job.getId(),
//                job.getCompany(),
//                job.getJobTitle(),
//                job.getStatus().name(),
//                job.getNotes()
//        );
//    }
//
//


}
