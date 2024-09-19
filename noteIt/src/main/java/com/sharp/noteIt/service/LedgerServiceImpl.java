package com.sharp.noteIt.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
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
import com.sharp.noteIt.model.LedgerUpdateResponse;
import com.sharp.noteIt.repo.BorrowerRepository;
import com.sharp.noteIt.repo.CustomerRepository;
import com.sharp.noteIt.repo.LedgerRepository;

import jakarta.transaction.Transactional;

@Service
public class LedgerServiceImpl implements LedgerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private BorrowerRepository borrowerRepository;

    @Autowired
    private LedgerRepository ledgerRepository;

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

        int startDay = start.get(Calendar.DAY_OF_MONTH);
        int lastDayOfMonth = start.getActualMaximum(Calendar.DAY_OF_MONTH);
        int daysInMonth = lastDayOfMonth;

        int daysActiveInMonth = lastDayOfMonth - startDay + 1;

        String month = new SimpleDateFormat("MMMM yyyy").format(start.getTime());
        BigDecimal principalAmount = BigDecimal.valueOf(borrowerDoc.getPrincipalAmount());
        BigDecimal interestRate = BigDecimal.valueOf(borrowerDoc.getInterestRate());

        // Calculate interest for the active days in the month
        BigDecimal interestAmount = principalAmount
            .multiply(interestRate)
            .divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP)
            .multiply(BigDecimal.valueOf(daysActiveInMonth))
            .divide(BigDecimal.valueOf(daysInMonth), 2, RoundingMode.HALF_UP);

        // Create the first ledger entry
        LedgerCal ledger = new LedgerCal();
        ledger.setPrincipalAmount(principalAmount.setScale(2, RoundingMode.HALF_UP).doubleValue());
        ledger.setInterestAmount(interestAmount.doubleValue());
        ledger.setMonth(month);
        ledger.setDays(daysActiveInMonth);
        ledger.setInterestPaid(0.0);
        ledger.setStatus("DUE");
        ledger.setBorrower(borrowerDoc);
        ledger.setLocked(false);

        ledgerRepository.save(ledger);

        // Generate subsequent ledger entries
        LocalDate startDate = start.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusMonths(1);
        LocalDate endDate = LocalDate.now();
        calculateAndGenerateSubsequentLedger(borrowerDoc, startDate, endDate);
    }


    private void calculateAndGenerateSubsequentLedger(BorrowerDoc borrowerDoc, LocalDate startDate, LocalDate endDate) {
        Calendar start = Calendar.getInstance();
        start.set(startDate.getYear(), startDate.getMonthValue() - 1, 1);

        Calendar end = Calendar.getInstance();
        end.set(endDate.getYear(), endDate.getMonthValue() - 1, endDate.getDayOfMonth());

        BigDecimal principalAmount = BigDecimal.valueOf(borrowerDoc.getPrincipalAmount());
        BigDecimal interestRate = BigDecimal.valueOf(borrowerDoc.getInterestRate());

        while (start.before(end)) {
            int daysInMonth = start.getActualMaximum(Calendar.DAY_OF_MONTH);

            BigDecimal interestAmount = principalAmount
                .multiply(interestRate)
                .divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(daysInMonth))
                .divide(BigDecimal.valueOf(daysInMonth), 2, RoundingMode.HALF_UP);

            LedgerCal ledger = new LedgerCal();
            ledger.setPrincipalAmount(principalAmount.setScale(2, RoundingMode.HALF_UP).doubleValue());
            ledger.setInterestAmount(interestAmount.doubleValue());
            ledger.setMonth(new SimpleDateFormat("MMMM yyyy").format(start.getTime()));
            ledger.setDays(daysInMonth);
            ledger.setInterestPaid(0.0);
            ledger.setStatus("DUE");
            ledger.setBorrower(borrowerDoc);
            ledger.setLocked(false);

            ledgerRepository.save(ledger);

            start.add(Calendar.MONTH, 1);
        }
    }


    
    private void updateLedgerWithPayments(BorrowerDoc borrowerDoc, double paymentAmount) {
        List<LedgerCal> ledgers = ledgerRepository.findByBorrower(borrowerDoc);
        BigDecimal totalInterestPaid = BigDecimal.valueOf(paymentAmount);
        
        for (LedgerCal ledger : ledgers) {
            if (totalInterestPaid.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal interestDue = BigDecimal.valueOf(ledger.getInterestAmount());
                BigDecimal interestPaid = BigDecimal.valueOf(ledger.getInterestPaid());
                BigDecimal interestRemaining = interestDue.subtract(interestPaid);

                if (totalInterestPaid.compareTo(interestRemaining) >= 0) {
                    ledger.setInterestPaid(interestDue.doubleValue());
                    totalInterestPaid = totalInterestPaid.subtract(interestRemaining);

                    BigDecimal excessPayment = totalInterestPaid;
                    if (excessPayment.compareTo(BigDecimal.ZERO) > 0) {
                        BigDecimal principalAmount = BigDecimal.valueOf(ledger.getPrincipalAmount());
                        principalAmount = principalAmount.subtract(excessPayment);
                        ledger.setPrincipalAmount(principalAmount.doubleValue());
                    }
                } else {
                    ledger.setInterestPaid(interestPaid.add(totalInterestPaid).doubleValue());
                    totalInterestPaid = BigDecimal.ZERO;
                }
                ledger.setStatus(totalInterestPaid.compareTo(BigDecimal.ZERO) > 0 ? "DUE" : "PAID");
                ledgerRepository.save(ledger);
            }
        }
    }
    @Override
    @Transactional
    public BorrowerDoc calculateAndUpdateLedger(Long borrowerId) {
        BorrowerDoc borrowerDoc = borrowerRepository.findById(borrowerId)
                .orElseThrow(() -> new RuntimeException("Borrower not found"));

        Calendar start = Calendar.getInstance();
        start.setTime(borrowerDoc.getBorrowedDate());

        String currentMonth = new SimpleDateFormat("MMMM yyyy").format(start.getTime());

        LedgerCal currentMonthLedger = ledgerRepository.findByBorrowerAndMonth(borrowerDoc, currentMonth)
                .orElseGet(() -> {
                    LedgerCal newLedger = new LedgerCal();
                    newLedger.setMonth(currentMonth);
                    newLedger.setBorrower(borrowerDoc);
                    newLedger.setLocked(false);
                    ledgerRepository.save(newLedger);
                    return newLedger;
                });

        BigDecimal principalAmount = BigDecimal.valueOf(borrowerDoc.getPrincipalAmount());
        BigDecimal interestRate = BigDecimal.valueOf(borrowerDoc.getInterestRate()).divide(BigDecimal.valueOf(100));
        
        
        // Calculate number of days in the current month
        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.setTime(new Date());
        int daysInMonth = currentCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        // Calculate the number of days the loan has been active in the current month
        Calendar borrowedDate = Calendar.getInstance();
        borrowedDate.setTime(borrowerDoc.getBorrowedDate());
        int startDay = borrowedDate.get(Calendar.DAY_OF_MONTH);
        int daysActiveInMonth = daysInMonth - startDay + 1;

        // Calculate monthly interest amount based on the number of days
        BigDecimal dailyInterestRate = interestRate.divide(BigDecimal.valueOf(365), 8, RoundingMode.HALF_UP);
        //BigDecimal monthlyInterestAmount = principalAmount.multiply(dailyInterestRate).multiply(BigDecimal.valueOf(daysActiveInMonth));
        BigDecimal monthlyInterestAmount = principalAmount
                .multiply(interestRate)
                .divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(daysActiveInMonth))
                .divide(BigDecimal.valueOf(daysInMonth), 2, RoundingMode.HALF_UP);
        BigDecimal interestPaid = BigDecimal.valueOf(currentMonthLedger.getInterestPaid());
        BigDecimal remainingInterest = monthlyInterestAmount.subtract(interestPaid);
        BigDecimal excessInterestPaid = interestPaid.subtract(monthlyInterestAmount);

        if (excessInterestPaid.compareTo(BigDecimal.ZERO) > 0) {
            principalAmount = principalAmount.subtract(excessInterestPaid);
            currentMonthLedger.setStatus(principalAmount.compareTo(BigDecimal.ZERO) <= 0 ? "CLOSED" : "PAID");
        } else if (remainingInterest.compareTo(BigDecimal.ZERO) > 0) {
            currentMonthLedger.setStatus("DUE");
        }

        currentMonthLedger.setInterestPaid(interestPaid.setScale(2, RoundingMode.HALF_UP).doubleValue());
        currentMonthLedger.setPrincipalAmount(principalAmount.setScale(2, RoundingMode.HALF_UP).doubleValue());
        ledgerRepository.save(currentMonthLedger);

        calculateNextMonthLedger(borrowerId, principalAmount.doubleValue(), excessInterestPaid.doubleValue(), currentMonth);

        return borrowerDoc;
    }


    @Override
    @Transactional
    public LedgerUpdateResponse updateInterestPaid(LedgerUpdateRequest request) {
        LedgerCal ledger = ledgerRepository.findById(request.getLedgerId())
                .orElseThrow(() -> new RuntimeException("Ledger not found"));

        if (ledger.isLocked()) {
            throw new RuntimeException("Ledger entry is locked and cannot be modified.");
        }

        BigDecimal interestPaid = BigDecimal.valueOf(request.getInterestPaid());
        BigDecimal interestAmount = BigDecimal.valueOf(ledger.getInterestAmount());
        BigDecimal principalAmount = BigDecimal.valueOf(ledger.getPrincipalAmount());
        BigDecimal excessAmount = interestPaid.subtract(interestAmount);

        if (excessAmount.compareTo(BigDecimal.ZERO) > 0) {
            principalAmount = principalAmount.subtract(excessAmount);
            ledger.setStatus(principalAmount.compareTo(BigDecimal.ZERO) <= 0 ? "CLOSED" : "PAID");
        } else {
            ledger.setStatus("DUE");
            ledger.setInterestAmount(interestAmount.subtract(interestPaid).setScale(2, RoundingMode.HALF_UP).doubleValue());
        }

        ledger.setPrincipalAmount(principalAmount.setScale(2, RoundingMode.HALF_UP).doubleValue());
        ledger.setInterestPaid(interestPaid.setScale(2, RoundingMode.HALF_UP).doubleValue());
        ledger.setLocked(true);

        ledgerRepository.save(ledger);

        if (principalAmount.compareTo(BigDecimal.ZERO) > 0) {
            calculateNextMonthLedger(ledger.getBorrower().getId(), principalAmount.doubleValue(), interestPaid.subtract(interestAmount).doubleValue(), ledger.getMonth());
        }

        return new LedgerUpdateResponse(
        	    ledger.getId(),
        	    ledger.getMonth(),
        	    ledger.getPrincipalAmount(),
        	    ledger.getInterestAmount(),
        	    ledger.getInterestPaid(),
        	    ledger.getStatus(),
        	    ledger.isLocked() // Assuming `isLocked` is a method in your `Ledger` class
        	);
    }


    private void calculateNextMonthLedger(Long borrowerId, double updatedPrincipalAmount, double excessInterestPaid, String currentMonth) {
        BorrowerDoc borrowerDoc = borrowerRepository.findById(borrowerId)
                .orElseThrow(() -> new RuntimeException("Borrower not found"));

        Calendar nextMonth = Calendar.getInstance();
        nextMonth.add(Calendar.MONTH, 1);

        BigDecimal interestRate = BigDecimal.valueOf(borrowerDoc.getInterestRate()).divide(BigDecimal.valueOf(100));
        BigDecimal principalAmount = BigDecimal.valueOf(updatedPrincipalAmount);
        BigDecimal monthlyInterestAmount = principalAmount.multiply(interestRate).divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);

        LedgerCal nextLedger = new LedgerCal();
        nextLedger.setPrincipalAmount(principalAmount.setScale(2, RoundingMode.HALF_UP).doubleValue());
        nextLedger.setInterestAmount(monthlyInterestAmount.setScale(2, RoundingMode.HALF_UP).doubleValue());
        nextLedger.setInterestPaid(0.0);
        nextLedger.setMonth(new SimpleDateFormat("MMMM yyyy").format(nextMonth.getTime()));
        nextLedger.setBorrower(borrowerDoc);
        nextLedger.setLocked(false);
        nextLedger.setStatus("DUE");

        ledgerRepository.save(nextLedger);
    }

    
    private String getNextMonth(String currentMonth) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy");
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(currentMonth));

            // Move to the next month
            cal.add(Calendar.MONTH, 1);

            return sdf.format(cal.getTime());
        } catch (Exception e) {
            throw new RuntimeException("Error parsing month string: " + currentMonth, e);
        }
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

    @Override
    @Transactional
    public void deleteLedgerByBorrowerId(Long borrowerId, Long ledgerId) {
        LedgerCal ledger = ledgerRepository.findByIdAndBorrowerId(ledgerId, borrowerId)
                .orElseThrow(() -> new IllegalArgumentException("Ledger entry not found for the given borrower"));
        ledgerRepository.delete(ledger);
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
