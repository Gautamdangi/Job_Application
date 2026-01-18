package com.jobs.jobtracker.Service;

import com.jobs.jobtracker.DTO.CreateJobDTO;
import com.jobs.jobtracker.DTO.JobResponseDTO;
import com.jobs.jobtracker.DTO.UpdateJobDTO;
import com.jobs.jobtracker.Model.Jobs;
import com.jobs.jobtracker.Model.Status;
import com.jobs.jobtracker.Model.User;
import com.jobs.jobtracker.Repository.JobsRepository;
import com.jobs.jobtracker.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Objects;


@Service
public class JobServiceImpliment implements JobService {
    @Autowired private  JobsRepository jobRepo;
    @Autowired private UserRepository userRepo;



//DTO -> Entity
    @Transactional
    @Override
    public JobResponseDTO create(CreateJobDTO dto) {


        // validation if job already applied
//        if(jobRepo.findBy(dto.getCompany(),dto.getJobTitle())){// in this we cant find id because we are not manually setting id so that only company name and role
//        throw new ILLegalStateException("Job already applied")
// }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();

        System.out.println("Authenticated user: " + userName);

        User user = userRepo.findByUserName(userName)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Jobs job = new Jobs();
        job.setCompany(dto.getCompany());
        job.setJobTitle(dto.getJobTitle());
        job.setLink(dto.getLink());
        job.setStatus(Status.Applied);
                job.setUser(user) ;// Set the user
        job.setAppliedDate(LocalDate.now());

        //persist/saved
        Jobs saved = jobRepo.save(job);

// Entity -> DTO
        return new JobResponseDTO(
                saved.getId(),
                saved.getCompany(),
                saved.getJobTitle(),
                saved.getLink(),
                saved.getStatus().name() // name returns enum const
        );
    }
    @Transactional
    @Override
    public JobResponseDTO update(UpdateJobDTO dto) {

        //check whether the job is present or not in db
        Jobs job = Objects.requireNonNull(jobRepo.findById(dto.getId()))
                .orElseThrow(()->new RuntimeException("Job not Found"));

        if (dto.getJobTitle() != null) {
            job.setJobTitle(dto.getJobTitle());

        }
//        if(!dto.getJobTitle().equals(job.getJobTitle())){
//
//        }

        if(dto.getStatus()!=null) {
            job.setStatus(Status.valueOf(dto.getStatus()));
        }
        if(dto.getNotes() != null) {
            job.setNotes(dto.getNotes());
        }

     Jobs updated = jobRepo.save(job);


        return new JobResponseDTO(updated);
    }



   @Transactional
    public void delete(Long id) {
    if(!jobRepo.existsById(id)) {
        throw new RuntimeException("Job not found");
    }
    jobRepo.deleteById(id);
   }

    public JobResponseDTO getById(Long id) {
        Jobs job = jobRepo.findById(id)
                .orElseThrow(()->new RuntimeException("Job not found"));


        return new JobResponseDTO(
                job.getId(),
                job.getCompany(),
                job.getJobTitle(),
                job.getStatus().name(),
                job.getNotes()
        );
    }




}
