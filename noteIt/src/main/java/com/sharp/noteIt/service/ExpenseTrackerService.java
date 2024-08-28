package com.sharp.noteIt.service;

import java.util.List;
import java.util.Optional;

import com.sharp.noteIt.model.CustomerDoc;
import com.sharp.noteIt.model.ExpenseTracker;
import com.sharp.noteIt.model.ExpenseTransaction;
import com.sharp.noteIt.model.UpdateExpenseRequest;

public interface ExpenseTrackerService {
	 Optional<ExpenseTracker> findByCustomer(CustomerDoc customer);
	    ExpenseTracker addIncome(Long customerId, Double income, String description,Double spentAmount);
	    ExpenseTracker addSpentAmount(Long customerId, Double spentAmount, String description);
	    ExpenseTracker updateExpense(Long customerId, Double income, Double spentAmount, String description);
//	    ExpenseTracker updateExpense(UpdateExpenseRequest request);
//		ExpenseTracker updateExpense(Long customerId, Double income, Double spentAmount, String description);
//
	    Optional<ExpenseTracker> getExpenseTrackerByCustomerId(Long customerId);
		List<ExpenseTransaction> getTransactionHistory(Long id);
		
//		 ExpenseTracker getExpenseTrackerByCustomerId(Long customerId);
//		    List<ExpenseTransaction> getExpenseTransactionsByExpenseTrackerId(Long expenseTrackerId);
//		    ExpenseTracker createOrUpdateExpenseTracker(ExpenseTrackerRequest request);
		
		 void deleteTransaction(Long transactionId, Long customerId);
	    }
