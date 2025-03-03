package com.sharp.noteIt.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sharp.noteIt.model.BorrowerDoc;
import com.sharp.noteIt.model.CustomerDoc;
import com.sharp.noteIt.model.LedgerCal;

@Repository
public interface LedgerRepository extends JpaRepository<LedgerCal, Long>{
	
	/**
     * Delete all ledger entries associated with the given borrower.
     *
     * @param borrower the borrower whose ledger entries are to be deleted
     */
    void deleteAllByBorrower(BorrowerDoc borrower);
    
    /**
     * Find all ledger entries associated with the given borrower.
     *
     * @param borrower the borrower whose ledger entries are to be retrieved
     * @return a list of ledger entries for the borrower
     */
    //List<LedgerCal> findAllByBorrower(BorrowerDoc borrower);
    List<LedgerCal> findByBorrowerId(Long borrowerId);

	LedgerCal findByMonthAndBorrower(String string, BorrowerDoc borrower);
	
	
	 
	 List<LedgerCal> findByBorrower(BorrowerDoc borrower);
	    Optional<LedgerCal> findByBorrowerAndMonth(BorrowerDoc borrower, String month);
	    LedgerCal findByBorrowerAndId(BorrowerDoc borrower, Long ledgerId);
	   
	    //void deleteByBorrowerId(Long borrowerId);
	    void deleteById(Long id);
	    void deleteByBorrowerId(Long borrowerId);

	    Optional<LedgerCal> findByIdAndBorrowerId(Long id, Long borrowerId);
	    void deleteByBorrower(BorrowerDoc borrower);
}
