package com.sharp.noteIt.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sharp.noteIt.model.BorrowerDoc;
import com.sharp.noteIt.model.BorrowerRequest;
import com.sharp.noteIt.model.CustomerDoc;
import com.sharp.noteIt.model.CustomerRequest;
import com.sharp.noteIt.model.LedgerCal;
import com.sharp.noteIt.model.LedgerUpdateRequest;
import com.sharp.noteIt.model.SelfNotes;
import com.sharp.noteIt.repo.BorrowerRepository;
import com.sharp.noteIt.repo.CustomerRepository;
import com.sharp.noteIt.repo.LedgerRepository;
import com.sharp.noteIt.repo.SelfNotesRepository;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import jakarta.transaction.Transactional;
import lombok.Value;

@Service
public class CustomerServiceImpl implements CustomerServiceI {

	@Autowired
	CustomerRepository repo;

	@Override
	public CustomerDoc saveCustomer(CustomerRequest request) {
		CustomerDoc convertPojoToEntity = convertPojoToEntity(request);
		return repo.save(convertPojoToEntity);

	}

	private CustomerDoc convertPojoToEntity(CustomerRequest request) {
		CustomerDoc doc = new CustomerDoc();
		doc.setId(request.getId());
		doc.setEmail(request.getEmail());
		doc.setFirstName(request.getFirstName());
		doc.setLastName(request.getLastName());
		doc.setUserName(request.getUserName());
		doc.setPhone(request.getPhone());
		doc.setPassword(request.getPassword());
		doc.setCreatedBy(request.getUserName());
		doc.setCreatedTs(new Date());
		doc.setUpdatedTs(new Date());
		doc.setImage(request.getImage());
		return doc;
	}
	
	

	@Override
	public CustomerDoc login(String phone, String password) {
		return repo.findByPhoneAndPassword(phone, password)
				.orElseThrow(() -> new IllegalArgumentException("Invalid phone or password"));
	}

	@Override
	public CustomerDoc getCustomerProfile(String firstName) {
		return repo.getCustomerByfirstName(firstName).orElseThrow(() -> new IllegalArgumentException("Customer not found"));
	}

	@Override
	public List<CustomerDoc> getAllCustomers() {
		return repo.findAll();
	}

	@Autowired
    private BorrowerRepository borrowerRepository;
    
    @Autowired
    private CustomerRepository customerRepository;

    

    @Override
    public CustomerDoc addBorrowerToCustomer(Long customerId, BorrowerDoc borrowerDoc) {
        Optional<CustomerDoc> customerOptional = customerRepository.findById(customerId);

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

        while (start.before(end) || start.equals(end)) {
            String month = new SimpleDateFormat("MMMM yyyy").format(start.getTime());
            double monthlyInterestAmount = (borrowerDoc.getPrincipalAmount() * (borrowerDoc.getInterestRate() / 100)) / 12;
            int daysInMonth = start.getActualMaximum(Calendar.DAY_OF_MONTH);

            LedgerCal ledger = new LedgerCal();
            ledger.setPrincipalAmount(borrowerDoc.getPrincipalAmount());
            ledger.setInterestAmount(monthlyInterestAmount);
            ledger.setMonth(month);
            ledger.setDays(start.get(Calendar.DAY_OF_MONTH));
          //  ledger.setCumulativeInterest(monthlyInterestAmount);
            ledger.setInterestPaid(0.0); // Initial interest paid is 0
            ledger.setStatus("DUE");
            ledger.setBorrower(borrowerDoc);
            ledger.setLocked(false);

            ledgerRepository.save(ledger);

            start.add(Calendar.MONTH, 1);
            start.set(Calendar.DAY_OF_MONTH, 1);
        }
    }

    
 
    public Optional<CustomerDoc> findCustomerById(Long customerId) {
        return customerRepository.findById(customerId);
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
                        return br;
                    })
                    .collect(Collectors.toList());
        } else {
            throw new RuntimeException("Customer not found");
        }
    }
//    
//    @Autowired
//    SelfNotesRepository selfNotesRepository;
//    public SelfNotes save(SelfNotes note) {
//    	SelfNotes notes = new  SelfNotes();
//    	//notes.setCreatedBy(CustomerDoc.getUserName());
//    	notes.setCreatedTs(new Date());
//    	notes.setCreatedTs(new Date());
//        return selfNotesRepository.save(note);
//    }
//    
//    @Override
//    public List<SelfNotes> getAllNotes() {
//        return selfNotesRepository.findAll();
//    }
    @Autowired
    private SelfNotesRepository selfNotesRepository;

    @Override
    public SelfNotes createOrUpdateSelfNote(Long customerId, SelfNotes selfNote) {
        Optional<CustomerDoc> customer = customerRepository.findById(customerId);
        if (!customer.isPresent()) {
            throw new RuntimeException("Customer not found with id " + customerId);
        }

        selfNote.setCustomer(customer.get());

        if (selfNote.getId() != null) {
            Optional<SelfNotes> existingNote = selfNotesRepository.findById(selfNote.getId());
            if (existingNote.isPresent()) {
                SelfNotes updatedNote = existingNote.get();
                updatedNote.setNotes(selfNote.getNotes());
                updatedNote.setTitle(selfNote.getTitle());
                updatedNote.setUpdatedTs(new Date());
                return selfNotesRepository.save(updatedNote);
            }
        }

        selfNote.setCreatedTs(new Date());
        selfNote.setUpdatedTs(new Date());
        return selfNotesRepository.save(selfNote);
    }
    @Override
    public void deleteSelfNoteById(Long customerId, Integer noteId) {
        System.out.println("Attempting to delete note with ID: " + noteId);
        if (selfNotesRepository.existsById(noteId)) {
            selfNotesRepository.deleteById(noteId);
            System.out.println("Note deleted successfully.");
        } else {
            System.out.println("Note with ID " + noteId + " not found.");
        }
    }

    @Override
    public List<SelfNotes> getNotesByCustomerId(Long customerId) {
        return selfNotesRepository.findByCustomerId(customerId);
    }

    @Override
    public SelfNotes getSelfNoteById(Long customerId, Integer noteId) {
        Optional<SelfNotes> selfNote = selfNotesRepository.findById(noteId);
        return selfNote.filter(note -> note.getCustomer().getId().equals(customerId)).orElse(null);
    }

	
	
//    @Value("${twilio.account.sid}")
//    private String accountSid;
//
//    @Value("${twilio.auth.token}")
//    private String authToken;
//
//    @Value("${twilio.phone.number}")
//    private String twilioPhoneNumber;
//
//    public SmsServiceImpl() {
//        Twilio.init(accountSid, authToken);
//    }
//
//    @Override
//    public void sendSms(String to, String body) {
//        Message.creator(
//                new PhoneNumber(to), // To number
//                new PhoneNumber(twilioPhoneNumber), // From number
//                body // Message body
//        ).create();
//    }
	
//	 @Override
//	    public void sendSms(String to, String message) {
//	        try {
//	            Message.creator(
//	                    new PhoneNumber(to),
//	                    new PhoneNumber(twilioPhoneNumber),
//	                    message
//	            ).create();
//	            logger.info("Sent SMS to {}", to);
//	        } catch (Exception e) {
//	            logger.error("Failed to send SMS to {}: {}", to, e);
//	        }
//	    }

   

    @Autowired
    private LedgerRepository ledgerRepository;
    
    
    @Override
    public Optional<LedgerCal> findById(Long ledgerId) {
        return ledgerRepository.findById(ledgerId);
    }

    @Override
    public LedgerCal save(LedgerCal ledger) {
        return ledgerRepository.save(ledger);
    }

  
//    @Override
//    @Transactional
//    public void calculateLedgerForCurrentMonth(Long borrowerId) {
//        BorrowerDoc borrower = borrowerRepository.findById(borrowerId)
//                .orElseThrow(() -> new RuntimeException("Borrower not found"));
//
//        Calendar borrowedDate = Calendar.getInstance();
//        borrowedDate.setTime(borrower.getBorrowedDate());
//        Calendar now = Calendar.getInstance();
//        Calendar monthEnd = Calendar.getInstance();
//        monthEnd.set(Calendar.DAY_OF_MONTH, monthEnd.getActualMaximum(Calendar.DAY_OF_MONTH));
//
//        if (borrowedDate.after(now)) {
//            throw new IllegalArgumentException("Borrowed date cannot be in the future");
//        }
//
//        double principalAmount = borrower.getPrincipalAmount();
//        double interestRate = borrower.getInterestRate();
//        double monthlyInterestAmount = (principalAmount * (interestRate / 100)) / 12;
//
//        // Calculate days from borrowed date to the end of the current month
//        long daysBetween = monthEnd.getTimeInMillis() - borrowedDate.getTimeInMillis();
//        long daysInMonth = monthEnd.getActualMaximum(Calendar.DAY_OF_MONTH);
//        double interestPerDay = monthlyInterestAmount / daysInMonth;
//        double cumulativeInterest = (interestPerDay * (borrowedDate.get(Calendar.DAY_OF_MONTH)));
//
//        LedgerCal existingLedger = ledgerRepository.findByMonthAndBorrower(
//                now.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) + " " + now.get(Calendar.YEAR),
//                borrower);
//
//        if (existingLedger == null) {
//            LedgerCal ledger = new LedgerCal();
//            ledger.setPrincipalAmount(principalAmount);
//            ledger.setInterestAmount(monthlyInterestAmount);
//            ledger.setMonth(now.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) + " " + now.get(Calendar.YEAR));
//            ledger.setDays(borrowedDate.get(Calendar.DAY_OF_MONTH));
//          //  ledger.setCumulativeInterest(cumulativeInterest);
//            ledger.setInterestPaid(0.0); // Initial value
//            ledger.setStatus("DUE");
//            ledger.setBorrower(borrower);
//
//            ledgerRepository.save(ledger);
//        }
//    }

   


//    @Override
//    public List<LedgerCal> getLedgerByBorrowerId(Long borrowerId) {
//        BorrowerDoc borrowerDoc = borrowerRepository.findById(borrowerId)
//                .orElseThrow(() -> new RuntimeException("Borrower not found"));
//
//        return ledgerRepository.findAllByBorrower(borrowerDoc);
//    }
    @Override
    public List<LedgerCal> getLedgerByBorrowerId(Long borrowerId) {
        return ledgerRepository.findByBorrowerId(borrowerId);
    }

	  @Override
    @Transactional
    public BorrowerDoc calculateAndUpdateLedger(Long borrowerId) {
        BorrowerDoc borrowerDoc = borrowerRepository.findById(borrowerId)
                .orElseThrow(() -> new RuntimeException("Borrower not found"));

        double principalAmount = borrowerDoc.getPrincipalAmount();
        double interestRate = borrowerDoc.getInterestRate();
        Date borrowedDate = borrowerDoc.getBorrowedDate();
        Date endDate = borrowerDoc.getEndDate();

        if (borrowedDate == null || endDate == null || borrowedDate.after(endDate)) {
            throw new IllegalArgumentException("Invalid borrowed or end date");
        }

        Calendar start = Calendar.getInstance();
        start.setTime(borrowedDate);

        Calendar end = Calendar.getInstance();
        end.setTime(endDate);

        ledgerRepository.deleteAllByBorrower(borrowerDoc);

        while (start.before(end) || start.equals(end)) {
            String month = new SimpleDateFormat("MMMM yyyy").format(start.getTime());
            int daysInMonth = start.getActualMaximum(Calendar.DAY_OF_MONTH);
            double monthlyInterestAmount = (principalAmount * (interestRate / 100)) / 12;
            double interestPerDay = monthlyInterestAmount / daysInMonth;

            // Calculate interest for days left in the current month
            int daysLeftInMonth = daysInMonth - start.get(Calendar.DAY_OF_MONTH) + 1;
            double interestForCurrentMonth = interestPerDay * daysLeftInMonth;

            // Calculate the cumulative interest
            double cumulativeInterest = interestForCurrentMonth;

            // Create a new LedgerCal object
            LedgerCal ledger = new LedgerCal();
            ledger.setPrincipalAmount(principalAmount);
            ledger.setInterestAmount(monthlyInterestAmount);
            ledger.setMonth(month);
            ledger.setDays(daysLeftInMonth);
           // ledger.setCumulativeInterest(cumulativeInterest);
            ledger.setBorrower(borrowerDoc); // Set the borrower

         // Save to repository
         ledgerRepository.save(ledger);

            // Set a placeholder for interest paid. This should be set based on user input.
            double interestPaid = 0; // Placeholder value; this should be updated by user input later

            ledger.setInterestPaid(interestPaid);
            ledger.setStatus(cumulativeInterest <= interestPaid ? "PAID" : "DUE");

            ledgerRepository.save(ledger);

            // Update principal amount based on interest paid
            if (interestPaid >= cumulativeInterest) {
                borrowerDoc.setPrincipalAmount(principalAmount - (interestPaid - cumulativeInterest));
            } else {
                borrowerDoc.setPrincipalAmount(principalAmount);
            }

            // Move to the next month
            start.add(Calendar.MONTH, 1);
            start.set(Calendar.DAY_OF_MONTH, 1);

            if (start.getTime().after(endDate)) {
                break;
            }
        }

        borrowerRepository.save(borrowerDoc);
        return borrowerDoc;
    }

	  
//	  private void calculateSubsequentMonths(Long borrowerId, Double principalAmount) {
//		    BorrowerDoc borrower = borrowerRepository.findById(borrowerId)
//		            .orElseThrow(() -> new RuntimeException("Borrower not found"));
//
//		    Calendar start = Calendar.getInstance();
//		    start.setTime(borrower.getBorrowedDate());
//
//		    // Move start to the beginning of the next month
//		    start.add(Calendar.MONTH, 1);
//		    start.set(Calendar.DAY_OF_MONTH, 1);
//
//		    Calendar now = Calendar.getInstance();
//
//		    while (start.before(now) || start.equals(now)) {
//		        String month = start.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) + " " + start.get(Calendar.YEAR);
//		        LedgerCal ledger = ledgerRepository.findByMonthAndBorrower(month, borrower);
//
//		        double interestRate = borrower.getInterestRate();
//		        double monthlyInterestAmount = (principalAmount * (interestRate / 100)) / 12;
//		        int daysInMonth = start.getActualMaximum(Calendar.DAY_OF_MONTH);
//		        double interestPerDay = monthlyInterestAmount / daysInMonth;
//
//		        if (ledger == null) {
//		            ledger = new LedgerCal();
//		            ledger.setPrincipalAmount(principalAmount);
//		            ledger.setInterestAmount(monthlyInterestAmount);
//		            ledger.setMonth(month);
//		            ledger.setDays(start.get(Calendar.DAY_OF_MONTH));
//		           // ledger.setCumulativeInterest(interestPerDay * start.get(Calendar.DAY_OF_MONTH));
//		            ledger.setInterestPaid(0.0); // Initial value
//		            ledger.setStatus("DUE");
//		            ledger.setBorrower(borrower);
//		        } else {
//		            ledger.setPrincipalAmount(principalAmount);
//		            ledger.setInterestAmount(monthlyInterestAmount);
//		            ledger.setDays(start.get(Calendar.DAY_OF_MONTH));
//		            //ledger.setCumulativeInterest(interestPerDay * start.get(Calendar.DAY_OF_MONTH));
//		            ledger.setStatus(ledger.getInterestPaid() >= monthlyInterestAmount ? "PAID" : "DUE");
//		        }
//
//		        ledgerRepository.save(ledger);
//
//		        start.add(Calendar.MONTH, 1);
//		        start.set(Calendar.DAY_OF_MONTH, 1);
//		    }
//		}

	  @Override
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
	      }

	      // Update ledger with new values
	      ledger.setPrincipalAmount(principalAmount);
	      ledger.setInterestPaid(interestPaid);

	      // Lock the current month's ledger
	      ledger.setLocked(true); 

	      // Calculate days for the current month and next month
	      updateDaysInLedger(ledger);

	      ledgerRepository.save(ledger);

	      if (principalAmount > 0) {
	          // Calculate the ledger for the next month based on the updated principal amount
	          calculateNextMonthLedger(ledger.getBorrower().getId(), principalAmount);
	      }
	  }


	  @Transactional
	  public void calculateNextMonthLedger(Long borrowerId, double updatedPrincipalAmount) {
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

	      double interestRate = borrower.getInterestRate(); // Adjust if needed

	      // Calculate interest for the next month
	      double monthlyInterestAmount = (updatedPrincipalAmount * (interestRate / 100)) / 12;

	      LedgerCal newLedger = new LedgerCal();
	      newLedger.setPrincipalAmount(updatedPrincipalAmount);
	      newLedger.setInterestAmount(monthlyInterestAmount);
	      newLedger.setMonth(month);
	      newLedger.setDays(start.getActualMaximum(Calendar.DAY_OF_MONTH));
	     // newLedger.setCumulativeInterest(monthlyInterestAmount);
	      newLedger.setInterestPaid(0.0); // Set to 0 initially
	      newLedger.setStatus("DUE"); // Set initial status
	      newLedger.setLocked(false); // Initially unlocked
	      newLedger.setBorrower(borrower);

	      ledgerRepository.save(newLedger);
	  }

	  private void updateDaysInLedger(LedgerCal ledger) {
		    Date borrowedDate = ledger.getBorrower().getBorrowedDate(); // Borrowed date
		    Calendar borrowedCal = Calendar.getInstance();
		    borrowedCal.setTime(borrowedDate);

		    // Calculate end of the current month
		    Calendar endOfMonthCal = Calendar.getInstance();
		    endOfMonthCal.set(Calendar.DAY_OF_MONTH, endOfMonthCal.getActualMaximum(Calendar.DAY_OF_MONTH));
		    endOfMonthCal.set(Calendar.HOUR_OF_DAY, 23);
		    endOfMonthCal.set(Calendar.MINUTE, 59);
		    endOfMonthCal.set(Calendar.SECOND, 59);
		    endOfMonthCal.set(Calendar.MILLISECOND, 999);

		    // Calculate days from the borrowed date to the end of the current month
		    long millisFromBorrowedDateToEndOfMonth = endOfMonthCal.getTimeInMillis() - borrowedCal.getTimeInMillis();
		    long daysFromBorrowedDateToEndOfMonth = millisFromBorrowedDateToEndOfMonth / (1000 * 60 * 60 * 24) + 1;

		    // Calculate days in the next month
		    Calendar nextMonthCal = Calendar.getInstance();
		    nextMonthCal.add(Calendar.MONTH, 1);
		    nextMonthCal.set(Calendar.DAY_OF_MONTH, 1);
		    nextMonthCal.set(Calendar.HOUR_OF_DAY, 0);
		    nextMonthCal.set(Calendar.MINUTE, 0);
		    nextMonthCal.set(Calendar.SECOND, 0);
		    nextMonthCal.set(Calendar.MILLISECOND, 0);
		    int daysInNextMonth = nextMonthCal.getActualMaximum(Calendar.DAY_OF_MONTH);

		    // Calculate days for the next month if applicable
		    Calendar nextMonthEndCal = Calendar.getInstance();
		    nextMonthEndCal.add(Calendar.MONTH, 2);
		    nextMonthEndCal.set(Calendar.DAY_OF_MONTH, 1);
		    nextMonthEndCal.add(Calendar.DAY_OF_MONTH, -1);
		    int daysFromStartOfNextMonth = nextMonthEndCal.get(Calendar.DAY_OF_MONTH);

		    // Update the ledger with the total days
		    ledger.setDays((int) (daysFromBorrowedDateToEndOfMonth + daysInNextMonth));
		}

	@Override
	public void calculateLedgerForCurrentMonth(Long borrowerId) {
		// TODO Auto-generated method stub
		
	}


    @Override
    public Optional<LedgerCal> findLedgerById(Long ledgerId) {
        return ledgerRepository.findById(ledgerId);
    }

    @Override
    public void saveLedger(LedgerCal ledger) {
        ledgerRepository.save(ledger);
    }

    @Override
    public List<LedgerCal> findLedgersByBorrowerId(Long borrowerId) {
        BorrowerDoc borrowerDoc = borrowerRepository.findById(borrowerId)
                .orElseThrow(() -> new RuntimeException("Borrower not found"));
        
        return ledgerRepository.findAllById((Iterable<Long>) borrowerDoc);
    }

	@Override
	public SelfNotes save(SelfNotes note) {
		// TODO Auto-generated method stub
		return null;
	}


//	@Override
//	public List<SelfNotes> getAllNotes() {
//		// TODO Auto-generated method stub
//		return null;
//	}

}
