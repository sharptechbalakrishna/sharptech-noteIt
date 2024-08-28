package com.sharp.noteIt.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "borrower")
public class BorrowerDoc implements Serializable{

	 private static final long serialVersionUID = 5356759213022980760L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String borrowerName;
    private String phoneNumber;
    private String email;
    private Double principalAmount;
    private Double interestRate;
    private String creditBasis;
    private String creditStatus;
    private Date borrowedDate;
    private Date endDate;
    private String timePeriodUnit;
    private Long timePeriodNumber;
    private String status;
    

    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id",nullable = false)
    @JsonBackReference
    private CustomerDoc customerDoc;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getBorrowerName() {
		return borrowerName;
	}
	public void setBorrowerName(String borrowerName) {
		this.borrowerName = borrowerName;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Double getPrincipalAmount() {
		return principalAmount;
	}
	public void setPrincipalAmount(Double principalAmount) {
		this.principalAmount = principalAmount;
	}
	public Double getInterestRate() {
		return interestRate;
	}
	public void setInterestRate(Double interestRate) {
		this.interestRate = interestRate;
	}
	public String getCreditBasis() {
		return creditBasis;
	}
	public void setCreditBasis(String creditBasis) {
		this.creditBasis = creditBasis;
	}
	public String getCreditStatus() {
		return creditStatus;
	}
	public void setCreditStatus(String creditStatus) {
		this.creditStatus = creditStatus;
	}
	public Date getBorrowedDate() {
		return borrowedDate;
	}
	public void setBorrowedDate(Date borrowedDate) {
		this.borrowedDate = borrowedDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public CustomerDoc getCustomerDoc() {
		return customerDoc;
	}
	public void setCustomerDoc(CustomerDoc customerDoc) {
		this.customerDoc = customerDoc;
	}
	public String getTimePeriodUnit() {
		return timePeriodUnit;
	}
	public void setTimePeriodUnit(String timePeriodUnit) {
		this.timePeriodUnit = timePeriodUnit;
	}
	public Long getTimePeriodNumber() {
		return timePeriodNumber;
	}
	public void setTimePeriodNumber(Long timePeriodNumber) {
		this.timePeriodNumber = timePeriodNumber;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

   
}
