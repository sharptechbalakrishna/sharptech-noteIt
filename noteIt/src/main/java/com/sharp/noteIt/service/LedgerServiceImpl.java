package com.sharp.noteIt.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

        int startDay = start.get(Calendar.DAY_OF_MONTH);  // Day of the month loan started
        int lastDayOfMonth = start.getActualMaximum(Calendar.DAY_OF_MONTH);  // Last day of the month
        int daysLeftInMonth = lastDayOfMonth - startDay + 1;  // Days left from start to the end of the month

        String month = new SimpleDateFormat("MMMM yyyy").format(start.getTime());
        BigDecimal principalAmount = BigDecimal.valueOf(borrowerDoc.getPrincipalAmount());
        BigDecimal interestRate = BigDecimal.valueOf(borrowerDoc.getInterestRate()).divide(BigDecimal.valueOf(100));

        // Interest for remaining days in the starting month
        BigDecimal dailyInterestRate = interestRate.divide(BigDecimal.valueOf(365), 6, RoundingMode.HALF_UP);
        BigDecimal interestAmount = principalAmount.multiply(dailyInterestRate).multiply(BigDecimal.valueOf(daysLeftInMonth)).setScale(2, RoundingMode.HALF_UP);

        LedgerCal ledger = new LedgerCal();
        ledger.setPrincipalAmount(principalAmount.setScale(2, RoundingMode.HALF_UP).doubleValue());
        ledger.setInterestAmount(interestAmount.doubleValue());
        ledger.setMonth(month);
        ledger.setDays(daysLeftInMonth);  // Store days left in the current month
        ledger.setInterestPaid(0.0);  // Initial interest paid is 0
        ledger.setStatus("DUE");
        ledger.setBorrower(borrowerDoc);
        ledger.setLocked(false);

        ledgerRepository.save(ledger);
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
                    // Create a new ledger if not exists
                    LedgerCal newLedger = new LedgerCal();
                    newLedger.setMonth(currentMonth);
                    newLedger.setBorrower(borrowerDoc);
                    newLedger.setLocked(false);
                    ledgerRepository.save(newLedger);
                    return newLedger;
                });

        BigDecimal principalAmount = BigDecimal.valueOf(borrowerDoc.getPrincipalAmount());
        BigDecimal interestRate = BigDecimal.valueOf(borrowerDoc.getInterestRate()).divide(BigDecimal.valueOf(100));
        BigDecimal monthlyInterestAmount = principalAmount.multiply(interestRate).divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);
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

        // Calculate ledger for the next month
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

        // Calculate next month's ledger
        if (principalAmount.compareTo(BigDecimal.ZERO) > 0) {
            calculateNextMonthLedger(ledger.getBorrower().getId(), principalAmount.doubleValue(), interestPaid.subtract(interestAmount).doubleValue(), ledger.getMonth());
        }

        // Create response
        LedgerUpdateResponse response = new LedgerUpdateResponse();
        response.setId(ledger.getId());
        response.setMonth(ledger.getMonth());
        response.setPrincipalAmount(ledger.getPrincipalAmount());
        response.setInterestAmount(ledger.getInterestAmount());
        response.setInterestPaid(ledger.getInterestPaid());
        response.setStatus(ledger.getStatus());
        response.setLocked(ledger.isLocked());

        return response;
    }



    @Transactional
    public void calculateNextMonthLedger(Long borrowerId, double principalAmount, double excessInterestPaid, String currentMonth) {
        BorrowerDoc borrower = borrowerRepository.findById(borrowerId)
                .orElseThrow(() -> new RuntimeException("Borrower not found"));

        // Calculate the next month
        Calendar nextMonthCal = Calendar.getInstance();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy");
            nextMonthCal.setTime(sdf.parse(currentMonth));
        } catch (Exception e) {
            throw new RuntimeException("Error parsing month string: " + currentMonth, e);
        }

        nextMonthCal.add(Calendar.MONTH, 1);  // Move to next month
        String nextMonth = new SimpleDateFormat("MMMM yyyy").format(nextMonthCal.getTime());
        int daysInNextMonth = nextMonthCal.getActualMaximum(Calendar.DAY_OF_MONTH);  // Days in the next month

        // Calculate interest amount for the next full month
        BigDecimal principalAmountBD = BigDecimal.valueOf(principalAmount);
        BigDecimal interestRate = BigDecimal.valueOf(borrower.getInterestRate()).divide(BigDecimal.valueOf(100));
        BigDecimal dailyInterestRate = interestRate.divide(BigDecimal.valueOf(365), 6, RoundingMode.HALF_UP);
        BigDecimal monthlyInterestAmount = principalAmountBD.multiply(dailyInterestRate).multiply(BigDecimal.valueOf(daysInNextMonth)).setScale(2, RoundingMode.HALF_UP);

        // Check if the ledger for the next month already exists
        LedgerCal nextMonthLedger = ledgerRepository.findByBorrowerAndMonth(borrower, nextMonth)
                .orElseGet(() -> {
                    LedgerCal newLedger = new LedgerCal();
                    newLedger.setMonth(nextMonth);
                    newLedger.setBorrower(borrower);
                    newLedger.setLocked(false);
                    ledgerRepository.save(newLedger);
                    return newLedger;
                });

        nextMonthLedger.setPrincipalAmount(principalAmountBD.setScale(2, RoundingMode.HALF_UP).doubleValue());
        nextMonthLedger.setInterestAmount(monthlyInterestAmount.doubleValue());
        nextMonthLedger.setDays(daysInNextMonth);  // Store total days in the next month
        nextMonthLedger.setInterestPaid(0.0);
        nextMonthLedger.setStatus("DUE");

        ledgerRepository.save(nextMonthLedger);
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
