package com.sharp.noteIt.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sharp.noteIt.model.CustomerDoc;
import com.sharp.noteIt.model.User;
import com.sharp.noteIt.repo.CustomerRepository;



@Service
public class OurUserDetailsService implements UserDetailsService {

	@Autowired
    private CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        CustomerDoc customer = customerRepository.findByPhone(phone);
        if (customer == null) {
            throw new UsernameNotFoundException("User not found with phone: " + phone);
        }
        return new User(customer);
    }
}

