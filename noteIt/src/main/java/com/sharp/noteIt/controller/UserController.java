package com.sharp.noteIt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sharp.noteIt.model.CustomerRequest;
import com.sharp.noteIt.service.CustomerServiceI;

@RestController
public class UserController {
	@Autowired
	CustomerServiceI service;
	
	@PostMapping
	public String saveCustomer(@RequestBody CustomerRequest request) {
		
		service.saveCustomer(request);
		return "Success";
		
	}
}
