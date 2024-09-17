package com.sharp.noteIt.model;

public class BugReportRequest {
	private String email;
    private String bugMessage;
    private String title;
    
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
    

}
