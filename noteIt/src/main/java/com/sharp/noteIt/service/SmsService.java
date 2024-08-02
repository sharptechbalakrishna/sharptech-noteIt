package com.sharp.noteIt.service;

public interface SmsService {
   // void sendSms(String to, String body);
    void sendSms(String phoneNumber, String message);
    
}
