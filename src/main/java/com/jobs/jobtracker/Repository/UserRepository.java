package com.jobs.jobtracker.Repository;

import com.jobs.jobtracker.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {


boolean existsByUserName(String userName);


    <T> Optional<User> findByUserName(String userName);


}
