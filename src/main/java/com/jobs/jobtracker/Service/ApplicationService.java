package com.jobs.jobtracker.Service;

import com.jobs.jobtracker.DTO.ApplicationResponse;
import com.jobs.jobtracker.DTO.ApplicationUpdate;
import com.jobs.jobtracker.Model.JobApplication;
import com.jobs.jobtracker.Model.Role;
import com.jobs.jobtracker.Model.Status;
import com.jobs.jobtracker.Model.User;
import com.jobs.jobtracker.Repository.ApplicationRepository;
import com.jobs.jobtracker.Repository.JobsRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public class ApplicationService {
    private ApplicationRepository applicationRepository;
    private JobsRepository jobsRepository;
    private FileStorageService fileStorageService;
    private EmailService emailService;

    //apply for job
    @Transactional
    public ApplicationResponse applyForJob(Long jobId, String coverLetter, MultipartFile resume, User applicant) {

        //check job
        var jobs = jobsRepository.findById(jobId).orElseThrow(
                () ->
                        new RuntimeException(STR."JobId not found: \{jobId}"));

        // add letter exception time


        //status of job
        if (!jobs.getIsActive()) {
            throw new RuntimeException("This job is no longer  accepting responses");
        }

        // already applied by applicant
        if (applicationRepository.existsByApplicantIdAndJobsId(applicant.getId(), jobs.getId())) {
            throw new RuntimeException("you have already applied for this job");
        }
//save resume or in storage given by user
        String resumePath = fileStorageService.storeFile(resume, applicant.getId());


        var application = JobApplication.builder()
                .applicant(applicant)
                .jobs(jobs)
                .resumePath(resumePath)
                .status(Status.Applied)
                .coverLetter(coverLetter)
                .build();

        //save to db
        application = applicationRepository.save(application);

        //send confirmation mail
        emailService.sendApplicationConfirmation(applicant.getUserEmail(), applicant.getUserName(), jobs.getJobTitle());

        return new ApplicationResponse(
                application.getId(),
                application.getJobs().getId(),
                application.getJobs().getJobTitle(),
                application.getJobs().getCompany(),
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
    public ApplicationResponse updateApplication(Long  applicantId, ApplicationUpdate update, User recruiter){
        var application = applicationRepository.findById(applicantId).orElseThrow(()-> new RuntimeException("Application not found"));

                //auth check who can update this application
        if (!application.getJobs().getRecruiter().getId().equals(recruiter.getId())) {
            throw new RuntimeException("you are not authorize to update tns application");
        }
        application.setStatus(update.getStatus());
        if(update.getRecruiterNotes() != null){
            application.setInterviewDate(update.getInterviewDate());
        }

        application = applicationRepository.save(application);

        // Send status update email
        emailService.sendStatusUpdateEmail(
                application.getApplicant().getUserEmail(),
                application.getApplicant().getUserName(),
                application.getJobs().getJobTitle(),
                update.getStatus().name()
        );

        return new ApplicationResponse(
                application.getId(),
                application.getJobs().getId(),
                application.getJobs().getJobTitle(),
                application.getJobs().getCompany(),
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
                .orElseThrow(() -> new RuntimeException("Application not found with id: " + applicationId));

        boolean isApplicant = application.getApplicant().getId().equals(user.getId());
        boolean isRecruiter = application.getJobs().getRecruiter().getId().equals(user.getId());
        boolean isAdmin = user.getRole() ==
                Role.Admin;

        if (!isApplicant && !isRecruiter && !isAdmin) {
            throw new RuntimeException("You are not authorized to view this application");
        }

        return ApplicationResponse(application);
    }

    private ApplicationResponse ApplicationResponse(JobApplication application) {
        return new ApplicationResponse(
                application.getId(),
                application.getJobs().getId(),
                application.getJobs().getJobTitle(),
                application.getJobs().getCompany(),
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
                .map(this::ApplicationResponse);
    }


    public Page<ApplicationResponse> getJobApplications(Long jobsId, User recruiter, Pageable pageable){
        var job = jobsRepository.findById(jobsId)
                .orElseThrow(()-> new RuntimeException("Job not found"));

        if(!job.getRecruiter().getId().equals(recruiter.getId())){
            throw new RuntimeException("You are not authorize to view this application");
        }
        return applicationRepository.findByJobsId(jobsId,pageable)
                .map(this::ApplicationResponse);
    }



    public Page<ApplicationResponse>getRecruiterApplications(User recruiter, Pageable pageable){
        return applicationRepository.findByRecruiterId(recruiter.getId(), pageable)
                .map((this:: ApplicationResponse));

    }
}


