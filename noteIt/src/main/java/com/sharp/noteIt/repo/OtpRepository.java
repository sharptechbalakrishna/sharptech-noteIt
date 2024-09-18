package com.sharp.noteIt.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sharp.noteIt.model.Otp;

@Repository
public interface OtpRepository extends JpaRepository<Otp, Long>{
	Otp findByEmailAndOtp(String email, String otp);
	
	    void deleteByEmail(String email);
	
}
