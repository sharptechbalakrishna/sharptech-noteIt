package com.sharp.noteIt.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.sharp.noteIt.model.CustomerDoc;
import com.sharp.noteIt.model.Otp;
import com.sharp.noteIt.model.ReqRes;
import com.sharp.noteIt.repo.CustomerRepository;
import com.sharp.noteIt.repo.OtpRepository;

import jakarta.transaction.Transactional;

@Service
public class OtpServiceImpl implements OtpService {

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private JavaMailSender javaMailSender; // Add JavaMailSender for email sending

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    @Transactional
    public ReqRes generateAndSendOtp(String email) {
        // Check if the email exists in the CustomerDoc repository
        Optional<CustomerDoc> customerOptional = customerRepository.findSingleByEmail(email);
        if (customerOptional.isEmpty()) {
            return new ReqRes(404, "Email does not exist.");
        }

        // Delete any existing OTP associated with this email before generating a new one
        otpRepository.deleteByEmail(email);

        // Generate a new OTP
        String otpValue = generateOtp();
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(5); // OTP valid for 5 minutes

        // Create a new OTP object and save it
        Otp otp = new Otp();
        otp.setEmail(email);
        otp.setOtp(otpValue);
        otp.setExpirydate(expiryTime);

        otpRepository.save(otp);

        // Send the OTP via email
        try {
            sendOtpEmail(email, otpValue); // Send OTP to the provided email
            return new ReqRes(200, "OTP sent successfully to email: " + email);
        } catch (Exception e) {
            return new ReqRes(500, "Failed to send OTP email: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean validateOtp(String email, String otp) {
        Otp storedOtp = otpRepository.findByEmailAndOtp(email, otp);
        if (storedOtp != null && storedOtp.getExpirydate().isAfter(LocalDateTime.now())) {
            otpRepository.delete(storedOtp); // OTP is valid and should be removed after use
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public CustomerDoc getCustomerByEmail(String email) {
        Optional<CustomerDoc> customerOpt = customerRepository.findSingleByEmail(email);
        if (customerOpt.isPresent()) {
            return customerOpt.get();
        } else {
            throw new RuntimeException("Customer not found with email: " + email);
        }
    }


    @Override
    @Transactional
    public void changePassword(Long customerId, String oldPassword, String newPassword, String confirmPassword) {
        CustomerDoc customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("No customer found with ID: " + customerId));

        // Verify old password
        if (!bCryptPasswordEncoder.matches(oldPassword, customer.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }

        // Check if new password and confirm password match
        if (!newPassword.equals(confirmPassword)) {
            throw new RuntimeException("New password and confirm password do not match");
        }

        // Set and save the new password
        customer.setPassword(bCryptPasswordEncoder.encode(newPassword));
        customerRepository.save(customer);
    }

    // Generate a 4-digit OTP
    private String generateOtp() {
        return String.format("%04d", new Random().nextInt(10000));
    }

    // Method to send OTP to the user's email
    private void sendOtpEmail(String email, String otpValue) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your OTP Code");
        message.setText("Your OTP code is: " + otpValue + ". Please use this code to proceed.");
        javaMailSender.send(message);
    }
}
