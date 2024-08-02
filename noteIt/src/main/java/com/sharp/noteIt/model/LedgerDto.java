package com.sharp.noteIt.model;

public class LedgerDto {
	
	private static final long serialVersionUID = -2102633356770338746L;
	  private Long id;
	    private Double principalAmount;
	    private Double interestAmount;
	    private String month;
	    private Integer days;
	    private Double cumulativeInterest;
	    private Double interestPaid;
	    private String status;
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
		public Double getCumulativeInterest() {
			return cumulativeInterest;
		}
		public void setCumulativeInterest(Double cumulativeInterest) {
			this.cumulativeInterest = cumulativeInterest;
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
	    

}
