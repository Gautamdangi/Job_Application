package com.jobs.jobtracker.model;

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

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name ="jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String company;
    private String jobTitle;
    private String applyLink;
    private String location;

    private String notes;


    @Column(nullable = false)
    private Boolean isActive = true; // For soft delete-> for auditing

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt; // Track creation (set in service/pre-persist)

    @LastModifiedDate
    private LocalDateTime updatedAt;

    //    deadline to apply
    private LocalDateTime deadline;

    private String salaryRange;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruiter_id", nullable = false)
    private User recruiter;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL)
    private List<JobApplication> applications = new ArrayList<>();

}