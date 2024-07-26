package com.sharp.noteIt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sharp.noteIt.model.BorrowerDoc;
import com.sharp.noteIt.model.BorrowerRequest;
import com.sharp.noteIt.model.CustomerDoc;
import com.sharp.noteIt.model.CustomerRequest;
import com.sharp.noteIt.model.InterestRequest;
import com.sharp.noteIt.model.InterestResponse;
import com.sharp.noteIt.service.CustomerServiceI;

@RestController("/api/borrowers/")
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
	    
}

