package com.jobs.jobtracker.Service;

import com.jobs.jobtracker.DTO.ApplicationResponse;
import com.jobs.jobtracker.DTO.ApplicationUpdate;
import com.jobs.jobtracker.Model.JobApplication;
import com.jobs.jobtracker.Model.Role;
import com.jobs.jobtracker.Model.Status;
import com.jobs.jobtracker.Model.User;
import com.jobs.jobtracker.Repository.ApplicationRepository;
import com.jobs.jobtracker.Repository.JobRepository;
import com.jobs.jobtracker.exceptions.AlreadyJobAppliedException;
import com.jobs.jobtracker.exceptions.JobNotFoundException;
import com.jobs.jobtracker.exceptions.ResourceNotFoundException;
import com.jobs.jobtracker.exceptions.UnauthorizedException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class ApplicationService {
    @Autowired
    private ApplicationRepository applicationRepository;
   @Autowired
   private JobRepository jobRepository;
   @Autowired
    private FileStorageService fileStorageService;
   @Autowired
    private EmailService emailService;

    //apply for job
    @Transactional
    public ApplicationResponse applyForJob(Long jobId, String coverLetter, MultipartFile resume, User applicant) {

        //check job
        var job = jobRepository.findById(jobId).orElseThrow(
                () ->
                        new ResourceNotFoundException(STR."JobId not found: \{jobId}"));


        //status of job
        if (!job.getIsActive()) {
            throw new JobNotFoundException("This job is no longer  accepting responses");
        }

        // already applied by applicant
        if (applicationRepository.existsByApplicantIdAndJobId(applicant.getId(), job.getId())) {
            throw new AlreadyJobAppliedException("You have already applied for this job");
        }
//save resume or in storage given by user
        String resumePath = fileStorageService.storeFile(resume, applicant.getId());


        var application = JobApplication.builder()
                .applicant(applicant)
                .job(job)
                .resumePath(resumePath)
                .status(Status.Applied)
                .coverLetter(coverLetter)
                .build();

        //save to db
        application = applicationRepository.save(application);

        log.info("Application submitted" ,applicant.getUserEmail(),jobId);

        //send confirmation mail
        emailService.sendApplicationConfirmation(applicant.getUserEmail(), applicant.getUserName(), job.getJobTitle());

        return new ApplicationResponse(
                application.getId(),
                application.getJob().getId(),
                application.getJob().getJobTitle(),
                application.getJob().getCompany(),
                application.getApplicant().getUserName(),
                application.getApplicant().getUserEmail(),
                application.getResumePath(),
                application.getCoverLetter(),
                application.getStatus(),
                application.getRecruiterNotes(),
                application.getInterviewDate(),
                application.getAppliedAT(),
                application.getUpdateAt()
        );

    }


//update application
    @Transactional
    public ApplicationResponse updateApplication(Long  applicantId, ApplicationUpdate update, User recruiter){
        var application = applicationRepository.findById(applicantId).orElseThrow(()-> new RuntimeException("Application not found"));

                //auth check who can update this application
        if (!application.getJob().getRecruiter().getId().equals(recruiter.getId())) {
            throw new RuntimeException("you are not authorize to update tns application");
        }
        application.setStatus(update.getStatus());
        if(update.getRecruiterNotes() != null){
            application.setRecruiterNotes(update.getRecruiterNotes());

        }
        if (update.getInterviewDate() != null) {

            application.setInterviewDate(update.getInterviewDate());
        }

        application = applicationRepository.save(application);
        log.info("Application updated",applicantId,update.getStatus());

        // Send status update email
        emailService.sendStatusUpdateEmail(
                application.getApplicant().getUserEmail(),
                application.getApplicant().getUserName(),
                application.getJob().getJobTitle(),
                update.getStatus().name()
        );

        return new ApplicationResponse(
                application.getId(),
                application.getJob().getId(),
                application.getJob().getJobTitle(),
                application.getJob().getCompany(),
                application.getApplicant().getUserName(),
                application.getApplicant().getUserEmail(),
                application.getResumePath(),
                application.getCoverLetter(),
                application.getStatus(),
                application.getRecruiterNotes(),
                application.getInterviewDate(),
                application.getAppliedAT(),
                application.getUpdateAt()
        );
    }

    // get application by users recruiter and applicant
    public ApplicationResponse getApplicationById(Long applicationId, User user) {
        var application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with id: " + applicationId));

        boolean isApplicant = application.getApplicant().getId().equals(user.getId());
        boolean isRecruiter = application.getJob().getRecruiter().getId().equals(user.getId());
        boolean isAdmin = user.getRole() ==
                Role.Admin;

        if (!isApplicant && !isRecruiter && !isAdmin) {
            throw new UnauthorizedException("You are not authorized to view this application");
        }

        return mapToApplicationResponse(application);
    }

    private ApplicationResponse mapToApplicationResponse(JobApplication application) {
        return new ApplicationResponse(
                application.getId(),
                application.getJob().getId(),
                application.getJob().getJobTitle(),
                application.getJob().getCompany(),
                application.getApplicant().getUserName(),
                application.getApplicant().getUserEmail(),
                application.getResumePath(),
                application.getCoverLetter(),
                application.getStatus(),
                application.getRecruiterNotes(),
                application.getInterviewDate(),
                application.getAppliedAT(),
                application.getUpdateAt()
        );
    }


    //get applications submitted by user By recruiter
    public Page<ApplicationResponse> getApplicantApplications(User applicant, Pageable pageable){
        return applicationRepository.findByApplicantId(applicant.getId(), pageable)
                .map(this::mapToApplicationResponse);
    }


    public Page<ApplicationResponse> getJobApplications(Long jobId, User recruiter, Pageable pageable){
        var job = jobRepository.findById(jobId)
                .orElseThrow(()-> new ResourceNotFoundException("Job not found"));

        if(!job.getRecruiter().getId().equals(recruiter.getId())){
            throw new UnauthorizedException("You are not authorize to view this application");
        }
        return applicationRepository.findByJobId(jobId, pageable)
                .map(this::mapToApplicationResponse);
    }



    public Page<ApplicationResponse>getRecruiterApplications(User recruiter, Pageable pageable){
        return applicationRepository.findByRecruiter_Id(recruiter.getId(), pageable)
                .map((this:: mapToApplicationResponse));

    }
}


