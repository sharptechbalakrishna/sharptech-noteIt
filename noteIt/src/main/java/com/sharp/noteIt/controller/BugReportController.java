package com.sharp.noteIt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sharp.noteIt.model.BugReport;


@RestController
public class BugReportController {
	
	@Autowired
    private JavaMailSender mailSender;

    @PostMapping("/bugreport")
    public String sendBugReport(@RequestBody BugReport report) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("sharptechpriyachowdhary@gmail.com");  // Recipient email
            message.setSubject("Bug Report: " + report.getTitle());
            message.setText("Description: " + report.getDescription());

            mailSender.send(message);
            return "Bug report sent successfully!";
        } catch (Exception e) {
            return "Error while sending bug report: " + e.getMessage();
        }
    }

}
