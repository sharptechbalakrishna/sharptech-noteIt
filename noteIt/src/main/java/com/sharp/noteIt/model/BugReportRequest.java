package com.sharp.noteIt.model;

public class BugReportRequest {
	private String email;
    private String bugMessage;
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getBugMessage() {
		return bugMessage;
	}
	public void setBugMessage(String bugMessage) {
		this.bugMessage = bugMessage;
	}
    

}
