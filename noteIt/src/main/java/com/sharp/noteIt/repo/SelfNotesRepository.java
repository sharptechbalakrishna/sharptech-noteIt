package com.sharp.noteIt.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sharp.noteIt.model.SelfNotes;

@Repository
public interface SelfNotesRepository extends JpaRepository<SelfNotes, Integer>{
	  List<SelfNotes> findByCustomerId(Long customerId);
	  void deleteById(Integer noteId);
}
