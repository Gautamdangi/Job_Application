package com.jobs.jobtracker.Repository;

import com.jobs.jobtracker.Model.JobApplication;
import com.jobs.jobtracker.Model.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends JpaRepository<JobApplication, Long> {

    //checks applicant and applicant  applied for job or not
    boolean existsByApplicantIdAndJobsId(Long applicantId, Long jobsId);

//find jobs by different param
    Page<JobApplication> findByRecruiterId(Long RecruiterId, Pageable pageable);

   Page<JobApplication> findByJobsId(Long jobsId, Pageable pageable);

    Page<JobApplication> findByApplicantId(Long ApplicantId, Pageable pageable);

    Page<JobApplication> findByStatus(Status status, Pageable pageable);

    Page<JobApplication> findByApplicantIdAndStatus(
            Long applicantId,
            Status status,
            Pageable pageable
    );
    //count applications
    long countApplicationsByApplicant(Long applicantId);
    long countApplicationByJobs( Long jobId);


    long countApplicationsByRecruiterAndStatus(
            Long recruiterId, Status status
    );
}
   
