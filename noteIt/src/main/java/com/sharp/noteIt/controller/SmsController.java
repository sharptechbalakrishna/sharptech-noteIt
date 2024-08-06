package com.sharp.noteIt.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sharp.noteIt.model.SmsRequest;
import com.sharp.noteIt.service.MessageService;
import com.sharp.noteIt.service.SmsService;
import com.twilio.rest.serverless.v1.service.environment.Log;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/sms")
@Slf4j
public class SmsController {

//    private static final Logger logger = LoggerFactory.getLogger(SmsController.class);
//
//    @Autowired
//    private SmsService smsService;
//    @PostMapping("/send")
//    public ResponseEntity<String> sendSms(@RequestBody SmsRequest smsRequest) {
//        logger.info("Received SMS request: {}", smsRequest);
//        try {
//            smsService.sendSms(smsRequest.getPhoneNumber(), smsRequest.getMessage());
//            return ResponseEntity.ok("SMS sent successfully");
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body("Failed to send SMS: " + e.getMessage());
//        }
//    }
	
//	 @Autowired
//	    private SmsService smsService;
//
//	    @PostMapping("/send")
//	    public void sendSms(@RequestParam String to, @RequestParam String body) {
//	        smsService.sendSms(to, body);
//	    }
	
	@GetMapping("/processSms")
	public String processSms()
	{
		return "todo";
	}
	@Autowired
	MessageService messageService;
	private static final Logger logger = LoggerFactory.getLogger(SmsController.class);

    @PostMapping("/sendSms")
    public String sendSms(@RequestBody SmsRequest smsRequest) {
        logger.info("processSms Started smsRequest: {}", smsRequest);
        return messageService.sendSms(smsRequest.getPhoneNumber(), smsRequest.getMessage());
    }


}

