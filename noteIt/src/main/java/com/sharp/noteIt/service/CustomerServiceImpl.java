package com.sharp.noteIt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sharp.noteIt.model.CustomerDoc;
import com.sharp.noteIt.model.CustomerRequest;
import com.sharp.noteIt.repo.CustomerRepository;

@Service
public class CustomerServiceImpl implements CustomerServiceI{
	
	@Autowired
	CustomerRepository repo;
	
	@Override
	public CustomerDoc saveCustomer(CustomerRequest request) {
		// TODO Auto-generated method stub
		return repo.save(convertPojoToEntity(request));
		
	}
	private CustomerDoc convertPojoToEntity(CustomerRequest request) {
		CustomerDoc doc = new CustomerDoc();
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

}
