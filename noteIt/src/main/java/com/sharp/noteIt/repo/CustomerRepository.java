package com.sharp.noteIt.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sharp.noteIt.model.CustomerDoc;

@Repository
@EnableJpaRepositories
public interface CustomerRepository extends JpaRepository<CustomerDoc, Long>{
	Optional<CustomerDoc> findByPhoneAndPassword(String phone, String password);
	Optional<CustomerDoc> getCustomerById(Long id);
	Optional<CustomerDoc> findById(Long id);
	CustomerDoc findByPhone(String phone);
	@Query("SELECT c FROM CustomerDoc c WHERE c.email = :email")
	List<CustomerDoc> findByEmail(String email);
	 @Query("SELECT c FROM CustomerDoc c WHERE c.email = :email")
	    CustomerDoc findSingleByEmail(@Param("email") String email);
	
	//Optional<CustomerDoc> findByemai(String email);
	
}
