package com.sharp.noteIt.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "expensetracker")
public class ExpenseTracker implements Serializable{
	 private static final long serialVersionUID = 5356759213022980760L;

	 @Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
	    private Long id;

	    @Column(name = "description")
	    private String description;

	    @Column(name = "income")
	    private Double income;

	    @Column(name = "spent_amount")
	    private Double spentAmount;

	    @Column(name = "total")
	    private Double total;

	    @Column(name = "savings")
	    private Double savings;

	    @OneToMany(mappedBy = "expenseTracker", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	    @JsonManagedReference
	    private List<ExpenseTransaction> transactions = new ArrayList<>();

	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "customer_id")
	    @JsonBackReference
	    private CustomerDoc customer;

	    @Column(name = "created_by")
	    private String createdBy;

	    @Column(name = "created_ts")
	    private Date createdTs;

	    @Column(name = "updated_ts")
	    private Date updatedTs;
	    // Getters and setters

	    public void addTransaction(ExpenseTransaction transaction) {
	        this.transactions.add(transaction);
	        transaction.setExpenseTracker(this);
	        updateTotals();
	    }

	    public void updateTotals() {
	        this.total = this.income - this.spentAmount;
	        this.savings = this.total;
	    }
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
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedTs() {
		return createdTs;
	}
	public void setCreatedTs(Date createdTs) {
		this.createdTs = createdTs;
	}
	public Date getUpdatedTs() {
		return updatedTs;
	}
	public void setUpdatedTs(Date updatedTs) {
		this.updatedTs = updatedTs;
	}
	public List<ExpenseTransaction> getTransactions() {
		return transactions;
	}
	public void setTransactions(List<ExpenseTransaction> transactions) {
		this.transactions = transactions;
	}
	

	

	
}
