package com.jobs.jobtracker.Repository;

import com.jobs.jobtracker.Model.JobApplication;
import com.jobs.jobtracker.Model.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends JpaRepository<JobApplication, Long> {

    //checks applicant and applicant  applied for job or not
    boolean existsByApplicantIdAndJobId(Long applicantId, Long jobId);

//find jobs by different param
@Query("SELECT a FROM JobApplication a WHERE a.job.recruiter.id = :recruiterId")
    Page<JobApplication> findByRecruiter_Id(Long recruiterId, Pageable pageable);

   Page<JobApplication> findByJobId(Long jobId, Pageable pageable);

    Page<JobApplication> findByApplicantId(Long applicantId, Pageable pageable);

    Page<JobApplication> findByStatus(Status status, Pageable pageable);

    Page<JobApplication> findByApplicantIdAndStatus(
            Long applicantId,
            Status status,
            Pageable pageable
    );
    //count applications
    long countApplicationsByApplicant(Long applicantId);
    long countByJob_Id( Long jobId);

    long countApplicationsByJob( Long jobId);
    long countByJob_Recruiter_IdAndStatus(
            Long recruiterId, Status status
    );
}
   
