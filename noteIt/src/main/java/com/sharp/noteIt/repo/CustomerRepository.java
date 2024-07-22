package com.sharp.noteIt.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sharp.noteIt.model.CustomerDoc;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerDoc, Long>{
	Optional<CustomerDoc> findByPhoneAndPassword(String phone, String password);
	Optional<CustomerDoc> findByName(String name);

}
