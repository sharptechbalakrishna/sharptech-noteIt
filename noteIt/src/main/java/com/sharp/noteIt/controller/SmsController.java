package com.sharp.noteIt.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sharp.noteIt.model.MessageRequest;
import com.sharp.noteIt.service.MessageService;

@RestController
@RequestMapping("/api/sms")
public class SmsController {
    private static final Logger logger = LoggerFactory.getLogger(SmsController.class);

    @Autowired
    private MessageService messageService;

    @PostMapping("/sendSms")
    public ResponseEntity<String> sendSms(@RequestBody MessageRequest messageRequest) {
        logger.info("Processing SMS request: {}", messageRequest);
        try {
            String status = messageService.sendSms(messageRequest.getToPhoneNumber(), messageRequest.getBody());
            return ResponseEntity.ok("SMS sent successfully with status: " + status);
        } catch (Exception e) {
            logger.error("Failed to send SMS", e);
            return ResponseEntity.status(500).body("Failed to send SMS: " + e.getMessage());
        }
    }
}
