package com.sharp.noteIt.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class BorrowerRequest implements Serializable{
	
	private static final long serialVersionUID = -2102633356770338746L;
	
	  
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
		
		public String getBorrowerName() {
			return borrowerName;
		}
		public void setBorrowerName(String borrowerName) {
			this.borrowerName = borrowerName;
		}
		public Double getPrincipalAmount() {
			return principalAmount;
		}
		public void setPrincipalAmount(Double principalAmount) {
			this.principalAmount = principalAmount;
		}
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
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
