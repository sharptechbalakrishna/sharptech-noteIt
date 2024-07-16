package com.sharp.noteIt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sharp.noteIt.model.CustomerDoc;
import com.sharp.noteIt.model.CustomerRequest;
import com.sharp.noteIt.service.CustomerServiceI;

@RestController
public class UserController {
	
	@Autowired
	CustomerServiceI service;
	
	@PostMapping("/register")
	public String saveCustomer(@RequestBody CustomerRequest request) {
		
		service.saveCustomer(request);
		return "Success";
		
	}
	
	 @GetMapping("/login")
	    public CustomerDoc login(@RequestParam String phone, @RequestParam String password) {
	        System.out.println("Attempting to login with phone: " + phone); // Print to console
	        return service.login(phone, password);
	    }
	
}
