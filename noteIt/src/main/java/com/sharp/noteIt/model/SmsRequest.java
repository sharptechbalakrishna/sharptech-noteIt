package com.sharp.noteIt.model;

public class SmsRequest {
	 private String phoneNumber;
	    private String message;
		public String getPhoneNumber() {
			return phoneNumber;
		}
		public void setPhoneNumber(String phoneNumber) {
			this.phoneNumber = phoneNumber;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		
		 @Override
		    public String toString() {
		        return "SmsRequest{phoneNumber='" + phoneNumber + "', message='" + message + "'}";
		    }

}
