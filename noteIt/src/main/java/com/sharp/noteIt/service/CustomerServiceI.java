package com.sharp.noteIt.service;

import java.util.List;
import java.util.Optional;

import com.sharp.noteIt.model.BorrowerDoc;
import com.sharp.noteIt.model.BorrowerRequest;
import com.sharp.noteIt.model.CustomerDoc;
import com.sharp.noteIt.model.CustomerRequest;

public interface CustomerServiceI {

	public CustomerDoc saveCustomer(CustomerRequest request);
	public CustomerDoc login(String phone, String password);
	//public CustomerDoc profile(CustomerRequest request);
	public CustomerDoc getCustomerProfile(String firstName);
	public  List<CustomerDoc> getAllCustomers();
	//public   List<BorrowerDoc> saveBorrower(BorrowerRequest brequest);
	//void sendSms(String to, String message);
//	 CustomerDoc addBorrowerToCustomer(Long customerId, BorrowerDoc borrowerDoc);
	public CustomerDoc addBorrowerToCustomer(Long customerId, BorrowerDoc borrowerDoc);
	Optional<CustomerDoc> findCustomerById(Long customerId);
    List<BorrowerRequest> getBorrowersForCustomer(Long customerId);
}
