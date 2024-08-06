package com.sharp.noteIt.service;

import org.springframework.stereotype.Service;
import com.fazecast.jSerialComm.SerialPort;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;



@Service
public class SmsServiceImpl implements SmsService {
	
	
//	private static final String PORT_NAME = "COM3"; // Adjust as necessary
//
//    @Override
//    public void sendSms(String phoneNumber, String message) {
//        SerialPort serialPort = SerialPort.getCommPort(PORT_NAME);
//        serialPort.setComPortParameters(9600, 8, 1, SerialPort.NO_PARITY);
//        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 1000, 0);
//
//        try {
//            serialPort.openPort();
//            serialPort.getOutputStream().write("AT\r".getBytes());
//            Thread.sleep(1000);
//
//            serialPort.getOutputStream().write("AT+CMGF=1\r".getBytes()); // Set SMS mode to text
//            Thread.sleep(1000);
//
//            serialPort.getOutputStream().write(("AT+CMGS=\"" + phoneNumber + "\"\r").getBytes()); // Set recipient phone number
//            Thread.sleep(1000);
//
//            serialPort.getOutputStream().write((message + "\u001A").getBytes()); // Send the message followed by Ctrl+Z
//            Thread.sleep(1000);
//
//            serialPort.closePort();
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to send SMS", e);
//        }
//    }

//    @Value("${twilio.account.sid}")
//    private String accountSid;
//
//    @Value("${twilio.auth.token}")
//    private String authToken;
//
//    @Value("${twilio.phone.number}")
//    private String twilioPhoneNumber;
//
//    @PostConstruct
//    public void init() {
//        Twilio.init(accountSid, authToken);
//    }
//
//    @Override
//    public void sendSms(String to, String body) {
//        Message.creator(
//                new PhoneNumber(to), // To number
//                new PhoneNumber("+12074104737"), // From number
//                body // Message body
//        ).create();
//    }
    
//    @Override
//    public void sendSms(String to, String body) {
//        Message.creator(
//        		new com.twilio.type.PhoneNumber("+916305043307"),
//        	      new com.twilio.type.PhoneNumber("+12074104737"), // From number
//                body // Message body
//        ).create();
//    }
	
	
	
	
//	@Value("${twilio.accountSid}")
//    private String accountSid;
//
//    @Value("${twilio.authToken}")
//    private String authToken;
//
//    @Value("${twilio.phoneNumber}")
//    private String twilioPhoneNumber;
//
//    public void sendSms(String toPhoneNumber, String messageBody) {
//        Twilio.init(accountSid, authToken);
//
//        Message.creator(
//                new PhoneNumber(toPhoneNumber),
//                new PhoneNumber(twilioPhoneNumber),
//                messageBody
//        ).create();
//    }
}