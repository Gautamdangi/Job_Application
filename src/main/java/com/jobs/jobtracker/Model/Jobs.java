package com.jobs.jobtracker.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name ="Job_applications")
public class Jobs {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   private  String company;
    private String jobTitle;
    private  String Link;
    private LocalDate appliedDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String notes;

    //connect to user
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
