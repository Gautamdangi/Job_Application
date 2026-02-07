package com.jobs.jobtracker.Repository;

import com.jobs.jobtracker.Model.Role;
import com.jobs.jobtracker.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
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
