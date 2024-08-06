package com.sharp.noteIt.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.sharp.noteIt.service.TwilioService;
import com.twilio.rest.api.v2010.account.Application;

@RestController
@RequestMapping("/api/twilio")
public class TwilioController {

    @Autowired
    private TwilioService twilioService;

    @PostMapping("/createApplication")
    public String createApplication() {
        Application application = twilioService.createApplication();
        return "Application created with SID: " + application.getSid();
    }
}

