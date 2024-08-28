package com.sharp.noteIt.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sharp.noteIt.model.CustomerDoc;
import com.sharp.noteIt.model.Otp;
import com.sharp.noteIt.model.ReqRes;
import com.sharp.noteIt.repo.CustomerRepository;
import com.sharp.noteIt.repo.OtpRepository;

import jakarta.persistence.NonUniqueResultException;

@Service
public class OtpServiceImpl implements OtpService {

    @Autowired
    private OtpRepository otpRepository;

    @Override
    public ReqRes generateAndSendOtp(String email) {
        String otpValue = generateOtp();
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(5); // OTP valid for 5 minutes

        Otp otp = new Otp();
        otp.setEmail(email);
        otp.setOtp(otpValue);
        otp.setExpiryTime(expiryTime);

        otpRepository.save(otp);

        // Simulate sending OTP (In real implementation, integrate with email/SMS service)
        System.out.println("OTP sent to email: " + otpValue);

        return new ReqRes(200, "OTP sent successfully");
    }

    @Override
    public boolean validateOtp(String email, String otp) {
        Otp storedOtp = otpRepository.findByEmailAndOtp(email, otp);
        if (storedOtp != null && storedOtp.getExpiryTime().isAfter(LocalDateTime.now())) {
            otpRepository.delete(storedOtp); // OTP is used and should be removed
            return true;
        }
        return false;
    }

    private String generateOtp() {
        return String.format("%04d", new Random().nextInt(10000));
    }
    
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public CustomerDoc getCustomerByEmail(String email) {
        List<CustomerDoc> customers = customerRepository.findByEmail(email);
        if (customers.size() > 1) {
            throw new NonUniqueResultException("Multiple results found for email: " + email);
        }
        return customers.stream().findFirst().orElse(null);
    }
}
