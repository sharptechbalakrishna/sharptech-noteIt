package com.sharp.noteIt.model;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
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

}
