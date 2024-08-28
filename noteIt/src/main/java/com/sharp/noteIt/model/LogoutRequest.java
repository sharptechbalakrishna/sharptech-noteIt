package com.sharp.noteIt.model;



public class LogoutRequest {
    private String phone;
    private String transactionId;

    // Getters and Setters
   

    public String getTransactionId() {
        return transactionId;
    }

    public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}

