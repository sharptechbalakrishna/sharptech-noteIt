package com.sharp.noteIt.model;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;

public class BaseDoc implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1599218789902578309L;
	
	@Column
	private String createdBy;
	@Column
	private Date createdTs;
	@Column
	private Date updatedTs;
	
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
