//package com.sharp.noteIt.security;
//
//
//
//
//import com.sharp.noteIt.repo.CustomerRepository;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//@Service
//public class CustomUserDetailsService implements UserDetailsService {
//
//    @Autowired
//    private CustomerRepository customerDocRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
//        return customerDocRepository.findByPhone(phone)
//                .map(user -> new CustomUserDetails(user.getPhone(), user.getPassword(), null))
//                .orElseThrow(() -> new UsernameNotFoundException("User not found with phone: " + phone));
//    }
//}
//
