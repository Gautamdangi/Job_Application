//package com.jobs.jobtracker.Service;
//
//
//import com.jobs.jobtracker.DTO.CreateJobDTO;
//import com.jobs.jobtracker.DTO.JobResponseDTO;
//import com.jobs.jobtracker.DTO.UpdateJobDTO;
//import com.jobs.jobtracker.Model.User;
//import jakarta.transaction.Transactional;
//
//public interface JobService {
//
//    JobResponseDTO create(CreateJobDTO createJob, User recruiter);
//    JobResponseDTO update(UpdateJobDTO updateJob, Long jobId, User recriter);
//
//    void delete(Long jobId, User recruiter);
//
//    //DTO -> Entity
////        @Transactional
////        JobResponseDTO create(CreateJobDTO dto);
//
////    JobResponseDTO getById(Long jobId);
//
//
//
//}
