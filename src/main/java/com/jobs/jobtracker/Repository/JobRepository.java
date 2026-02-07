package com.jobs.jobtracker.Repository;


import com.jobs.jobtracker.Model.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    // here all crud operation done by spring JPA


    //find jobs by different params
    Page<Job> findByIsActiveTrue(Pageable pageable);

    Page<Job> findByRecruiter_Id(Long recruiterId, Pageable pageable);

    Page<Job> findByJobTitle(String jobTitle, Pageable pageable);



    //search jobs
//    @Query("SELECT j FROM Job j WHERE j.isActive = true AND " +
//                 "LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
//                 "LOWER(j.company) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
//                 "LOWER(j.location) LIKE LOWER(CONCAT('%', :keyword, '%'))")
//    Page<Job> searchJob(@Param("keyword") String keyword, Pageable pageable);


    //count jobs
    long countJobsByRecruiter( Long recruiterId);

}


