package com.sharp.noteIt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sharp.noteIt.model.BorrowerDoc;
import com.sharp.noteIt.model.BorrowerRequest;
import com.sharp.noteIt.model.CustomerDoc;
import com.sharp.noteIt.model.LedgerCal;
import com.sharp.noteIt.model.LedgerUpdateRequest;
import com.sharp.noteIt.model.LedgerUpdateResponse;
import com.sharp.noteIt.service.LedgerService;
import com.sharp.noteIt.service.MessageService;
import com.sharp.noteIt.service.SmsService;


@RestController
public class LedgerController {
	
	@Autowired
	LedgerService ledgerService;
	
//	@PostMapping("/{customerId}/borrowers")
//    public ResponseEntity<?> addBorrowerToCustomer(@PathVariable Long customerId, @RequestBody BorrowerDoc borrowerDoc) {
//        CustomerDoc customer = ledgerService.addBorrowerToCustomer(customerId, borrowerDoc);
//        return ResponseEntity.ok(customer);
//    }
	
//	@Autowired
//	MessageService messageService;
//	@PostMapping("/{customerId}/borrowers")
//    public ResponseEntity<?> addBorrowerToCustomer(@PathVariable Long customerId, @RequestBody BorrowerDoc borrowerDoc) {
//        // Add borrower to customer
//        CustomerDoc customer = ledgerService.addBorrowerToCustomer(customerId, borrowerDoc);
//
//        // Fetch customer and borrower details
//        String customerName = customer.getUserName();
//        String borrowerName = borrowerDoc.getBorrowerName();
//        Double principalAmount = borrowerDoc.getPrincipalAmount();
//        Double interestRate = borrowerDoc.getInterestRate();
//
//        // Create message for borrower addition
//        String additionMessage = String.format(
//            "Hello %s, you have been added as a borrower for %s. Details: Principal Amount: %.2f, Interest Rate: %.2f.",
//            borrowerName,
//            customerName,
//            principalAmount,
//            interestRate
//        );
//
//        // Send SMS to borrower for addition
//        messageService.sendSms(borrowerDoc.getPhoneNumber(), additionMessage);
//
//        // Notify about due payments
//        notifyDuePayments(borrowerDoc);
//
//        return ResponseEntity.ok(customer);
//    }

//    private void notifyDuePayments(BorrowerDoc borrowerDoc) {
//        // Logic to calculate due payments
//        // You need to have a mechanism to calculate dues and the notification message
//
//        // Example due message
//        String dueMessage = "Reminder: Your due payment for this month is approaching. Please ensure that the payment is made before the 5th of the month.";
//
//        // Send SMS notification about due payments
//        messageService.sendSms(borrowerDoc.getPhoneNumber(), dueMessage);
//    }
	
	
	
	
	@PostMapping("/{customerId}/borrowers")
	public ResponseEntity<?> addBorrowerToCustomer(@PathVariable Long customerId, @RequestBody BorrowerDoc borrowerDoc) {
	    // Add borrower to customer
	    CustomerDoc customer = ledgerService.addBorrowerToCustomer(customerId, borrowerDoc);

	    // Optionally, fetch customer and borrower details if needed
	    String customerName = customer.getUserName();
	    String borrowerName = borrowerDoc.getBorrowerName();
	    Double principalAmount = borrowerDoc.getPrincipalAmount();
	    Double interestRate = borrowerDoc.getInterestRate();

	    // You can log the details or perform other operations if needed
	    System.out.println(String.format(
	        "Borrower %s added to customer %s with Principal Amount: %.2f and Interest Rate: %.2f.",
	        borrowerName, customerName, principalAmount, interestRate
	    ));

	    // If you don't want to send SMS messages, just return the response
	    return ResponseEntity.ok(customer);
	}

	
	 @PostMapping("/ledger/update")
	    public ResponseEntity<LedgerUpdateResponse> updateLedger(@RequestBody LedgerUpdateRequest request) {
	        LedgerUpdateResponse response = ledgerService.updateInterestPaid(request);
	        return ResponseEntity.ok(response);
	    }

    @GetMapping("/borrowers/{borrowerId}/ledger")
    public ResponseEntity<?> getAndUpdateLedger(@PathVariable Long borrowerId) {
        BorrowerDoc borrowerDoc = ledgerService.calculateAndUpdateLedger(borrowerId);
        return ResponseEntity.ok(borrowerDoc);
    }
    
    
    @GetMapping("/borrower/{borrowerId}")
    public BorrowerDoc getBorrowerById(@PathVariable Long borrowerId) {
        return ledgerService.getBorrowerById(borrowerId);
    }

    // Get all ledgers for a specific borrower
    @GetMapping("/ledger/{borrowerId}")
    public List<LedgerCal> getLedgerByBorrowerId(@PathVariable Long borrowerId) {
        return ledgerService.getLedgerByBorrowerId(borrowerId);
    }
    
    @GetMapping( "/{borrowerId}/{ledgerId}")
    public ResponseEntity<LedgerCal> getLedgerByBorrowerAndLedgerId(@PathVariable Long borrowerId, @PathVariable Long ledgerId) {
        LedgerCal ledger = ledgerService.getLedgerByBorrowerAndLedgerId(borrowerId, ledgerId);
        if (ledger != null) {
            return ResponseEntity.ok(ledger);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/{customerId}/borrowers")
    public List<BorrowerRequest> getBorrowersForCustomer(@PathVariable Long customerId) {
        return ledgerService.getBorrowersForCustomer(customerId);
    }
//    @DeleteMapping("/{customerId}/borrowers")
//    public ResponseEntity<?> deleteBorrowersByCustomerId(@PathVariable Long customerId) {
//    	ledgerService.deleteBorrowersByCustomerId(customerId);
//        return ResponseEntity.ok().body("Borrowers deleted for customer with id: " + customerId);
//    }
    @DeleteMapping("/borrower/{borrowerId}/ledger/{ledgerId}")
    public void deleteLedgerByBorrowerId(@PathVariable Long borrowerId, @PathVariable Long ledgerId) {
        ledgerService.deleteLedgerByBorrowerId(borrowerId, ledgerId);
    }

}
