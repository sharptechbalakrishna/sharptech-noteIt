package com.sharp.noteIt.service;

import java.net.URI;

import org.springframework.stereotype.Service;

import com.twilio.http.HttpMethod;
import com.twilio.rest.api.v2010.account.Application;

@Service
public class TwilioService {
	 public Application createApplication() {
	        return Application.creator()
	                .setVoiceMethod(HttpMethod.GET)
	                .setVoiceUrl(URI.create("http://demo.twilio.com/docs/voice.xml"))
	                .setFriendlyName("Phone Me")
	                .create();
	    }
	 
}
