package com.sharp.noteIt.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "notes")
public class SelfNotes {
	@Id
	@GeneratedValue
	private Integer id;
	private String notes;
	private String title;
	//private String createdBy;
	private Date createdTs;
	private Date updatedTs;
	
	@ManyToOne
    @JoinColumn(name = "customer_id", nullable = true)
	@JsonBackReference
    private CustomerDoc customer;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
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
	public CustomerDoc getCustomer() {
		return customer;
	}
	public void setCustomer(CustomerDoc customer) {
		this.customer = customer;
	}
	
	
}
