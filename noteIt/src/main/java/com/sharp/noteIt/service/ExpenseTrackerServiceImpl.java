package com.sharp.noteIt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sharp.noteIt.model.CustomerDoc;
import com.sharp.noteIt.model.ExpenseTracker;
import com.sharp.noteIt.model.UpdateExpenseRequest;
import com.sharp.noteIt.repo.CustomerRepository;
import com.sharp.noteIt.repo.ExpenseTrackerRepository;

import jakarta.transaction.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ExpenseTrackerServiceImpl implements ExpenseTrackerService {
	@Autowired
    private ExpenseTrackerRepository expenseTrackerRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public Optional<ExpenseTracker> findByCustomer(CustomerDoc customer) {
        return expenseTrackerRepository.findByCustomer(customer);
    }

    public ExpenseTracker addIncome(Long customerId, Double income, String description,Double spentAmount) {
        CustomerDoc customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        ExpenseTracker tracker = expenseTrackerRepository.findByCustomer(customer)
                .orElseGet(() -> {
                    ExpenseTracker newTracker = new ExpenseTracker();
                    newTracker.setCustomer(customer);
                    return newTracker;
                });

        if (tracker.getIncome() == null) {
            tracker.setIncome(0.0);
        }
        if (tracker.getSpentAmount() == null) {
            tracker.setSpentAmount(0.0);
        }

        tracker.setIncome(tracker.getIncome() + income);
        tracker.setDescription(description);
        tracker.setSpentAmount(spentAmount);
        tracker.setTotal(tracker.getIncome() - tracker.getSpentAmount());
        tracker.setSavings(tracker.getTotal());
        tracker.setCreatedBy(customer.getUserName());
        tracker.setCreatedTs(new Date());
        tracker.setUpdatedTs(new Date());

        return expenseTrackerRepository.save(tracker);
    }

    public ExpenseTracker addSpentAmount(Long customerId, Double spentAmount, String description) {
        // Fetch the customer or throw an exception if not found
        CustomerDoc customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // Find existing tracker or create a new one
        ExpenseTracker tracker = expenseTrackerRepository.findByCustomer(customer)
                .orElseGet(() -> {
                    ExpenseTracker newTracker = new ExpenseTracker();
                    newTracker.setCustomer(customer);
                    newTracker.setIncome(0.0); // Initialize income if needed
                    newTracker.setSpentAmount(0.0); // Initialize spentAmount if needed
                    return newTracker;
                });

        // Print current values for debugging
        System.out.println("Current spentAmount: " + tracker.getSpentAmount());
        System.out.println("Amount to add: " + spentAmount);

        // Ensure spentAmount is not null and update it
        if (spentAmount != null) {
            tracker.setSpentAmount(tracker.getSpentAmount() + spentAmount);
        } else {
            System.out.println("Spent amount provided is null.");
        }

        // Update other fields
        tracker.setDescription(description);
        tracker.setTotal(tracker.getIncome() - tracker.getSpentAmount());
        tracker.setSavings(tracker.getTotal());
        tracker.setSpentAmount(spentAmount);

        // Print updated values for debugging
        System.out.println("Updated spentAmount: " + tracker.getSpentAmount());
        System.out.println("Updated total: " + tracker.getTotal());

        // Save the updated tracker to the repository
        ExpenseTracker updatedTracker = expenseTrackerRepository.save(tracker);

        // Print the saved tracker for confirmation
        System.out.println("Saved tracker: " + updatedTracker);

        return updatedTracker;
    }

    public ExpenseTracker updateExpense(Long customerId, Double income, Double spentAmount, String description) {
        CustomerDoc customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        ExpenseTracker tracker = expenseTrackerRepository.findByCustomer(customer)
                .orElseGet(() -> {
                    ExpenseTracker newTracker = new ExpenseTracker();
                    newTracker.setCustomer(customer);
                    return newTracker;
                });

        if (income != null) {
            if (tracker.getIncome() == null) {
                tracker.setIncome(0.0);
            }
            tracker.setIncome(tracker.getIncome() + income);
        }

        if (spentAmount != null) {
            if (tracker.getSpentAmount() == null) {
                tracker.setSpentAmount(0.0);
            }
            tracker.setSpentAmount(tracker.getSpentAmount() + spentAmount);
        }

        tracker.setDescription(description);
        tracker.setTotal(tracker.getIncome() - tracker.getSpentAmount());
        tracker.setSavings(tracker.getTotal());
        tracker.setSpentAmount(spentAmount);

        return expenseTrackerRepository.save(tracker);
    }
    
    @Override
    public Optional<ExpenseTracker> getExpenseTrackerByCustomerId(Long customerId) {
        CustomerDoc customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        
        return expenseTrackerRepository.findByCustomer(customer);
    }

//    @Override
//    public ExpenseTracker updateExpense(UpdateExpenseRequest request) {
//        // Fetch the customer from the repository
//        CustomerDoc customer = customerRepository.findById(request.getCustomerId())
//                .orElseThrow(() -> new RuntimeException("Customer not found"));
//
//        // Find existing tracker or create a new one
//        ExpenseTracker tracker = expenseTrackerRepository.findByCustomer(customer)
//                .orElseGet(() -> {
//                    ExpenseTracker newTracker = new ExpenseTracker();
//                    newTracker.setCustomer(customer);
//                    newTracker.setIncome(0.0); // Initialize if needed
//                    newTracker.setSpentAmount(0.0); // Initialize if needed
//                    return newTracker;
//                });
//
//        // Update the tracker based on request values
//        if (request.getIncome() != null) {
//            tracker.setIncome(tracker.getIncome() + request.getIncome());
//        }
//
//        if (request.getSpentAmount() != null) {
//            tracker.setSpentAmount(tracker.getSpentAmount() + request.getSpentAmount());
//        }
//
//        tracker.setDescription(request.getDescription());
//
//        // Recalculate total and savings
//        tracker.setTotal(tracker.getIncome() - tracker.getSpentAmount());
//        tracker.setSavings(tracker.getTotal());
//
//        // Save the updated tracker
//        return expenseTrackerRepository.save(tracker);
//    }


}

