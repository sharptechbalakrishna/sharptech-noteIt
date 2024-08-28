package com.sharp.noteIt.controller;

import com.sharp.noteIt.model.*;
import com.sharp.noteIt.repo.CustomerRepository;
import com.sharp.noteIt.security.JWTService;
import com.sharp.noteIt.security.OurUserDetailsService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private OurUserDetailsService userDetailsService;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody CustomerDoc customerDoc) {
        if (customerRepository.findByPhone(customerDoc.getPhone()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Phone number already registered");
        }
        customerDoc.setPassword(passwordEncoder.encode(customerDoc.getPassword()));
        customerRepository.save(customerDoc);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
//        try {
//            authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(
//                            authRequest.getPhone(),
//                            authRequest.getPassword()
//                    )
//            );
//            final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getPhone());
//            final String token = jwtService.generateToken(userDetails);
//            return ResponseEntity.ok(new AuthResponse(token));
//        } catch (BadCredentialsException e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid phone number or password");
//        }
//    }
//    private final AuthenticationManager authenticationManager;
//    private final JwtService jwtService;
//    private final UserDetailsService userDetailsService;
    private Map<String, String> transactionMap = new HashMap<>();

    public AuthController(AuthenticationManager authenticationManager, JWTService jwtService, UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = (OurUserDetailsService) userDetailsService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        try {
            // Authenticate the user with the phone and password
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getPhone(),
                            authRequest.getPassword()
                    )
            );

            // Load user details and generate JWT token
            final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getPhone());
            final String token = jwtService.generateToken(userDetails);

            // Generate a transaction ID
            String transactionId = UUID.randomUUID().toString();
            transactionMap.put(authRequest.getPhone(), transactionId);

            // Create and return the login response
            LoginResponse response = new LoginResponse();
            response.setJwtToken(token);
            response.setTransactionId(transactionId);
            response.setMessage("Login successful!");

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid phone number or password");
        }
    }


    @PostMapping("/logout")
    public String logout(@RequestBody LogoutRequest request) {
        String storedTransactionId = transactionMap.get(request.getPhone());

        if (storedTransactionId != null && storedTransactionId.equals(request.getTransactionId())) {
            transactionMap.remove(request.getPhone());
            return "Logout successful!";
        } else {
            return "Invalid transaction ID or phone number!";
        }
    }
}
