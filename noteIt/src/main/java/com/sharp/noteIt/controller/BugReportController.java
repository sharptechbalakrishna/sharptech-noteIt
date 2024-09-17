package com.sharp.noteIt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sharp.noteIt.model.BugReport;
import com.sharp.noteIt.model.BugReportRequest;
import com.sharp.noteIt.service.EmailService;


@RestController
public class BugReportController {
	
//	@Autowired
//    private JavaMailSender mailSender;
//
//    @PostMapping("/bugreport")
//    public String sendBugReport(@RequestBody BugReport report) {
//        try {
//            SimpleMailMessage message = new SimpleMailMessage();
//            message.setTo("sharptechpriyachowdhary@gmail.com");  // Recipient email
//            message.setSubject("Bug Report: " + report.getTitle());
//            message.setText("Description: " + report.getDescription());
//
//            mailSender.send(message);
//            return "Bug report sent successfully!";
//        } catch (Exception e) {
//            return "Error while sending bug report: " + e.getMessage();
//        }
//    }
	
	
	@Autowired
    private EmailService emailService;

    @PostMapping("/send-bug-report")
    public String sendBugReport(@RequestBody BugReportRequest bugReportRequest) {
        String fromEmail = bugReportRequest.getEmail();
        String bugMessage = bugReportRequest.getBugMessage();
        String title = bugReportRequest.getTitle();  // Extract title from the request
        
        String toEmail = "sharptechpriyachowdhary@gmail.com";  // Your receiver's email
        String subject = "Bug Report: " + title;  // Use the title in the subject

        emailService.sendBugReport(fromEmail, toEmail, subject, title, bugMessage);

        return "Bug report sent successfully";
    }

}
