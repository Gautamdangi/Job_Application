package com.jobs.jobtracker.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users")
@Data
@NoArgsConstructor //FW like Hibernate create object using reflection
@AllArgsConstructor
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name ="user_name",nullable = false)
    private String userName;


    @Column(name = "user_email",nullable = false, unique = true)
    private String userEmail;
    @Column(name= "password")
    private  String password;

    @Column (name = "phoneNum")
    private String phoneNum;

    @Column(name = "resume")
    private String resumePath;

    @Enumerated(EnumType.STRING)
    @Column(name="role",nullable = false)
    private Role role;

    @Column(nullable = false) //checking whether the user present or not using isActive flag for soft delete and preserve data integrity
    private Boolean isActive = true;

    // to handle cross-cutting(AOP) concerns  like auditing and  status
    @CreatedDate
    @Column(name="created_at",nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;




   @OneToMany(mappedBy = "recruiter", cascade = CascadeType.ALL)
    private List<Job> postedJobs = new ArrayList<>(0);

   @OneToMany(mappedBy = "applicant",cascade = CascadeType.ALL)
    private List<JobApplication> applications =new ArrayList<>();



}
