package com.sharp.noteIt.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sharp.noteIt.model.ExpenseTracker;
@Repository
public interface ExpenseTrackerRepository extends JpaRepository<ExpenseTracker, Integer>{

}
