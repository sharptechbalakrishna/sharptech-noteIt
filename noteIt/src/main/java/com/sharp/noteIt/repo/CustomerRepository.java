package com.sharp.noteIt.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.sharp.noteIt.model.CustomerDoc;

@Repository
@EnableJpaRepositories
public interface CustomerRepository extends JpaRepository<CustomerDoc, Long>{
	Optional<CustomerDoc> findByPhoneAndPassword(String phone, String password);
	Optional<CustomerDoc> getCustomerByfirstName(String firstName);
	Optional<CustomerDoc> findById(Long id);

}
