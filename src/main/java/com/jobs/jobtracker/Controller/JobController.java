package com.jobs.jobtracker.Controller;

import com.jobs.jobtracker.DTO.CreateJobDTO;
import com.jobs.jobtracker.DTO.JobResponseDTO;
import com.jobs.jobtracker.DTO.UpdateJobDTO;
import com.jobs.jobtracker.Service.JobServiceImpliment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jobs")
public class JobController {
    @Autowired
    JobServiceImpliment jobApplicationServiceImpl;


    @GetMapping("/test-auth")
    public ResponseEntity<String> testAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok("Authenticated as: " + auth.getName());
    }




    @PostMapping("/create")
    public ResponseEntity<JobResponseDTO> createJob(@RequestBody CreateJobDTO createJob){

        System.out.println("Company = " + createJob.getCompany());
        System.out.println("JobTitle = " + createJob.getJobTitle());
        System.out.println("Link = " + createJob.getLink());

        return ResponseEntity.ok(
                jobApplicationServiceImpl.create(createJob)
        );
    }
    @PostMapping("/update")
    public ResponseEntity<JobResponseDTO> update(@RequestBody UpdateJobDTO updateJob){
        return ResponseEntity.ok(jobApplicationServiceImpl.update(updateJob));
    }
    @GetMapping("/{id}")
    public ResponseEntity<JobResponseDTO> getJobById(
            @PathVariable Long id) {


        return ResponseEntity.ok(jobApplicationServiceImpl.getById(id));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteJob(
            @PathVariable Long id) {

        jobApplicationServiceImpl.delete(id);
        return ResponseEntity.ok("Job deleted successfully");
    }


}
