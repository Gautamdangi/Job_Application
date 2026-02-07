package com.jobs.jobtracker.Controller;


import com.jobs.jobtracker.DTO.ApplicationResponse;
import com.jobs.jobtracker.DTO.PageResponse;
import com.jobs.jobtracker.Model.User;
import com.jobs.jobtracker.Service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.print.Pageable;

@RestController
@RequestMapping("/api/student/applications")
@RequiredArgsConstructor


public class ApplicantApplicationController {
    private final ApplicationService applicationService;

    @PostMapping("/apply")
    public ResponseEntity<ApplicationResponse> applyForJob(
            @RequestParam Long jobId,
            @RequestParam String coverLetter,
            @RequestParam MultipartFile resume,
            @AuthenticationPrincipal User applicant
            ){
        ApplicationResponse application = applicationService.applyForJob(jobId, coverLetter, resume, applicant);
        return ResponseEntity.ok(application);
    }

    @GetMapping("/{applicationId}")
    public ResponseEntity<ApplicationResponse> getApplication(
            @PathVariable Long applicationId,
            @AuthenticationPrincipal User applicant
    ){
        ApplicationResponse application = applicationService.getApplicationById(applicationId, applicant);
        return ResponseEntity.ok(application);
    }

    @GetMapping
    public ResponseEntity<PageResponse<ApplicationResponse>> getMyApplication(
            @AuthenticationPrincipal User applicant,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Pageable pageable = (Pageable) PageRequest.of(page, size, Sort.by("appliedAt").descending());
        Page<ApplicationResponse> applications = applicationService.getApplicantApplications(applicant , (org.springframework.data.domain.Pageable) pageable);

        return ResponseEntity.ok(new PageResponse<>(
                applications.getContent(),
                applications.getTotalPages(),
                applications.getNumber(),
                applications.getTotalElements(),
                applications.getSize(),
                applications.isLast()
        ));

    }

}
