
package com.sharp.noteIt.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.sharp.noteIt.model.BorrowerDoc;

import jakarta.transaction.Transactional;

@Repository
//@EnableJpaRepositories
public interface BorrowerRepository extends JpaRepository<BorrowerDoc, Long>{
	
	  //BorrowerDoc findByIdAndCustomerId(Long borrowerId, Long customerId);
	@Query("SELECT b FROM BorrowerDoc b WHERE b.id = :borrowerId AND b.customerDoc.id = :customerId")
    Optional<BorrowerDoc> findByIdAndCustomerId(@Param("borrowerId") Long borrowerId, @Param("customerId") Long customerId);
	
	
//	@Modifying
//    @Transactional
//    @Query("DELETE FROM BorrowerDoc b WHERE b.customer.id = :customerId")
//    void deleteAllByCustomerId(@Param("customerId") Long customerId);
	
//	@Modifying
//	@Transactional
//	@Query("DELETE FROM BorrowerDoc b WHERE b.customerDoc.id = :customerId")
//	void deleteAllByCustomerId(@Param("customerId") Long customerId);


}
