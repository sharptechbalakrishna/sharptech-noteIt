package com.sharp.noteIt.model;

public class LedgerUpdateResponse {
	private Long id;
    private String month;
    private double principalAmount;
    private double interestAmount;
    private double interestPaid;
    private String status;
    private boolean locked;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public double getPrincipalAmount() {
		return principalAmount;
	}
	public void setPrincipalAmount(double principalAmount) {
		this.principalAmount = principalAmount;
	}
	public double getInterestAmount() {
		return interestAmount;
	}
	public void setInterestAmount(double interestAmount) {
		this.interestAmount = interestAmount;
	}
	public double getInterestPaid() {
		return interestPaid;
	}
	public void setInterestPaid(double interestPaid) {
		this.interestPaid = interestPaid;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public boolean isLocked() {
		return locked;
	}
	public void setLocked(boolean locked) {
		this.locked = locked;
	}
}
