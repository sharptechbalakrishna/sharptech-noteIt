package com.sharp.noteIt.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.sharp.noteIt.model.BorrowerDoc;

@Service
public class ScheduledNotificationService {

    @Autowired
    private SmsService smsService;
    @Autowired
    MessageService messageService;
    // This method will run on the 1st of every month
    @Scheduled(cron = "0 0 0 1 * ?")
    public void notifyDuePayments() {
        // Fetch all borrowers with due payments
        // You need to implement a method to get all borrowers with pending dues

        // Example code:
        List<BorrowerDoc> borrowers = getBorrowersWithDuePayments();

        for (BorrowerDoc borrower : borrowers) {
            String message = "Reminder: Your due payment for this month is approaching. Please ensure that the payment is made before the 5th of the month.";
            messageService.sendSms(borrower.getPhoneNumber(), message);
        }
    }

    private List<BorrowerDoc> getBorrowersWithDuePayments() {
        // Implement this method to return a list of borrowers with due payments
        // Example return value
        return new ArrayList<>();
    }
}

