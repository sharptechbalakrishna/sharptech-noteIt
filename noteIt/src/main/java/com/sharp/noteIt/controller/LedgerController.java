package com.sharp.noteIt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sharp.noteIt.model.BorrowerDoc;
import com.sharp.noteIt.model.BorrowerRequest;
import com.sharp.noteIt.model.CustomerDoc;
import com.sharp.noteIt.model.LedgerCal;
import com.sharp.noteIt.model.LedgerUpdateRequest;
import com.sharp.noteIt.service.LedgerService;


@RestController
public class LedgerController {
	
	@Autowired
	LedgerService ledgerService;
	
	@PostMapping("/{customerId}/borrowers")
    public ResponseEntity<?> addBorrowerToCustomer(@PathVariable Long customerId, @RequestBody BorrowerDoc borrowerDoc) {
        CustomerDoc customer = ledgerService.addBorrowerToCustomer(customerId, borrowerDoc);
        return ResponseEntity.ok(customer);
    }

    @PostMapping("/ledger/update")
    public ResponseEntity<?> updateInterestPaid(@RequestBody LedgerUpdateRequest request) {
    	ledgerService.updateInterestPaid(request);
        return ResponseEntity.ok("Interest paid updated successfully");
    }

    @GetMapping("/borrowers/{borrowerId}/ledger")
    public ResponseEntity<?> getAndUpdateLedger(@PathVariable Long borrowerId) {
        BorrowerDoc borrowerDoc = ledgerService.calculateAndUpdateLedger(borrowerId);
        return ResponseEntity.ok(borrowerDoc);
    }
    
    
    @GetMapping("/borrower/{borrowerId}")
    public BorrowerDoc getBorrowerById(@PathVariable Long borrowerId) {
        return ledgerService.getBorrowerById(borrowerId);
    }

    // Get all ledgers for a specific borrower
    @GetMapping("/ledger/{borrowerId}")
    public List<LedgerCal> getLedgerByBorrowerId(@PathVariable Long borrowerId) {
        return ledgerService.getLedgerByBorrowerId(borrowerId);
    }
    
    @GetMapping( "/{borrowerId}/{ledgerId}")
    public ResponseEntity<LedgerCal> getLedgerByBorrowerAndLedgerId(@PathVariable Long borrowerId, @PathVariable Long ledgerId) {
        LedgerCal ledger = ledgerService.getLedgerByBorrowerAndLedgerId(borrowerId, ledgerId);
        if (ledger != null) {
            return ResponseEntity.ok(ledger);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/{customerId}/borrowers")
    public List<BorrowerRequest> getBorrowersForCustomer(@PathVariable Long customerId) {
        return ledgerService.getBorrowersForCustomer(customerId);
    }
}
