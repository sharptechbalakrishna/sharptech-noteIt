package com.sharp.noteIt.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class ExpenseTrackerRequest implements Serializable{
	
	private static final long serialVersionUID = -2102633356770338746L;
	
	private Long customerId;
    private Double income;
    private Double spentAmount;
    private String description;
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public Double getIncome() {
		return income;
	}
	public void setIncome(Double income) {
		this.income = income;
	}
	public Double getSpentAmount() {
		return spentAmount;
	}
	public void setSpentAmount(Double spentAmount) {
		this.spentAmount = spentAmount;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
    

}
