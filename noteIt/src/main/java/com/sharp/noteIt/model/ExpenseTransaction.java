package com.sharp.noteIt.model;


import java.sql.Time;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;

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
@Table(name = "expense_transaction")
public class ExpenseTransaction {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "income")
    private Double income;

    @Column(name = "spent_amount")
    private Double spentAmount;

    @Column(name = "created_ts")
    private Date createdTs;

    @Column(name = "updated_ts")
    private Date updatedTs;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expense_tracker_id",nullable = false)
    @JsonBackReference
    private ExpenseTracker expenseTracker;
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public ExpenseTracker getExpenseTracker() {
		return expenseTracker;
	}

	public void setExpenseTracker(ExpenseTracker expenseTracker) {
		this.expenseTracker = expenseTracker;
	}    

    }
