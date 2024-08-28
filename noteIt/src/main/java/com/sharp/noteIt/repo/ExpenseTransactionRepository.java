package com.sharp.noteIt.repo;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.sharp.noteIt.model.ExpenseTransaction;
@Repository
@EnableJpaRepositories
public interface ExpenseTransactionRepository extends JpaRepository<ExpenseTransaction, Long>{
	 List<ExpenseTransaction> findByExpenseTracker_Id(Long expenseTrackerId);
	 void deleteByIdAndExpenseTracker_Customer_Id(Long id, Long customerId);
}