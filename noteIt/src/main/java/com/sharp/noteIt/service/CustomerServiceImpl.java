package com.sharp.noteIt.service;

import org.springframework.stereotype.Service;

import com.sharp.noteIt.model.CustomerDoc;
import com.sharp.noteIt.model.CustomerRequest;
import com.sharp.noteIt.repo.CustomerRepository;

@Service
public class CustomerServiceImpl implements CustomerServiceI{

	CustomerRepository repo;
	@Override
	public CustomerDoc saveCustomer(CustomerRequest request) {
		// TODO Auto-generated method stub
		repo.save(convertPojoToEntity(request));
		return null;
	}
	private CustomerDoc convertPojoToEntity(CustomerRequest request) {
		CustomerDoc doc = new CustomerDoc();
		doc.setEmail(request.getEmail());
		doc.setName(request.getName());
		doc.setPhone(request.getPhone());
		return doc;
	}

}
