package com.sharp.noteIt.service;

import java.util.List;

import com.sharp.noteIt.model.BorrowerDoc;
import com.sharp.noteIt.model.BorrowerRequest;
import com.sharp.noteIt.model.CustomerDoc;
import com.sharp.noteIt.model.LedgerCal;
import com.sharp.noteIt.model.LedgerUpdateRequest;

public interface LedgerService {
	  CustomerDoc addBorrowerToCustomer(Long customerId, BorrowerDoc borrowerDoc);
	    BorrowerDoc calculateAndUpdateLedger(Long borrowerId);
	    void updateInterestPaid(LedgerUpdateRequest request);
	    BorrowerDoc getBorrowerById(Long borrowerId);
	    List<LedgerCal> getLedgerByBorrowerId(Long borrowerId);
	    LedgerCal getLedgerByBorrowerAndLedgerId(Long borrowerId, Long ledgerId);
		List<BorrowerRequest> getBorrowersForCustomer(Long customerId);
		 /**
	     * Delete a ledger entry by its ID and associated borrower ID.
	     * @param borrowerId The ID of the borrower.
	     * @param ledgerId The ID of the ledger entry to be deleted.
	     */
	    void deleteLedgerByBorrowerId(Long borrowerId, Long ledgerId);
}
