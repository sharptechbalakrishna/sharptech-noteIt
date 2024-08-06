package com.sharp.noteIt.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sharp.noteIt.model.CustomerDoc;
import com.sharp.noteIt.model.ExpenseTracker;
import com.sharp.noteIt.model.ExpenseTrackerRequest;
import com.sharp.noteIt.model.UpdateExpenseRequest;
import com.sharp.noteIt.repo.CustomerRepository;
import com.sharp.noteIt.service.ExpenseTrackerService;

@RestController

public class ExpenseTrackerController {

    @Autowired
    private ExpenseTrackerService expenseTrackerService;

    @Autowired
    private CustomerRepository customerRepository;

    @PostMapping("/update-expense")
    public ResponseEntity<ExpenseTracker> updateExpense(@RequestBody ExpenseTrackerRequest request) {
        CustomerDoc customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        if (request.getIncome() != null) {
            // Add income
            ExpenseTracker expenseTracker = expenseTrackerService.addIncome(
                    request.getCustomerId(), request.getIncome(), request.getDescription(),request.getSpentAmount());
            return ResponseEntity.ok(expenseTracker);
        } else if (request.getSpentAmount() != null) {
            // Add spent amount
            ExpenseTracker expenseTracker = expenseTrackerService.addSpentAmount(
                    request.getCustomerId(), request.getSpentAmount(), request.getDescription());
            return ResponseEntity.ok(expenseTracker);
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }
    @GetMapping("/get-expense-tracker")
    public ResponseEntity<ExpenseTracker> getExpenseTrackerByCustomerId(@RequestParam Long customerId) {
        Optional<ExpenseTracker> expenseTracker = expenseTrackerService.getExpenseTrackerByCustomerId(customerId);

        if (expenseTracker.isPresent()) {
            return ResponseEntity.ok(expenseTracker.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
//    @PostMapping("/update-expense")
//    public ResponseEntity<ExpenseTracker> updateExpense(@RequestBody UpdateExpenseRequest request) {
//        // Call the service layer with the request data
//        ExpenseTracker updatedTracker = expenseTrackerService.updateExpense(
//            request.getCustomerId(),
//            request.getIncome(),
//            request.getSpentAmount(),
//            request.getDescription()
//        );
//        return ResponseEntity.ok(updatedTracker);
//    }
}

