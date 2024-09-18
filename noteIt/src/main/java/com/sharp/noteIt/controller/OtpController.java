package com.sharp.noteIt.controller;

import com.sharp.noteIt.model.ChangePasswordRequest;
import com.sharp.noteIt.model.CustomerDoc;
import com.sharp.noteIt.model.ReqRes;
import com.sharp.noteIt.repo.CustomerRepository;
import com.sharp.noteIt.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.NoSuchElementException;

@RestController
public class OtpController {

    @Autowired
    private OtpService otpService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Forgot password - Generates OTP and sends it to the provided email
    @PostMapping("/forgot-password")
    public ResponseEntity<ReqRes> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        ReqRes response = otpService.generateAndSendOtp(email);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    // Reset password - Validates OTP and allows the user to reset the password
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");
        String newPassword = request.get("newPassword");

        if (otpService.validateOtp(email, otp)) {
            CustomerDoc customer = otpService.getCustomerByEmail(email);
            if (customer == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not registered");
            }
            customer.setPassword(passwordEncoder.encode(newPassword));
            customerRepository.save(customer);
            return ResponseEntity.ok("Password has been reset successfully");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid OTP");
        }
    }

    // Change password - Requires the customer to provide the old password and new password
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest request) {
        try {
            otpService.changePassword(request.getCustomerId(), request.getOldPassword(), 
                                       request.getNewPassword(), request.getConfirmPassword());
            return ResponseEntity.ok("Password changed successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}


