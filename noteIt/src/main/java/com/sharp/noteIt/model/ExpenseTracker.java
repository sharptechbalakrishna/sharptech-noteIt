package com.sharp.noteIt.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "expensetracker")
public class ExpenseTracker {
	
	@Id
	@GeneratedValue
	private Long id;
	private String Description;
	private Double totalAmount;
	private Double spentAmount;
	private Double total;
	private Double savings;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public Double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public Double getSpentAmount() {
		return spentAmount;
	}
	public void setSpentAmount(Double spentAmount) {
		this.spentAmount = spentAmount;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	public Double getSavings() {
		return savings;
	}
	public void setSavings(Double savings) {
		this.savings = savings;
	}
	
	

}
