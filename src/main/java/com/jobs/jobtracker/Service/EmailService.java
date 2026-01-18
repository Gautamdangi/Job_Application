package com.jobs.jobtracker.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendApplicationConfirmation(String toEmail, String applicantName, String jobTitle){
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Application Submitted Successfully");
            message.setText(String.format(
                    """
                            Dear %s,
                            
                            Your application for the position '%s' has been submitted successfully.
                            
                            We will review your application and get back to you soon.
                            
                            Best regards,
                            ATS Team""",
                    applicantName,jobTitle
            ));

            mailSender.send(message);


        }catch (Exception _){

        }
    }

    public  void sendStatusUpdateEmail(String toEmail, String jobTitle, String applicantName, String newStatus) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Application Status Update Successfully");
            message.setText(String.format(
                    """
                            Dear %s,
                            
                            Your application for the position '%s' has been update successfully.
                            
                            Please login to  review your application for more details .
                            
                            Best regards,
                            ATS Team""",
                    applicantName, jobTitle, newStatus
            ));

            mailSender.send(message);


        } catch (Exception _) {

        }
    }

        public  void sendInterviewInvitation(String toEmail, String applicantName, String jobTitle, String interviewDate){
            try{
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom(fromEmail);
                message.setTo(toEmail);
                message.setSubject("Interview Invitation");
                message.setText(String.format(
                        """
                                Dear %s,
                                
                                Congratulations! You have been shortlisted for an interview for '%s'.
                                
                                Interview Date: %s
                                
                                Please confirm your availability.
                                
                                Best regards,
                                ATS Team""",
                        applicantName,jobTitle,interviewDate
                ));

                mailSender.send(message);


            }catch (Exception _){

            }
    }
}

