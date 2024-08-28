package com.sharp.noteIt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sharp.noteIt.model.CustomerDoc;
import com.sharp.noteIt.model.ExpenseTracker;
import com.sharp.noteIt.model.ExpenseTransaction;
import com.sharp.noteIt.repo.CustomerRepository;
import com.sharp.noteIt.repo.ExpenseTrackerRepository;
import com.sharp.noteIt.repo.ExpenseTransactionRepository;

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
    
    @Autowired
    private ExpenseTransactionRepository expenseTransactionRepository;
    @Override
    public Optional<ExpenseTracker> findByCustomer(CustomerDoc customer) {
        return expenseTrackerRepository.findByCustomer(customer);
    }

    public ExpenseTracker addIncome(Long customerId, Double income, String description, Double spentAmount) {
        CustomerDoc customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        ExpenseTracker tracker = expenseTrackerRepository.findByCustomer(customer)
                .orElseGet(() -> {
                    ExpenseTracker newTracker = new ExpenseTracker();
                    newTracker.setCustomer(customer);
                    newTracker.setIncome(0.0);
                    newTracker.setSpentAmount(0.0);
                    return newTracker;
                });

        tracker.setIncome(tracker.getIncome() + (income != null ? income : 0.0));
        tracker.setDescription(description);
        tracker.setSpentAmount(tracker.getSpentAmount() + (spentAmount != null ? spentAmount : 0.0));
        tracker.setTotal(tracker.getIncome() - tracker.getSpentAmount());
        tracker.setSavings(tracker.getTotal());
        tracker.setCreatedBy(customer.getUserName());
        tracker.setCreatedTs(new Date());
        tracker.setUpdatedTs(new Date());

        // Add transaction
        ExpenseTransaction transaction = new ExpenseTransaction();
        transaction.setDescription(description);
        transaction.setIncome(income);
        transaction.setSpentAmount(spentAmount);     
        transaction.setCreatedTs(tracker.getCreatedTs());
        transaction.setUpdatedTs(tracker.getUpdatedTs());
        

        tracker.addTransaction(transaction);

        return expenseTrackerRepository.save(tracker);
    }

    public ExpenseTracker addSpentAmount(Long customerId, Double spentAmount, String description) {
        CustomerDoc customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        ExpenseTracker tracker = expenseTrackerRepository.findByCustomer(customer)
                .orElseGet(() -> {
                    ExpenseTracker newTracker = new ExpenseTracker();
                    newTracker.setCustomer(customer);
                    newTracker.setIncome(0.0);
                    newTracker.setSpentAmount(0.0);
                    newTracker.setCreatedTs(new Date()); // Initialize creation timestamp
                    newTracker.setUpdatedTs(new Date()); // Initialize update timestamp
                    return newTracker;
                });

        tracker.setSpentAmount(tracker.getSpentAmount() + (spentAmount != null ? spentAmount : 0.0));
        tracker.setDescription(description);
        tracker.setTotal(tracker.getIncome() - tracker.getSpentAmount());
        tracker.setSavings(tracker.getTotal());
        tracker.setUpdatedTs(new Date()); // Update the timestamp

        // Add transaction
        ExpenseTransaction transaction = new ExpenseTransaction();
        transaction.setDescription(description);
        transaction.setSpentAmount(spentAmount);
        //transaction.setTransactionDate(tracker.getCreatedTs()); // Use createdTs from ExpenseTracker
        transaction.setCreatedTs(tracker.getCreatedTs());
        transaction.setUpdatedTs(tracker.getUpdatedTs());

        tracker.addTransaction(transaction);

        return expenseTrackerRepository.save(tracker);
    }

    public ExpenseTracker updateExpense(Long customerId, Double income, Double spentAmount, String description) {
        CustomerDoc customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        ExpenseTracker tracker = expenseTrackerRepository.findByCustomer(customer)
                .orElseGet(() -> {
                    ExpenseTracker newTracker = new ExpenseTracker();
                    newTracker.setCustomer(customer);
                    newTracker.setIncome(0.0);
                    newTracker.setSpentAmount(0.0);
                    return newTracker;
                });

        if (income != null) {
            tracker.setIncome(tracker.getIncome() + income);
        }

        if (spentAmount != null) {
            tracker.setSpentAmount(tracker.getSpentAmount() + spentAmount);
        }

        tracker.setDescription(description);
        tracker.setTotal(tracker.getIncome() - tracker.getSpentAmount());
        tracker.setSavings(tracker.getTotal());

        return expenseTrackerRepository.save(tracker);
    }

    @Override
    public Optional<ExpenseTracker> getExpenseTrackerByCustomerId(Long customerId) {
        CustomerDoc customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        return expenseTrackerRepository.findByCustomer(customer);
    }
    
    

  

    @Override
    public List<ExpenseTransaction> getTransactionHistory(Long expenseTrackerId) {
        return expenseTransactionRepository.findByExpenseTracker_Id(expenseTrackerId);
    }
    
    
    @Override
    public void deleteTransaction(Long transactionId, Long customerId) {
        // Verify the customer exists
        CustomerDoc customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // Delete the transaction if it exists and belongs to the customer
        expenseTransactionRepository.deleteByIdAndExpenseTracker_Customer_Id(transactionId, customerId);
    }
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




