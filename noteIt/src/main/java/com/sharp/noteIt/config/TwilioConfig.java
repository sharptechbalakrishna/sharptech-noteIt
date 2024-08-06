//package com.sharp.noteIt.config;
//import com.twilio.Twilio;
//
//import jakarta.annotation.PostConstruct;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//
//
//
//@Configuration
//public class TwilioConfig {
//	  @Value("${twilio.accountSid}")
//	    private String accountSid;
//
//	    @Value("${twilio.authToken}")
//	    private String authToken;
//
//	    @PostConstruct
//	    public void init() {
//	        // Initialize Twilio with your Account SID and Auth Token
//	        Twilio.init(accountSid, authToken);
//	    }
//
//public String getAccountSid() {
//	return accountSid;
//}
//
//public void setAccountSid(String accountSid) {
//	this.accountSid = accountSid;
//}
//
//public String getAuthToken() {
//	return authToken;
//}
//
//public void setAuthToken(String authToken) {
//	this.authToken = authToken;
//}
//
//
//}
//
////reddy