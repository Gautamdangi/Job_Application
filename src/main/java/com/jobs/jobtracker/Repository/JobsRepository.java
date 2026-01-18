package com.jobs.jobtracker.Repository;


import com.jobs.jobtracker.Model.Jobs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface JobsRepository extends JpaRepository<Jobs, Long> {

    Optional<Jobs> findByUser_userId(Long userId);

    // here all crud operation done by spring JPA


}
