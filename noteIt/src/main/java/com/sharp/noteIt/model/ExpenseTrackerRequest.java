package com.sharp.noteIt.model;

import java.io.Serializable;
import java.sql.Date;

import lombok.Data;

@Data
public class ExpenseTrackerRequest implements Serializable{
	
	private static final long serialVersionUID = -2102633356770338746L;
	
	 private Long id;
	    private String description;
	    private Double income;
	    private Double spentAmount;
	    private Double total;
	    private Double savings;
	    private Long customerId; // ID of the associated CustomerDoc
	    private String createdBy;
	    private Date createdTs;
	    private Date updatedTs;

	    // Getters and setters

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

	    public Long getCustomerId() {
	        return customerId;
	    }

	    public void setCustomerId(Long customerId) {
	        this.customerId = customerId;
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
    

}
