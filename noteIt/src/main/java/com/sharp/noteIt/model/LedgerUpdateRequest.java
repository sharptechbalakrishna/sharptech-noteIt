package com.sharp.noteIt.model;

public class LedgerUpdateRequest {
	 private Long ledgerId;
	    private double interestPaid;
		public Long getLedgerId() {
			return ledgerId;
		}
		public void setLedgerId(Long ledgerId) {
			this.ledgerId = ledgerId;
		}
		public double getInterestPaid() {
			return interestPaid;
		}
		public void setInterestPaid(double interestPaid) {
			this.interestPaid = interestPaid;
		}
	    

}
