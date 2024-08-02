//package com.sharp.noteIt.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RestController;
//
//import lombok.Value;
//
//@RestController
//public class TwilioController {
//	
//	@Autowired
//	TwilionService twillioService;
//
//	@Value("${app.twillio.fromPhoneNo}")
//	private String from;
//
//	@Value("${app.twillio.toPhoneNo}")
//	private String to;
//
//	@GetMapping("/sendSms")
//	public String sendSms() {
//
//		String body = "Hello. Good Morning!!";
//		twillioService.sendSms(to, from, body);
//		return "message sent successfully";
//
//
//	}
//
//}
