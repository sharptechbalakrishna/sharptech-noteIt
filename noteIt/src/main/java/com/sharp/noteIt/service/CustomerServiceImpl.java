package com.sharp.noteIt.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sharp.noteIt.model.BorrowerDoc;
import com.sharp.noteIt.model.BorrowerRequest;
import com.sharp.noteIt.model.CustomerDoc;
import com.sharp.noteIt.model.CustomerRequest;
import com.sharp.noteIt.repo.BorrowerRepository;
import com.sharp.noteIt.repo.CustomerRepository;
import com.twilio.rest.api.v2010.account.Message;

@Service
public class CustomerServiceImpl implements CustomerServiceI {

	@Autowired
	CustomerRepository repo;

	@Override
	public CustomerDoc saveCustomer(CustomerRequest request) {
		CustomerDoc convertPojoToEntity = convertPojoToEntity(request);
		return repo.save(convertPojoToEntity);

	}

	private CustomerDoc convertPojoToEntity(CustomerRequest request) {
		CustomerDoc doc = new CustomerDoc();
		doc.setId(request.getId());
		doc.setEmail(request.getEmail());
		doc.setFirstName(request.getFirstName());
		doc.setLastName(request.getLastName());
		doc.setUserName(request.getUserName());
		doc.setPhone(request.getPhone());
		doc.setPassword(request.getPassword());
		doc.setCreatedBy(request.getUserName());
		doc.setCreatedTs(new Date());
		doc.setUpdatedTs(new Date());
		//doc.setBorrowers(request.getBorrowers());
//		List<BorrowerDoc> borrowerDocs = request.getBorrowers().stream()
//		        .map(borrowerRequest -> {
//		            BorrowerDoc borrowerDoc = new BorrowerDoc();
//		            borrowerDoc.setBorrowerName(borrowerRequest.getBorrowerName());
//		            borrowerDoc.setPhoneNumber(borrowerRequest.getPhoneNumber());
//		            borrowerDoc.setEmail(borrowerRequest.getEmail());
//		            borrowerDoc.setPrincipalAmount(borrowerRequest.getPrincipalAmount());
//		            borrowerDoc.setInterestRate(borrowerRequest.getInterestRate());
//		            borrowerDoc.setCreditBasis(borrowerRequest.getCreditBasis());
//		            borrowerDoc.setCreditStatus(borrowerRequest.getCreditStatus());
//		            borrowerDoc.setBorrowedDate(borrowerRequest.getBorrowedDate());
//		            borrowerDoc.setEndDate(borrowerRequest.getEndDate());
//		            return borrowerDoc;
//		        })
//		        .collect(Collectors.toList());
//		doc.setBorrowers(borrowerDocs);
		return doc;
	}
	
	

	@Override
	public CustomerDoc login(String phone, String password) {
		return repo.findByPhoneAndPassword(phone, password)
				.orElseThrow(() -> new IllegalArgumentException("Invalid phone or password"));
	}

	@Override
	public CustomerDoc getCustomerProfile(String firstName) {
		return repo.getCustomerByfirstName(firstName).orElseThrow(() -> new IllegalArgumentException("Customer not found"));
	}

	@Override
	public List<CustomerDoc> getAllCustomers() {
		return repo.findAll();
	}

	@Autowired
    private BorrowerRepository borrowerRepository;
    
    @Autowired
    private CustomerRepository customerRepository;

    

    @Override
    public CustomerDoc addBorrowerToCustomer(Long customerId, BorrowerDoc borrowerDoc) {
        Optional<CustomerDoc> customerOptional = customerRepository.findById(customerId);

        if (customerOptional.isPresent()) {
            CustomerDoc customer = customerOptional.get();
            borrowerDoc.setCustomerDoc(customer);
            borrowerRepository.save(borrowerDoc);
            customer.getBorrowers().add(borrowerDoc);
            return customerRepository.save(customer);
        } else {
            throw new RuntimeException("Customer not found");
        }
    }
    
 
    public Optional<CustomerDoc> findCustomerById(Long customerId) {
        return customerRepository.findById(customerId);
    }

  
    public List<BorrowerRequest> getBorrowersForCustomer(Long customerId) {
        Optional<CustomerDoc> customerOptional = customerRepository.findById(customerId);

        if (customerOptional.isPresent()) {
            CustomerDoc customer = customerOptional.get();
            return customer.getBorrowers().stream()
                    .map(borrower -> {
                        BorrowerRequest br = new BorrowerRequest();
                        br.setBorrowerName(borrower.getBorrowerName());
                        br.setPrincipalAmount(borrower.getPrincipalAmount());
                        return br;
                    })
                    .collect(Collectors.toList());
        } else {
            throw new RuntimeException("Customer not found");
        }
    }
    
    
	
	
	
	
	
	
	
	
	
	
	
//	@Override
//	public void sendSms(String to, String message) {
//		// TODO Auto-generated method stub
//		
//	}

	
	
//	 @Override
//	    public void sendSms(String to, String message) {
//	        try {
//	            Message.creator(
//	                    new PhoneNumber(to),
//	                    new PhoneNumber(twilioPhoneNumber),
//	                    message
//	            ).create();
//	            logger.info("Sent SMS to {}", to);
//	        } catch (Exception e) {
//	            logger.error("Failed to send SMS to {}: {}", to, e);
//	        }
//	    }

//	 import java.util.Date;
//	 import java.util.List;
//	 import java.util.stream.Collectors;
//
//	 import org.slf4j.Logger;
//	 import org.slf4j.LoggerFactory;
//	 import org.springframework.beans.factory.annotation.Autowired;
//	 import org.springframework.beans.factory.annotation.Value;
//	 import org.springframework.stereotype.Service;
//
//	 import com.sharp.noteIt.model.BorrowerDoc;
//	 import com.sharp.noteIt.model.CustomerDoc;
//	 import com.sharp.noteIt.model.CustomerRequest;
//	 import com.sharp.noteIt.repo.CustomerRepository;
//	 import com.twilio.rest.api.v2010.account.Message;
//	 import com.twilio.type.PhoneNumber;

}
