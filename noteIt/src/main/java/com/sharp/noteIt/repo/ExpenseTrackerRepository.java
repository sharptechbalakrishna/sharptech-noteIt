package com.sharp.noteIt.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sharp.noteIt.model.CustomerDoc;
import com.sharp.noteIt.model.ExpenseTracker;
import com.sharp.noteIt.model.ExpenseTransaction;

import jakarta.transaction.Transactional;
@Repository
@EnableJpaRepositories
public interface ExpenseTrackerRepository extends JpaRepository<ExpenseTracker, Long> {
    List<ExpenseTracker> findByCustomerId(Long customerId);
    Optional<ExpenseTracker> findByCustomer(CustomerDoc customer);
   // Optional<ExpenseTracker> getExpenseTrackerByCustomerId(Long customerId);
//    List<ExpenseTransaction> findByExpenseTrackerId(Long expenseTrackerId);
    
//    @Modifying
//    @Transactional
//    @Query("DELETE FROM ExpenseTracker e WHERE e.customer.id = :customerId")
//    void deleteAllByCustomerId(@Param("customerId") Long customerId);
  
}

