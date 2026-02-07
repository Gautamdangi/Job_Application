package com.jobs.jobtracker.Controller;

import com.jobs.jobtracker.DTO.CreateJobDTO;
import com.jobs.jobtracker.DTO.JobResponseDTO;
import com.jobs.jobtracker.DTO.PageResponse;
import com.jobs.jobtracker.DTO.UpdateJobDTO;

import com.jobs.jobtracker.Model.User;
import com.jobs.jobtracker.Service.JobServiceImplement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recruiter/jobs")
@RequiredArgsConstructor

public class RecruiterJobController {
    private final JobServiceImplement jobServiceImplement;

    @PostMapping
    @PreAuthorize("hasRole('Recruiter')")
    public ResponseEntity<JobResponseDTO> create(

           @Valid @RequestBody CreateJobDTO createJobDTO,
            @AuthenticationPrincipal User recruiter
            ){
       // String recruiterUserName = authentication.getName();

        JobResponseDTO job = jobServiceImplement.create(createJobDTO, recruiter);
       return ResponseEntity.status(HttpStatus.CREATED)
               .body(job );

    }

    @GetMapping("/jobId")
    public ResponseEntity<JobResponseDTO> update(
            @PathVariable Long jobId,
            @RequestBody UpdateJobDTO updateJobDTO,
            @AuthenticationPrincipal User recruiter
            )
    {
        JobResponseDTO job = jobServiceImplement.update(jobId,updateJobDTO,recruiter);
        return  ResponseEntity.ok(job);
    }

    @DeleteMapping("/jobid")
    public ResponseEntity<String> deleteJob(
            @PathVariable Long jobId,
            @AuthenticationPrincipal User recruiter
    )
    {
       jobServiceImplement.delete(jobId,recruiter);
       return ResponseEntity.ok("Job deleted successfully");

    }

    @GetMapping
    public ResponseEntity<PageResponse<JobResponseDTO>> getMyJobs(
            @AuthenticationPrincipal User recruiter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<JobResponseDTO> jobs = jobServiceImplement.getJobByRecruiter_Id(recruiter.getId(), pageable);

        return ResponseEntity.ok(new PageResponse<>(
                jobs.getContent(),
                jobs.getNumber(),
                jobs.getSize(),
                jobs.getTotalElements(),
                jobs.getTotalPages(),
                jobs.isLast()

       ));
}

@GetMapping("/{jobId}/getjobs")
public ResponseEntity<JobResponseDTO> getJob(@PathVariable Long jobId) {
    JobResponseDTO job = jobServiceImplement.getJobById(jobId);
    return ResponseEntity.ok(job);
}

}