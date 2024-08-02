package com.sharp.noteIt.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "expensetracker")
public class ExpenseTracker {
	
	@Id  
	@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "description") // Use lowercase naming
    private String description;

    @Column(name = "income")
    private Double income;

    @Column(name = "spent_amount")
   private Double spentAmount;

    @Column(name = "total")
    private Double total;

    @Column(name = "savings")
    private Double savings;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    @JsonBackReference // Ensure this annotation matches your use case
    private CustomerDoc customer;


	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public CustomerDoc getCustomer() {
		return customer;
	}
	public void setCustomer(CustomerDoc customer) {
		this.customer = customer;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
