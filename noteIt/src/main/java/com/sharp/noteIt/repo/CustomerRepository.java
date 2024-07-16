package com.sharp.noteIt.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sharp.noteIt.model.CustomerDoc;

public interface CustomerRepository extends JpaRepository<CustomerDoc, Long>{

}
