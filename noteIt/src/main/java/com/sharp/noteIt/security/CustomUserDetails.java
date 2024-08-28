package com.sharp.noteIt.security;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.sharp.noteIt.model.BorrowerDoc;
import com.sharp.noteIt.model.CustomerDoc;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

	private Long id;
	private String firstName;
	private String email;
	private String phone;
	private String password;
	private String lastName;
	private String userName;
	private List<BorrowerDoc> borrowers;
	private String Image;
	private String role;

    public CustomUserDetails(CustomerDoc customer) {
        this.userName = customer.getUserName();
        this.password = customer.getPassword();
        this.role = customer.getRole();
        this.phone = customer.getPhone();
        this.email = customer.getEmail();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Convert role to authorities, if needed
        return List.of(() -> role);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<BorrowerDoc> getBorrowers() {
		return borrowers;
	}

	public void setBorrowers(List<BorrowerDoc> borrowers) {
		this.borrowers = borrowers;
	}

	public String getImage() {
		return Image;
	}

	public void setImage(String image) {
		Image = image;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    // Add any other customer details you want to access here
}
