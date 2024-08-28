//package com.sharp.noteIt.security;
//
////import java.lang.foreign.Linker.Option;
//import java.util.Optional;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import com.sharp.noteIt.model.CustomerDoc;
//import com.sharp.noteIt.repo.CustomerRepository;
//
//@Service
//public class CustomerDetailsService implements UserDetailsService{
//	
//	
//	 @Autowired
//	    private CustomerRepository customerDocRepository;
//	 @Autowired
//	    private PasswordEncoder passwordEncoder;
//
////	    public UserDetails loadCustomerByPhone(String phone) throws UsernameNotFoundException {
////	        Optional<CustomerDoc> customer = customerDocRepository.findByPhone(phone);
////	        if (customer.isPresent()) {
////	            var customerObj = customer.get();
////	            return User.builder()
////	                .username(customerObj.getUserName())
////	                .password(customerObj.getPassword())
////	                .roles(customerObj.getRole())  // Assuming getRole() returns a role as a String
////	                .build();
////	        } else {
////	            throw new UsernameNotFoundException(phone);
////	        }
////	    }
//	 
//	 
//	
//	 public UserDetails loadCustomerByPhone(String phone) throws UsernameNotFoundException {
//		    Optional<CustomerDoc> customer = customerDocRepository.findByPhone(phone);
//		    if (customer.isPresent()) {
//		        return new CustomUserDetails(customer.get());
//		    } else {
//		        throw new UsernameNotFoundException("Customer not found with phone: " + phone);
//		    }
//		}
//
//
//	    public String[] getRoles(CustomerDoc customerDoc) {
//	    	if(customerDoc.getRole() == null) {
//	    		return new String[] {"USER"};
//	    	
//	    }
//	    	return customerDoc.getRole().split(",");
//	    }
//	    
//		//user definedclass
//	    @Override
//		public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//	    	Optional<CustomerDoc> customer = customerDocRepository.findByPhone(username);
//	        if (customer.isPresent()) {
//	            return new CustomUserDetails(customer.get());
//	        } else {
//	            throw new UsernameNotFoundException("Customer not found with phone: " + username);
//	        }
//		}
//
//}
//	    