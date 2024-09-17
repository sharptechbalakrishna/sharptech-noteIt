package com.sharp.noteIt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendBugReport(String fromEmail, String toEmail, String subject, String title, String bugMessage) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("no-reply@example.com"); // You can use a fixed "from" email address
        mailMessage.setTo(toEmail);
        mailMessage.setSubject(subject);

        // Format the email body with the title and sender's email
        String emailBody = String.format(
            "Bug Report Title: %s\n\n" +
            "Sent by: %s\n\n" +
            "Message:\n%s",
            title,
            fromEmail,
            bugMessage
        );

        mailMessage.setText(emailBody);
        mailSender.send(mailMessage);
    }
}
