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

import jakarta.transaction.Transactional;

public interface CustomerServiceI {

	public CustomerDoc saveCustomer(CustomerRequest request);
	public CustomerDoc login(String phone, String password);
	//deleting borrower by customerid
	 void deleteBorrowerById(Long customerId, Long borrowerId);
	//public CustomerDoc profile(CustomerRequest request);
	public CustomerDoc getCustomerProfile(Long id);
	public  List<CustomerDoc> getAllCustomers();
	//public   List<BorrowerDoc> saveBorrower(BorrowerRequest brequest);

//	 CustomerDoc addBorrowerToCustomer(Long customerId, BorrowerDoc borrowerDoc);
	public CustomerDoc addBorrowerToCustomer(Long customerId, BorrowerDoc borrowerDoc);
	Optional<CustomerDoc> findCustomerById(Long customerId);
    List<BorrowerRequest> getBorrowersForCustomer(Long customerId);
    SelfNotes save(SelfNotes note);
	//List<SelfNotes> getAllNotes();
	// void sendSms(String to, String body);
	Optional<LedgerCal> findById(Long ledgerId);
	LedgerCal save(LedgerCal ledger);
	//BorrowerDoc calculateAndUpdateLedger(Long borrowerId);
	List<LedgerCal> getLedgerByBorrowerId(Long borrowerId);
	 void calculateLedgerForCurrentMonth(Long borrowerId);
	    void updateInterestPaid(LedgerUpdateRequest request);
	    
	    CustomerDoc getCustomerById(Long customerId);
	    
	    //fridat api
	    
	    BorrowerDoc calculateAndUpdateLedger(Long borrowerId);
	    
	    Optional<LedgerCal> findLedgerById(Long ledgerId);
	    
	    void saveLedger(LedgerCal ledger);
	    
	    List<LedgerCal> findLedgersByBorrowerId(Long borrowerId);
	    
	    //self notes
	    SelfNotes createOrUpdateSelfNote(Long customerId, SelfNotes selfNote);

	    void deleteSelfNoteById(Long customerId, Integer noteId);

	    List<SelfNotes> getNotesByCustomerId(Long customerId);

	    SelfNotes getSelfNoteById(Long customerId, Integer noteId);
	    
	    //deleting the customer data
	    //void deleteCustomerById(Long customerId);
	    //void deleteCustomerById(Long id);
	    @Transactional
	    void deleteCustomerById(Long customerId);
}
