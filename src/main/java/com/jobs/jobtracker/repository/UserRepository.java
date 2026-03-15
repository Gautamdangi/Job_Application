package com.jobs.jobtracker.repository;

import com.jobs.jobtracker.model.Role;
import com.jobs.jobtracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {


boolean existsByUserName(String userName);


    Optional<User> findByUserName(String userName);


    Optional<User> findByUserEmail(String userEmail);

    List<User> findByRole(Role role);

    List<User> findActiveUsersByRole( Role role);

}
