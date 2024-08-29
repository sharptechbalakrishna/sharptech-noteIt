package com.sharp.noteIt.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.sharp.noteIt.model.CustomerDoc;
import com.sharp.noteIt.model.Otp;
import com.sharp.noteIt.model.ReqRes;
import com.sharp.noteIt.repo.CustomerRepository;
import com.sharp.noteIt.repo.OtpRepository;

@Service
public class OtpServiceImpl implements OtpService {

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public ReqRes generateAndSendOtp(String email) {
        String otpValue = generateOtp();
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(5); // OTP valid for 5 minutes

        Otp otp = new Otp();
        otp.setEmail(email);
        otp.setOtp(otpValue);
        otp.setExpirydate(expiryTime);

        otpRepository.save(otp);

        // Simulate sending OTP (In real implementation, integrate with email/SMS service)
        System.out.println("OTP sent to email: " + otpValue);

        return new ReqRes(200, "OTP sent successfully");
    }

    @Override
    public boolean validateOtp(String email, String otp) {
        Otp storedOtp = otpRepository.findByEmailAndOtp(email, otp);
        if (storedOtp != null && storedOtp.getExpirydate().isAfter(LocalDateTime.now())) {
            otpRepository.delete(storedOtp); // OTP is used and should be removed
            return true;
        }
        return false;
    }

    private String generateOtp() {
        return String.format("%04d", new Random().nextInt(10000));
    }

    @Override
    public CustomerDoc getCustomerByEmail(String email) {
        List<CustomerDoc> customers = customerRepository.findByEmail(email);
        if (customers.isEmpty()) {
            return null; // No customer found with this email
        }
        if (customers.size() > 1) {
            // Handle the case where multiple customers are found
            // This could involve logging the issue, notifying admins, or resolving the conflict
            System.err.println("Warning: Multiple customers found for email: " + email);
            // Optionally, return null or handle according to your business logic
            // You might also consider returning the first customer or raising an exception if needed
        }
        return customers.get(0); // Return the first customer if found
    }
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Override
    public void changePassword(Long customerId, String oldPassword, String newPassword, String confirmPassword) {
        CustomerDoc customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("No customer found with ID: " + customerId));

        // Check if old password matches
        if (!bCryptPasswordEncoder.matches(oldPassword, customer.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }

        // Check if new password and confirm password match
        if (!newPassword.equals(confirmPassword)) {
            throw new RuntimeException("New password and confirm password do not match");
        }

        // Set the new password
        customer.setPassword(bCryptPasswordEncoder.encode(newPassword));
        customerRepository.save(customer);
    }
}
