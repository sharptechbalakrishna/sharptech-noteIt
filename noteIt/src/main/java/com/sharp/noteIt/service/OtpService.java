package com.sharp.noteIt.service;

import org.springframework.stereotype.Service;

import com.sharp.noteIt.model.CustomerDoc;
import com.sharp.noteIt.model.ReqRes;
import com.sharp.noteIt.model.User;

public interface OtpService {
	 	ReqRes generateAndSendOtp(String email);
	    boolean validateOtp(String email, String otp);
	    CustomerDoc getCustomerByEmail(String email);
	    void changePassword(Long customerId, String oldPassword, String newPassword, String confirmPassword);
}
