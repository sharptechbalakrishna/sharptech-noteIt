package com.sharp.noteIt.controller;

import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import com.sharp.noteIt.repo.CustomerRepository;
import com.sharp.noteIt.security.CustomUserDetails;
//import com.sharp.noteIt.security.CustomerDetailsService;
//import com.sharp.noteIt.security.JwtService;
//import com.sharp.noteIt.security.JWTUtils;
//import com.sharp.noteIt.security.JWTUtils;
import com.sharp.noteIt.service.CustomerServiceI;
import com.sharp.noteIt.service.SmsService;
import com.twilio.exception.AuthenticationException;

@RestController
public class UserController {

	@Autowired
	CustomerServiceI service;

//	@PostMapping("/register")
//	public ResponseEntity<?> saveCustomer(@RequestBody CustomerRequest request) {
//
//		CustomerDoc saveCustomer = this.service.saveCustomer(request);
//		return new ResponseEntity<>(saveCustomer, HttpStatus.CREATED);
//	}
//
//	@GetMapping("/login")
//	public CustomerDoc login(@RequestParam String phone, @RequestParam String password) {
//		System.out.println("Attempting to login with phone: " + phone); // Print to console
//		return service.login(phone, password);
//	}
	//security commented by wednesday
//	 @Autowired
//	    private AuthenticationManager authenticationManager;
//
//	    @Autowired
//	    private CustomerRepository customerDocRepository;
//	    
//	    @Autowired
//	    private JwtService jwtService;
//	    
//	    
//	    @Autowired
//	    CustomerDetailsService customerDetailsService;
//	    @Autowired
//	    private JWTUtils jwtUtils;
//
//	    @PostMapping("/register")
//	    public ResponseEntity<String> register(@RequestBody CustomerDoc customer) {
//	        // Encrypt the password
//	        String encryptedPassword = new BCryptPasswordEncoder().encode(customer.getPassword());
//	        customer.setPassword(encryptedPassword);
//
//	        // Save the customer to the database
//	        customerDocRepository.save(customer);
//
//	        return ResponseEntity.ok("User registered successfully");
//	    }
//	    @PostMapping("/login")
//	    public ResponseEntity<String> login(@RequestParam String phone, @RequestParam String password) {
//	        try {
//	            org.springframework.security.core.Authentication authentication = authenticationManager.authenticate(
//	                    new UsernamePasswordAuthenticationToken(phone, password));
//
//	            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//	            String jwtToken = jwtUtils.generateToken(userDetails);
//
//	            return ResponseEntity.ok(jwtToken);
//	        } catch (AuthenticationException e) {
//	            // Log the exception
//	            System.err.println("Authentication failed: " + e.getMessage());
//	            return ResponseEntity.status(401).body("Invalid phone number or password");
//	        }
//	    }
//	    @Autowired
//	    private PasswordEncoder passwordEncoder;
//	    @PostMapping("/register")
//	    public CustomerDoc createCustomer(@RequestBody CustomerDoc customerDoc)
//	    {
//	    	customerDoc.setPassword(passwordEncoder.encode(customerDoc.getPassword()));
//	    	return customerDocRepository.save(customerDoc);
//	    }
//	    
//	    @PostMapping("/login")
//	    public String login(@RequestParam String phone, @RequestParam String password )
//	    {
//	    	Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
//	    			phone,password));
//	    	if(authentication.isAuthenticated()) {
//	    		return jwtService.generateToken(customerDetailsService.loadCustomerByPhone(phone));
//	    	}
//	    	else {
//	    		throw new UsernameNotFoundException("Invalid phone number and password");
//	    	}
//	    }

	@PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // Handle logout (e.g., invalidate JWT)
        return new ResponseEntity<>(HttpStatus.OK);
    }
	@GetMapping("/getAllCustomers")
    public List<CustomerDoc> getAllCustomers() {
        return service.getAllCustomers();
    }
	@GetMapping("/userDetails/{id}")
    public CustomerDoc getCustomerByName(@PathVariable Long id) {
        return service.getCustomerProfile(id);
    }
	@DeleteMapping("/{customerId}/borrowers/{borrowerId}")
	public ResponseEntity<String> deleteBorrower(@PathVariable Long customerId, @PathVariable Long borrowerId) {
	    service.deleteBorrowerById(customerId, borrowerId);
	    return ResponseEntity.ok("Borrower with id: " + borrowerId + " and associated ledger entries deleted for customer with id: " + customerId);
	}
//	@PostMapping("/calinterest")
//    public InterestResponse calculateInterest(@RequestBody InterestRequest request) {
//        double principal = request.getPrincipal();
//        double interestRate = request.getInterestRate();
//        double interest = (principal * interestRate) / 100;
//
//        return new InterestResponse(interest);
//    }

//	  @PostMapping("/{customerId}")
//	    public ResponseEntity<BorrowerDoc> saveBorrower(@PathVariable Long customerId, @RequestBody BorrowerDoc borrower) {
//	        return ResponseEntity.ok(service.saveBorrower(customerId, borrower));
//	    }
	
	
//	    @PostMapping("/{customerId}/borrowers")
//	    public CustomerDoc addBorrowerToCustomer(@PathVariable Long customerId, @RequestBody BorrowerDoc borrowerDoc)
//	    {	    
//	    	return this.service.addBorrowerToCustomer(customerId, borrowerDoc);
//	    }
	
//	    @PostMapping("/{customerId}/borrowers")
//	    public ResponseEntity<?> addBorrowerToCustomer(@PathVariable Long customerId, @RequestBody BorrowerDoc borrowerDoc) {
//	        // Add borrower to the customer
//	        BorrowerDoc addedBorrower = service.addBorrowerToCustomer(customerId, borrowerDoc);
//	        
//	        // Calculate ledger based on the added borrower
//	        service.calculateAndCreateLedgerForBorrower(addedBorrower);
//
//	        return ResponseEntity.ok(addedBorrower);
//	    }
	    
//	    @GetMapping("/{customerId}/borrowers")
//	    public List<BorrowerRequest> getBorrowersForCustomer(@PathVariable Long customerId) {
//	        return this.service.getBorrowersForCustomer(customerId);
//	    }
	    
	    //notes api's
	    @PostMapping("/{customerId}/selfnotes")
	    public ResponseEntity<SelfNotes> createOrUpdateSelfNote(
	            @PathVariable Long customerId,
	            @RequestBody SelfNotes selfNote) {
	        SelfNotes savedSelfNote = this.service.createOrUpdateSelfNote(customerId, selfNote);
	        return ResponseEntity.ok(savedSelfNote);
	    }
	    @DeleteMapping("/{customerId}/selfnotes/{noteId}")
	    public ResponseEntity<Void> deleteSelfNoteById(
	            @PathVariable Long customerId,
	            @PathVariable Integer noteId) {
	        try {
	            System.out.println("Deleting note with ID: " + noteId);
	            this.service.deleteSelfNoteById(customerId, noteId);
	            return ResponseEntity.noContent().build();
	        } catch (Exception e) {
	            e.printStackTrace();
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	        }
	    }


	    @GetMapping("/{customerId}/selfnotes")
	    public ResponseEntity<List<SelfNotes>> getNotesByCustomerId(@PathVariable Long customerId) {
	        List<SelfNotes> notes = this.service.getNotesByCustomerId(customerId);
	        return ResponseEntity.ok(notes);
	    }

	    @GetMapping("/{customerId}/selfnotes/{noteId}")
	    public ResponseEntity<SelfNotes> getSelfNoteById(
	            @PathVariable Long customerId,
	            @PathVariable Integer noteId) {
	        SelfNotes selfNote = this.service.getSelfNoteById(customerId, noteId);
	        if (selfNote != null) {
	            return ResponseEntity.ok(selfNote);
	        } else {
	            return ResponseEntity.notFound().build();
	        }
	    }
	    
//	    
//	    @DeleteMapping("/{customerId}")
//	    public ResponseEntity<Void> deleteCustomer(@PathVariable Long customerId) {
//	    	this.service.deleteCustomerById(customerId);
//	        return ResponseEntity.noContent().build(); // HTTP 204 No Content
//	    } 
	    
	    
	    
//	    
//	    @DeleteMapping("/{id}")
//	    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
//	    	this.service.deleteCustomerById(id);
//	        return ResponseEntity.noContent().build();
//	    }
	    
	    
	    
	    
	    @DeleteMapping("/{id}")
	    public ResponseEntity<String> deleteCustomer(@PathVariable Long id) {
	    	this.service.deleteCustomerById(id);
	        return ResponseEntity.ok("Customer and associated data deleted successfully");
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
	    
//	    @PutMapping("/{id}/calculateLedger")
//	    public BorrowerDoc calculateAndUpdateLedger(@PathVariable Long id) {
//	        return this.service.calculateAndUpdateLedger(id);
//	    }
//	    
//	    @GetMapping("/{id}/ledger")
//	    public List<LedgerCal> getLedgerByBorrowerId(@PathVariable Long id) {
//	        return this.service.getLedgerByBorrowerId(id);
//	    }
//	    @GetMapping("/borrower/{borrowerId}")
//	    public ResponseEntity<List<LedgerCal>> getLedgerByBorrowerId(@PathVariable Long borrowerId) {
//	        List<LedgerCal> ledgerEntries = this.service.getLedgerByBorrowerId(borrowerId);
//	        if (ledgerEntries.isEmpty()) {
//	            return ResponseEntity.noContent().build();
//	        }
//	        return ResponseEntity.ok(ledgerEntries);
//	    }
	    
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
		

//// Endpoint to calculate and update the ledger for a specific borrower
//@PostMapping("/calculate/{borrowerId}")
//public ResponseEntity<BorrowerDoc> calculateLedger(@PathVariable Long borrowerId) {
//    BorrowerDoc borrowerDoc = this.service.calculateAndUpdateLedger(borrowerId);
//    return ResponseEntity.ok(borrowerDoc);
//}

//// Endpoint to update interest paid and recalculate the ledger
//@PostMapping("/updateInterestPaid")
//public ResponseEntity<?> updateInterestPaid(@RequestBody LedgerUpdateRequest request) {
//    LedgerCal ledger = this.service.findLedgerById(request.getLedgerId())
//            .orElseThrow(() -> new RuntimeException("Ledger not found"));
//
//    ledger.setInterestPaid(request.getInterestPaid());
//    this.service.saveLedger(ledger);
//
//    // Recalculate ledger after updating interest paid
//    this.service.calculateAndUpdateLedger(ledger.getBorrower().getId());
//
//    return ResponseEntity.ok().build();
//}

// Endpoint to retrieve ledger details for a specific borrower
//@GetMapping("/details/{borrowerId}")
//public ResponseEntity<List<LedgerCal>> getLedgerDetails(@PathVariable Long borrowerId) {
//    List<LedgerCal> ledgerList = this.service.findLedgersByBorrowerId(borrowerId);
//    return ResponseEntity.ok(ledgerList);
//}

}
