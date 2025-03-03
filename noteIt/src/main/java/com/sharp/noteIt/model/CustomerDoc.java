package com.sharp.noteIt.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "customer")
public class CustomerDoc extends BaseDoc {

    private static final long serialVersionUID = 5356759213022980760L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String userName;

    @Column
    private String email;

    @Column
    private String phone;

    @Column
    private String password;
    
    @Column
    private String image;
    
    @Column
    private String role;

   

    public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@OneToMany(targetEntity = BorrowerDoc.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER,orphanRemoval = true)
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    @JsonManagedReference
    private List<BorrowerDoc> borrowers;
    
    @OneToMany(targetEntity = SelfNotes.class, cascade = CascadeType.REMOVE, fetch = FetchType.EAGER,orphanRemoval = true)
    @JoinColumn(name = "customer_id",referencedColumnName = "id")
    @JsonManagedReference
    List<SelfNotes> selfnotes;
   
    
    @OneToMany(targetEntity = ExpenseTracker.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER,orphanRemoval = true)
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    @JsonManagedReference
    private List<ExpenseTracker> expenseTracker;
 
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

    public List<BorrowerDoc> getBorrowers() {
        return borrowers;
    }

    public void setBorrowers(List<BorrowerDoc> borrowers) {
        this.borrowers = borrowers;
    }

	public List<SelfNotes> getSelfnotes() {
		return selfnotes;
	}

	public void setSelfnotes(List<SelfNotes> selfnotes) {
		this.selfnotes = selfnotes;
	}

	public List<ExpenseTracker> getExpenseTracker() {
		return expenseTracker;
	}

	public void setExpenseTracker(List<ExpenseTracker> expenseTracker) {
		this.expenseTracker = expenseTracker;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
    
    
}


