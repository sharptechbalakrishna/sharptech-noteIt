package com.sharp.noteIt.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class BorrowerRequest implements Serializable{
	
	private static final long serialVersionUID = -2102633356770338746L;
	
	  
	    private String borrowerName;
	    private Double principalAmount;
		
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
	 
	 
	    
			    	    
}
