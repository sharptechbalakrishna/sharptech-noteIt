package com.sharp.noteIt.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sharp.noteIt.model.BorrowerDoc;
import com.sharp.noteIt.model.BorrowerRequest;
import com.sharp.noteIt.model.CustomerDoc;
import com.sharp.noteIt.model.LedgerCal;
import com.sharp.noteIt.model.LedgerUpdateRequest;
import com.sharp.noteIt.repo.BorrowerRepository;
import com.sharp.noteIt.repo.CustomerRepository;
import com.sharp.noteIt.repo.LedgerRepository;

import jakarta.transaction.Transactional;
@Service
public class LedgerServiceImpl implements LedgerService{
	
	 @Autowired
	    private CustomerRepository customerRepository;

	    @Autowired
	    private BorrowerRepository borrowerRepository;

	    @Autowired
	    private LedgerRepository ledgerRepository;
	    
	
	    public CustomerDoc addBorrowerToCustomer(Long customerId, BorrowerDoc borrowerDoc) {
	        Optional<CustomerDoc> customerOptional = customerRepository.findById(customerId);
	        //String message = "Dear " + borrowerDoc.getBorrowerName() + ", you have been added as a borrower.";
	        //messageService.sendSms(borrowerDoc.getPhoneNumber(), message);
	        if (customerOptional.isPresent()) {
	            CustomerDoc customer = customerOptional.get();
	            borrowerDoc.setCustomerDoc(customer);
	            borrowerRepository.save(borrowerDoc);
	            customer.getBorrowers().add(borrowerDoc);
	            customerRepository.save(customer);

	            // Calculate and generate initial ledger entries for the newly added borrower
	            calculateAndGenerateInitialLedger(borrowerDoc);

	            return customer;
	        } else {
	            throw new RuntimeException("Customer not found");
	        }
	     
	    }

	    private void calculateAndGenerateInitialLedger(BorrowerDoc borrowerDoc) {
	        Calendar start = Calendar.getInstance();
	        start.setTime(borrowerDoc.getBorrowedDate());

	        Calendar end = Calendar.getInstance();
	        end.setTime(borrowerDoc.getEndDate());

	        // Calculate only for the current month
	        String month = new SimpleDateFormat("MMMM yyyy").format(start.getTime());
	        double monthlyInterestAmount = (borrowerDoc.getPrincipalAmount() * (borrowerDoc.getInterestRate() / 100)) / 12;
	        int daysInMonth = start.getActualMaximum(Calendar.DAY_OF_MONTH);

	        LedgerCal ledger = new LedgerCal();
	        ledger.setPrincipalAmount(borrowerDoc.getPrincipalAmount());
	        ledger.setInterestAmount(monthlyInterestAmount);
	        ledger.setMonth(month);
	        ledger.setDays(start.get(Calendar.DAY_OF_MONTH));
	        ledger.setInterestPaid(0.0); // Initial interest paid is 0
	        ledger.setStatus("DUE");
	        ledger.setBorrower(borrowerDoc);
	        ledger.setLocked(false);

	        ledgerRepository.save(ledger);
	    }

	  
	    @Transactional
	    public BorrowerDoc calculateAndUpdateLedger(Long borrowerId) {
	        BorrowerDoc borrowerDoc = borrowerRepository.findById(borrowerId)
	                .orElseThrow(() -> new RuntimeException("Borrower not found"));

	        Calendar start = Calendar.getInstance();
	        start.setTime(borrowerDoc.getBorrowedDate());

	        // Fetch the ledger for the current month
	        LedgerCal currentMonthLedger = ledgerRepository.findByBorrowerAndMonth(borrowerDoc, 
	            new SimpleDateFormat("MMMM yyyy").format(start.getTime()))
	                .orElseThrow(() -> new RuntimeException("Current month's ledger not found"));

	        double principalAmount = borrowerDoc.getPrincipalAmount();
	        double interestRate = borrowerDoc.getInterestRate();
	        Date endDate = borrowerDoc.getEndDate();

	        if (endDate == null || start.getTime().after(endDate)) {
	            throw new IllegalArgumentException("Invalid end date");
	        }

	        int daysInMonth = start.getActualMaximum(Calendar.DAY_OF_MONTH);
	        double monthlyInterestAmount = (principalAmount * (interestRate / 100)) / 12;
	        double interestPerDay = monthlyInterestAmount / daysInMonth;

	        // Calculate interest for days left in the current month
	        int daysLeftInMonth = daysInMonth - start.get(Calendar.DAY_OF_MONTH) + 1;
	        double interestForCurrentMonth = interestPerDay * daysLeftInMonth;

	        double interestPaid = currentMonthLedger.getInterestPaid(); // Placeholder value

	        // Update the ledger based on the interest paid
	        double remainingInterest = interestForCurrentMonth - interestPaid; // Calculate remaining interest
	        double excessAmount = interestPaid - interestForCurrentMonth;

	        if (excessAmount > 0) {
	            principalAmount -= excessAmount;
	            currentMonthLedger.setStatus(principalAmount <= 0 ? "CLOSED" : "PAID");
	        } else {
	            currentMonthLedger.setStatus("DUE");
	        }

	        // Update ledger with new values
	        currentMonthLedger.setPrincipalAmount(principalAmount);
	        currentMonthLedger.setInterestPaid(interestPaid);
	        ledgerRepository.save(currentMonthLedger);

	        // If principal amount is still remaining, calculate the ledger for the next month
	        if (principalAmount > 0) {
	            calculateNextMonthLedger(borrowerId, principalAmount, remainingInterest);
	        }

	        borrowerDoc.setPrincipalAmount(principalAmount);
	        borrowerRepository.save(borrowerDoc);

	        return borrowerDoc;
	    }

	  
	    @Transactional
	    public void updateInterestPaid(LedgerUpdateRequest request) {
	        LedgerCal ledger = ledgerRepository.findById(request.getLedgerId())
	                .orElseThrow(() -> new RuntimeException("Ledger not found"));

	        if (ledger.isLocked()) {
	            throw new RuntimeException("Ledger entry is locked and cannot be modified.");
	        }

	        double interestPaid = request.getInterestPaid();
	        double interestAmount = ledger.getInterestAmount();
	        double principalAmount = ledger.getPrincipalAmount();

	        // Calculate excess amount
	        double excessAmount = interestPaid - interestAmount;

	        if (excessAmount > 0) {
	            // Reduce principal amount by excess amount
	            principalAmount -= excessAmount;
	            ledger.setStatus(principalAmount <= 0 ? "CLOSED" : "PAID");
	        } else {
	            ledger.setStatus("DUE");
	            // Calculate remaining interest for the next month
	            ledger.setInterestAmount(interestAmount - interestPaid); // Adjust for remaining amount
	        }

	        // Update ledger with new values
	        ledger.setPrincipalAmount(principalAmount);
	        ledger.setInterestPaid(interestPaid);

	        // Lock the current month's ledger
	        ledger.setLocked(true);

	        // Save the updated ledger
	        ledgerRepository.save(ledger);

	        if (principalAmount > 0) {
	            // Calculate the ledger for the next month based on the updated principal amount and remaining interest
	            calculateNextMonthLedger(ledger.getBorrower().getId(), principalAmount, ledger.getInterestAmount() - interestPaid);
	        }
	    }

	    
	    @Transactional
	    public void calculateNextMonthLedger(Long borrowerId, double updatedPrincipalAmount, double remainingInterest) {
	        BorrowerDoc borrower = borrowerRepository.findById(borrowerId)
	                .orElseThrow(() -> new RuntimeException("Borrower not found"));

	        Calendar start = Calendar.getInstance();
	        int currentMonth = start.get(Calendar.MONTH);
	        int currentYear = start.get(Calendar.YEAR);
	        start.set(Calendar.DAY_OF_MONTH, 1); // Start from the beginning of the month
	        start.add(Calendar.MONTH, 1); // Move to the next month

	        int nextMonth = start.get(Calendar.MONTH);
	        int nextYear = start.get(Calendar.YEAR);
	        String month = new SimpleDateFormat("MMMM yyyy").format(start.getTime());

	        double interestRate = borrower.getInterestRate();

	        // Calculate interest for the next month
	        double monthlyInterestAmount = (updatedPrincipalAmount * (interestRate / 100)) / 12;
	        double totalInterestAmount = monthlyInterestAmount + remainingInterest; // Include remaining interest

	        LedgerCal newLedger = new LedgerCal();
	        newLedger.setPrincipalAmount(updatedPrincipalAmount);
	        newLedger.setInterestAmount(totalInterestAmount);
	        newLedger.setMonth(month);
	        newLedger.setDays(start.getActualMaximum(Calendar.DAY_OF_MONTH));
	        newLedger.setInterestPaid(0.0); // Set to 0 initially
	        newLedger.setStatus("DUE"); // Set initial status
	        newLedger.setLocked(false); // Initially unlocked
	        newLedger.setBorrower(borrower);

	        ledgerRepository.save(newLedger);
	    }

	    
	    
	    @Override
	    public BorrowerDoc getBorrowerById(Long borrowerId) {
	        return borrowerRepository.findById(borrowerId)
	                .orElseThrow(() -> new RuntimeException("Borrower not found"));
	    }

	    @Override
	    public List<LedgerCal> getLedgerByBorrowerId(Long borrowerId) {
	        BorrowerDoc borrower = borrowerRepository.findById(borrowerId)
	                .orElseThrow(() -> new RuntimeException("Borrower not found"));
	        return ledgerRepository.findByBorrower(borrower);
	    }
	    
	    @Override
	    public LedgerCal getLedgerByBorrowerAndLedgerId(Long borrowerId, Long ledgerId) {
	        Optional<BorrowerDoc> borrowerOpt = borrowerRepository.findById(borrowerId);
	        if (borrowerOpt.isPresent()) {
	            BorrowerDoc borrower = borrowerOpt.get();
	            return ledgerRepository.findByBorrowerAndId(borrower, ledgerId);
	        } else {
	            throw new RuntimeException("Borrower not found");
	        }
	    }

	    public List<BorrowerRequest> getBorrowersForCustomer(Long customerId) {
	        Optional<CustomerDoc> customerOptional = customerRepository.findById(customerId);

	        if (customerOptional.isPresent()) {
	            CustomerDoc customer = customerOptional.get();
	            return customer.getBorrowers().stream()
	                    .map(borrower -> {
	                        BorrowerRequest br = new BorrowerRequest();
	                        br.setBorrowerName(borrower.getBorrowerName());
	                        br.setPrincipalAmount(borrower.getPrincipalAmount());
	                        br.setInterestRate(borrower.getInterestRate());
	                        br.setStatus(borrower.getStatus());
	                        br.setId(borrower.getId());
	                        br.setBorrowedDate(borrower.getBorrowedDate());
	                        br.setCreditBasis(borrower.getCreditBasis());
	                        br.setCreditStatus(borrower.getCreditStatus());
	                        br.setEmail(borrower.getEmail());
	                        br.setEndDate(borrower.getEndDate());
	                       br.setPhoneNumber(borrower.getPhoneNumber());
	                       br.setTimePeriodNumber(borrower.getTimePeriodNumber());
	                       br.setTimePeriodUnit(borrower.getTimePeriodUnit());
	                        return br;
	                    })
	                    .collect(Collectors.toList());
	        } else {
	            throw new RuntimeException("Customer not found");
	        }
	    }		
		

}
