package com.sharp.noteIt.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ledger")
public class LedgerCal implements Serializable{
	private static final long serialVersionUID = 5356759213022980760L;
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    private Double principalAmount;
	    private Double interestAmount;
	    private String month;
	    private Integer days;
	    
	    private Double interestPaid;
	    private String status;
	    private boolean locked;

	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "borrower_id")
	    @JsonManagedReference
	    private BorrowerDoc borrower;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Double getPrincipalAmount() {
			return principalAmount;
		}

		public void setPrincipalAmount(Double principalAmount) {
			this.principalAmount = principalAmount;
		}

		public Double getInterestAmount() {
			return interestAmount;
		}

		public void setInterestAmount(Double interestAmount) {
			this.interestAmount = interestAmount;
		}

		public String getMonth() {
			return month;
		}

		public void setMonth(String month) {
			this.month = month;
		}

		public Integer getDays() {
			return days;
		}

		public void setDays(Integer days) {
			this.days = days;
		}

		

		public Double getInterestPaid() {
			return interestPaid;
		}

		public void setInterestPaid(Double interestPaid) {
			this.interestPaid = interestPaid;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public BorrowerDoc getBorrower() {
			return borrower;
		}

		public void setBorrower(BorrowerDoc borrower) {
			this.borrower = borrower;
		}

		public boolean isLocked() {
			return locked;
		}

		public void setLocked(boolean locked) {
			this.locked = locked;
		}

		
	    
}
