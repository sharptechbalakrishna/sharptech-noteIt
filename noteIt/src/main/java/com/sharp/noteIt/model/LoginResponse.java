package com.sharp.noteIt.model;



public class LoginResponse {
	private String jwtToken;
    private String transactionId;
    private String message;
    private CustomerDoc customerDetails;

    public String getJwtToken() {
		return jwtToken;
	}

	public void setJwtToken(String jwtToken) {
		this.jwtToken = jwtToken;
	}

	// Getters and Setters
    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

	public CustomerDoc getCustomerDetails() {
		return customerDetails;
	}

	public void setCustomerDetails(CustomerDoc customerDetails) {
		this.customerDetails = customerDetails;
	}
    
}

