package com.sharp.noteIt.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sharp.noteIt.model.SmsRequest;
import com.sharp.noteIt.service.SmsService;

@RestController
@RequestMapping("/api/sms")
public class SmsController {

    private static final Logger logger = LoggerFactory.getLogger(SmsController.class);

    @Autowired
    private SmsService smsService;
    @PostMapping("/send")
    public ResponseEntity<String> sendSms(@RequestBody SmsRequest smsRequest) {
        logger.info("Received SMS request: {}", smsRequest);
        try {
            smsService.sendSms(smsRequest.getPhoneNumber(), smsRequest.getMessage());
            return ResponseEntity.ok("SMS sent successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to send SMS: " + e.getMessage());
        }
    }
}

