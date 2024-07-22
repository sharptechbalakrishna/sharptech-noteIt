package com.sharp.noteIt.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class CustomerRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2102633356770338746L;
	private Long id;
	private String name;
	private String email;
	private String phone;
	private String password;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
