package com.jobs.jobtracker.Repository;


import com.jobs.jobtracker.Model.Jobs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface JobsRepository extends JpaRepository<Jobs, Long> {

//    Optional<Jobs> findByUserId(Long applicantId);

    // here all crud operation done by spring JPA


    //find jobs by different params
    Page<Jobs> findByIsActiveTrue(Pageable pageable);

    Page<Jobs> findByRecruiterId(Long recruiterId, Pageable pageable);

    Page<Jobs> findByJobTitle(String jobTitle, Pageable pageable);


    //search jobs
    Page<Jobs> searchJobs(String keyword, Pageable pageable);


    //count jobs
    long countJobsByRecruiter( Long recruiterId);

}


