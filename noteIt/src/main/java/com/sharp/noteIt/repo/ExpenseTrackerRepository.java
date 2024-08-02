package com.sharp.noteIt.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.sharp.noteIt.model.CustomerDoc;
import com.sharp.noteIt.model.ExpenseTracker;
@Repository
public interface ExpenseTrackerRepository extends JpaRepository<ExpenseTracker, Long> {
    List<ExpenseTracker> findByCustomerId(Long customerId);
    Optional<ExpenseTracker> findByCustomer(CustomerDoc customer);
}

