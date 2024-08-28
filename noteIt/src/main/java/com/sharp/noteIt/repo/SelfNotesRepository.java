package com.sharp.noteIt.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sharp.noteIt.model.SelfNotes;

import jakarta.transaction.Transactional;

@Repository
public interface SelfNotesRepository extends JpaRepository<SelfNotes, Integer>{
	  List<SelfNotes> findByCustomerId(Long customerId);
	  void deleteById(Integer noteId);
	  
//	  @Modifying
//	    @Transactional
//	    @Query("DELETE FROM SelfNotes s WHERE s.customer.id = :customerId")
//	    void deleteAllByCustomerId(@Param("customerId") Long customerId);
}
