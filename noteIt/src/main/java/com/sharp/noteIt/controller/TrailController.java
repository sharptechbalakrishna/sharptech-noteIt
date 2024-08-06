//package com.sharp.noteIt.controller;
//
//import org.apache.http.HttpStatus;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.sharp.noteIt.config.TwilioConfig;
//import com.twilio.Twilio;
//import com.twilio.rest.api.v2010.account.Message;
//import com.twilio.type.PhoneNumber;
//
//@RestController
//public class TrailController {
//	
//	private final TwilioConfig twilioConfig;
//
//    @Autowired
//    public TrailController(TwilioConfig twilioConfig) {
//        this.twilioConfig = twilioConfig;
//    }
//
//    @PostMapping("/send-sms")
//    public ResponseEntity<String> sendSms(@RequestParam String to, @RequestParam String message) {
//        try {
//            Twilio.init(twilioConfig.getAccountSid(), twilioConfig.getAuthToken());
//            Message.creator(new PhoneNumber(to), new PhoneNumber(twilioConfig.getPhoneNumber()), message).create();
//            return ResponseEntity.ok("SMS sent successfully!");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body("Error sending SMS: " + e.getMessage());
//        }
//    }
//
//}
