package com.jobs.jobtracker.Controller;

import com.jobs.jobtracker.DTO.JobResponseDTO;
import com.jobs.jobtracker.DTO.PageResponse;
import com.jobs.jobtracker.Service.JobServiceImplement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/applicant/jobs")
@RequiredArgsConstructor
public class ApplicantJobController {
    private final JobServiceImplement jobServiceImplement;

    @GetMapping
    public ResponseEntity<PageResponse<JobResponseDTO>> getAllJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "0") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<JobResponseDTO> jobs = jobServiceImplement.getAllActiveJob(pageable);

        return ResponseEntity.ok(new PageResponse<>(
                jobs.getContent(),
                jobs.getNumber(),
                jobs.getSize(),
                jobs.getTotalElements(),
                jobs.getTotalPages(),
                jobs.isLast()
        ));

    }

//    @GetMapping("/search")
//    public ResponseEntity<PageResponse<JobResponseDTO>> searchJobs(
//            @RequestParam String keyword,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size
//    ) {
//        Pageable pageable = PageRequest.of(page, size);
//        Page<JobResponseDTO> jobs = jobServiceImplement.searchJobs(keyword, pageable);
//
//        return ResponseEntity.ok(new PageResponse<>(
//                jobs.getContent(),
//                jobs.getNumber(),
//                jobs.getSize(),
//                jobs.getTotalElements(),
//                jobs.getTotalPages(),
//                jobs.isLast()
//        ));
//    }

    @GetMapping("/{jobId}")
    public ResponseEntity<JobResponseDTO> getJobById(@PathVariable Long jobId) {
        JobResponseDTO job = jobServiceImplement.getJobById(jobId);

        return ResponseEntity.ok(job);
    }
}
