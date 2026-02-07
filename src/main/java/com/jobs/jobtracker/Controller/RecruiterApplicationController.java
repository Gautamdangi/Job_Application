package com.jobs.jobtracker.Controller;

import com.jobs.jobtracker.DTO.ApplicationResponse;
import com.jobs.jobtracker.DTO.ApplicationUpdate;
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

import java.awt.print.Pageable;

@RestController
@RequestMapping("/api/recruiter/applications")
@RequiredArgsConstructor
public class RecruiterApplicationController {
    private final ApplicationService applicationService;

    @GetMapping
    public ResponseEntity<PageResponse<ApplicationResponse>> getAllApplication(
            @AuthenticationPrincipal User recruiter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Pageable pageable = (Pageable) PageRequest.of(page, size, Sort.by("appliedAt").descending());
        Page<ApplicationResponse> applications = applicationService.getRecruiterApplications(recruiter, (org.springframework.data.domain.Pageable) pageable);

        return ResponseEntity.ok(new PageResponse<>(
                applications.getContent(),
                applications.getTotalPages(),
                applications.getNumber(),
                applications.getTotalElements(),
                applications.getSize(),
                applications.isLast()
        ));

    }

    @GetMapping("/job/{jobId}")
    public ResponseEntity<PageResponse<ApplicationResponse>> getJobApplication(
            @PathVariable Long jobId,
            @AuthenticationPrincipal User recruiter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Pageable pageable = (Pageable) PageRequest.of(page, size, Sort.by("appliedAt").descending());
        Page<ApplicationResponse> applications = applicationService.getJobApplications(jobId, recruiter, (org.springframework.data.domain.Pageable) pageable);

        return ResponseEntity.ok(new PageResponse<>(
                applications.getContent(),
                applications.getTotalPages(),
                applications.getNumber(),
                applications.getTotalElements(),
                applications.getSize(),
                applications.isLast()
        ));

    }

    @PutMapping("/{applicationId}/status")
    public ResponseEntity<ApplicationResponse> updateApplication(
            @PathVariable Long applicationId,
            @RequestBody ApplicationUpdate update,
            @AuthenticationPrincipal User recruiter
            ){
        ApplicationResponse application = applicationService.updateApplication(applicationId, update, recruiter);
        return ResponseEntity.ok(application);
    }

    @GetMapping("/{applicationId}")
    public ResponseEntity<ApplicationResponse> getApplication(
            @PathVariable Long applicationId,
            @AuthenticationPrincipal User recruiter
    ){
        ApplicationResponse application = applicationService.getApplicationById(applicationId, recruiter);
        return ResponseEntity.ok(application);
    }
}
