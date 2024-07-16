package com.sharp.noteIt.service;

import com.sharp.noteIt.model.CustomerDoc;
import com.sharp.noteIt.model.CustomerRequest;

public interface CustomerServiceI {

	public CustomerDoc saveCustomer(CustomerRequest request);
	public CustomerDoc login(String phone, String password);
}
