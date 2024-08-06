package com.sharp.noteIt.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import jakarta.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MessageService {
	
	@Value("${TWILIO_ACCOUNT_SID}")
	String ACCOUNT_SID;
	
	@Value("${TWILIO_AUTH_TOKEN}")
	String AUTH_TOKEN;
	
	@Value("${TWILIO_PHONE_NUMBER}")
	String PHONE_NUMBER;
	
	 private static final Logger logger = LoggerFactory.getLogger(MessageService.class);

//	    public MessageService() {
//	        logger.info("Creating class MessageService");
//	        logger.info("ACCOUNT_SID: {}", ACCOUNT_SID);
//	    }
	@PostConstruct
	private void setup() {
		 //logger.info("ACCOUNT_SID: {}", ACCOUNT_SID);
		 Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
	}
	public String sendSms(String smsNumber, String smsMessage)
	{
		 Message message = Message.creator(
               new PhoneNumber(smsNumber), // To
               new PhoneNumber(PHONE_NUMBER), // From
               smsMessage// Body
           ).create();
		return message.getStatus().toString();
	}

}
