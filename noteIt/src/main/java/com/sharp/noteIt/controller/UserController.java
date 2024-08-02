package com.sharp.noteIt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sharp.noteIt.model.BorrowerDoc;
import com.sharp.noteIt.model.BorrowerRequest;
import com.sharp.noteIt.model.CustomerDoc;
import com.sharp.noteIt.model.CustomerRequest;
import com.sharp.noteIt.model.InterestRequest;
import com.sharp.noteIt.model.InterestResponse;
import com.sharp.noteIt.model.LedgerCal;
import com.sharp.noteIt.model.LedgerUpdateRequest;
import com.sharp.noteIt.model.MessageRequest;
import com.sharp.noteIt.model.SelfNotes;
import com.sharp.noteIt.model.SmsRequest;
import com.sharp.noteIt.service.CustomerServiceI;
import com.sharp.noteIt.service.SmsService;

@RestController
public class UserController {

	@Autowired
	CustomerServiceI service;

	@PostMapping("/register")
	public ResponseEntity<?> saveCustomer(@RequestBody CustomerRequest request) {

		CustomerDoc saveCustomer = this.service.saveCustomer(request);
		return new ResponseEntity<>(saveCustomer, HttpStatus.CREATED);
	}

	@GetMapping("/login")
	public CustomerDoc login(@RequestParam String phone, @RequestParam String password) {
		System.out.println("Attempting to login with phone: " + phone); // Print to console
		return service.login(phone, password);
	}
	@GetMapping("/getAllCustomers")
    public List<CustomerDoc> getAllCustomers() {
        return service.getAllCustomers();
    }
	@GetMapping("/userDetails/{firstName}")
    public CustomerDoc getCustomerByName(@PathVariable String firstName) {
        return service.getCustomerProfile(firstName);
    }

	@PostMapping("/calinterest")
    public InterestResponse calculateInterest(@RequestBody InterestRequest request) {
        double principal = request.getPrincipal();
        double interestRate = request.getInterestRate();
        double interest = (principal * interestRate) / 100;

        return new InterestResponse(interest);
    }

//	  @PostMapping("/{customerId}")
//	    public ResponseEntity<BorrowerDoc> saveBorrower(@PathVariable Long customerId, @RequestBody BorrowerDoc borrower) {
//	        return ResponseEntity.ok(service.saveBorrower(customerId, borrower));
//	    }
	
	
	    @PostMapping("/{customerId}/borrowers")
	    public CustomerDoc addBorrowerToCustomer(@PathVariable Long customerId, @RequestBody BorrowerDoc borrowerDoc) {
	        return this.service.addBorrowerToCustomer(customerId, borrowerDoc);
	    }
	    
	    
	    @GetMapping("/{customerId}/borrowers")
	    public List<BorrowerRequest> getBorrowersForCustomer(@PathVariable Long customerId) {
	        return this.service.getBorrowersForCustomer(customerId);
	    }
	    
	    @PostMapping("/saveNotes")
	    public SelfNotes createNote(@RequestBody SelfNotes note) {
	        return this.service.save(note);
	    }
	    @GetMapping("/notesdisplay")
	    public List<SelfNotes> getAllNotes() {
	        return this.service.getAllNotes();
	    }
	    
//       private final SmsService smsService1 = null;
//
//	    @Autowired
//	    private SmsService smsService;
//	    @PostMapping("/send")
//	    public ResponseEntity<String> sendSms(
//	            @RequestParam String phoneNumber,
//	            @RequestParam String message) {
//
//	        try {
//	            smsService1.sendSms(phoneNumber, message);
//	            return ResponseEntity.ok("SMS sent successfully");
//	        } catch (Exception e) {
//	            return ResponseEntity.status(500).body("Failed to send SMS: " + e.getMessage());
//	        }
//	    }
	    
	    @PutMapping("/{id}/calculateLedger")
	    public BorrowerDoc calculateAndUpdateLedger(@PathVariable Long id) {
	        return this.service.calculateAndUpdateLedger(id);
	    }
	    
//	    @GetMapping("/{id}/ledger")
//	    public List<LedgerCal> getLedgerByBorrowerId(@PathVariable Long id) {
//	        return this.service.getLedgerByBorrowerId(id);
//	    }
	    @GetMapping("/borrower/{borrowerId}")
	    public ResponseEntity<List<LedgerCal>> getLedgerByBorrowerId(@PathVariable Long borrowerId) {
	        List<LedgerCal> ledgerEntries = this.service.getLedgerByBorrowerId(borrowerId);
	        if (ledgerEntries.isEmpty()) {
	            return ResponseEntity.noContent().build();
	        }
	        return ResponseEntity.ok(ledgerEntries);
	    }
	    
//	    @PostMapping("/calculate-ledger/{borrowerId}")
//	    public List<LedgerCal> calculateLedger(@PathVariable Long borrowerId) {
//	        return this.service..calculateLedger(borrowerId);
//	    }
//
//	    @GetMapping("/ledger/{borrowerId}")
//	    public List<LedgerCal> getLedgerByBorrowerId(@PathVariable Long borrowerId) {
//	        return borrowerService.getLedgerByBorrowerId(borrowerId);
//	    }
	    
//	    @PostMapping("/calculate/{borrowerId}")
//	    public BorrowerDoc calculateLedger(@PathVariable Long borrowerId) {
//	        return this.service.calculateAndUpdateLedger(borrowerId);
//	    }
	    
//	    @PostMapping("/api/ledger/updateInterestPaid")
//	    public ResponseEntity<?> updateInterestPaid(@RequestBody LedgerUpdateRequest request) {
//	        LedgerCal ledger = this.service.findById(request.getLedgerId())
//	                .orElseThrow(() -> new RuntimeException("Ledger not found"));
//
//	        ledger.setInterestPaid(request.getInterestPaid());
//	        this.service.save(ledger);
//
//	        calculateAndUpdateLedger(ledger.getBorrower().getId()); // Recalculate based on updated interest paid
//
//	        return ResponseEntity.ok().build();
//	    }
//	    
//	    @PostMapping("/calculateCurrentMonth")
//	    public ResponseEntity<?> calculateLedgerForCurrentMonth(@RequestParam Long borrowerId) {
//	    	this.service.calculateLedgerForCurrentMonth(borrowerId);
//	        return ResponseEntity.ok().build();
//	    }

//	    @PostMapping("/api/ledger/updateInterestPaid")
//	    public ResponseEntity<?> updateInterestPaid(@RequestBody LedgerUpdateRequest request) {
//	        LedgerCal ledger = this.service.findById(request.getLedgerId())
//	                .orElseThrow(() -> new RuntimeException("Ledger not found"));
//
//	        if (ledger.isLocked()) {
//	            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Ledger entry is locked and cannot be modified.");
//	        }
//
//	        this.service.updateInterestPaid(request);
//	       
//	        return ResponseEntity.ok().build();
//	    }
	    
	    
//	    @PostMapping("/updateInterestPaid")
//	    public ResponseEntity<?> updateInterestPaid(@RequestBody LedgerUpdateRequest request) {
//	        ledgerCalculationService.updateInterestPaid(request);
//	        return ResponseEntity.ok().build();
//	    }





// @PostMapping("/calculate/{borrowerId}")
//	    public BorrowerDoc calculateLedger(@PathVariable Long borrowerId) {
//	        return this.service.calculateAndUpdateLedger(borrowerId);
//	    }
		

// Endpoint to calculate and update the ledger for a specific borrower
@PostMapping("/calculate/{borrowerId}")
public ResponseEntity<BorrowerDoc> calculateLedger(@PathVariable Long borrowerId) {
    BorrowerDoc borrowerDoc = this.service.calculateAndUpdateLedger(borrowerId);
    return ResponseEntity.ok(borrowerDoc);
}

// Endpoint to update interest paid and recalculate the ledger
@PostMapping("/updateInterestPaid")
public ResponseEntity<?> updateInterestPaid(@RequestBody LedgerUpdateRequest request) {
    LedgerCal ledger = this.service.findLedgerById(request.getLedgerId())
            .orElseThrow(() -> new RuntimeException("Ledger not found"));

    ledger.setInterestPaid(request.getInterestPaid());
    this.service.saveLedger(ledger);

    // Recalculate ledger after updating interest paid
    this.service.calculateAndUpdateLedger(ledger.getBorrower().getId());

    return ResponseEntity.ok().build();
}

// Endpoint to retrieve ledger details for a specific borrower
@GetMapping("/details/{borrowerId}")
public ResponseEntity<List<LedgerCal>> getLedgerDetails(@PathVariable Long borrowerId) {
    List<LedgerCal> ledgerList = this.service.findLedgersByBorrowerId(borrowerId);
    return ResponseEntity.ok(ledgerList);
}

}
