package com.sharp.noteIt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sharp.noteIt.model.CustomerDoc;
import com.sharp.noteIt.model.CustomerRequest;
import com.sharp.noteIt.repo.CustomerRepository;

@Service
public class CustomerServiceImpl implements CustomerServiceI {

	@Autowired
	CustomerRepository repo;

	@Override
	public CustomerDoc saveCustomer(CustomerRequest request) {
		return repo.save(convertPojoToEntity(request));

	}

	private CustomerDoc convertPojoToEntity(CustomerRequest request) {
		CustomerDoc doc = new CustomerDoc();
		doc.setId(request.getId());
		doc.setEmail(request.getEmail());
		doc.setName(request.getName());
		doc.setPhone(request.getPhone());
		doc.setPassword(request.getPassword());
		return doc;
	}

	@Override
	public CustomerDoc login(String phone, String password) {
		return repo.findByPhoneAndPassword(phone, password)
				.orElseThrow(() -> new IllegalArgumentException("Invalid phone or password"));
	}

	@Override
	public CustomerDoc getCustomerProfile(String name) {
		return repo.findByName(name).orElseThrow(() -> new IllegalArgumentException("Customer not found"));
	}

	@Override
	public List<CustomerDoc> getAllCustomers() {
		return repo.findAll();
	}
}
