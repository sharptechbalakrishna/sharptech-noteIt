package com.sharp.noteIt.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sharp.noteIt.model.SelfNotes;

@Repository
public interface SelfNotesRepository extends JpaRepository<SelfNotes, Integer>{

}
