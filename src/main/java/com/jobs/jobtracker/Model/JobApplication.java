package com.jobs.jobtracker.Model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
//Unique constrains is used to enforces uniqueness in DB level allow unique characters
@Table(name = "job_applications",
        uniqueConstraints = @UniqueConstraint(columnNames = {"applicant_id","job_id"}))
@EntityListeners(AuditingEntityListener.class)
@Builder// pattern from lombok instead of @Data to remove messy constructors ,easy to set values
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class JobApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name= "applicant_id", nullable = false)
    private User applicant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id",nullable = false)
    private Jobs jobs;

    @Column(nullable = false)
    private String resumePath;

    @Enumerated(EnumType.STRING)
    private Status status = Status.Applied;


    private  String coverLetter;
    private  String recruiterNotes;


    private LocalDateTime interviewDate;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime appliedAT;

    @LastModifiedDate
    private LocalDateTime updateAt;





}
