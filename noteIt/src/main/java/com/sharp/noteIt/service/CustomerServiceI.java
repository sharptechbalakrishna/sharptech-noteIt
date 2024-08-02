package com.sharp.noteIt.service;

import java.util.List;
import java.util.Optional;

import com.sharp.noteIt.model.BorrowerDoc;
import com.sharp.noteIt.model.BorrowerRequest;
import com.sharp.noteIt.model.CustomerDoc;
import com.sharp.noteIt.model.CustomerRequest;
import com.sharp.noteIt.model.LedgerCal;
import com.sharp.noteIt.model.LedgerUpdateRequest;
import com.sharp.noteIt.model.SelfNotes;

public interface CustomerServiceI {

	public CustomerDoc saveCustomer(CustomerRequest request);
	public CustomerDoc login(String phone, String password);
	//public CustomerDoc profile(CustomerRequest request);
	public CustomerDoc getCustomerProfile(String firstName);
	public  List<CustomerDoc> getAllCustomers();
	//public   List<BorrowerDoc> saveBorrower(BorrowerRequest brequest);

//	 CustomerDoc addBorrowerToCustomer(Long customerId, BorrowerDoc borrowerDoc);
	public CustomerDoc addBorrowerToCustomer(Long customerId, BorrowerDoc borrowerDoc);
	Optional<CustomerDoc> findCustomerById(Long customerId);
    List<BorrowerRequest> getBorrowersForCustomer(Long customerId);
    SelfNotes save(SelfNotes note);
	List<SelfNotes> getAllNotes();
	// void sendSms(String to, String body);
	Optional<LedgerCal> findById(Long ledgerId);
	LedgerCal save(LedgerCal ledger);
	//BorrowerDoc calculateAndUpdateLedger(Long borrowerId);
	List<LedgerCal> getLedgerByBorrowerId(Long borrowerId);
	 void calculateLedgerForCurrentMonth(Long borrowerId);
	    void updateInterestPaid(LedgerUpdateRequest request);
	    
	    
	    
	    //fridat api
	    
	    BorrowerDoc calculateAndUpdateLedger(Long borrowerId);
	    
	    Optional<LedgerCal> findLedgerById(Long ledgerId);
	    
	    void saveLedger(LedgerCal ledger);
	    
	    List<LedgerCal> findLedgersByBorrowerId(Long borrowerId);
}
