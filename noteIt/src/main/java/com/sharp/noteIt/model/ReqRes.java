package com.sharp.noteIt.model;



public class ReqRes {

    private int statusCode;
    private String message;

    // Default constructor
    public ReqRes() {
    }

    // Parameterized constructor
    public ReqRes(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    // Getters and Setters

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ReqRes{" +
                "statusCode=" + statusCode +
                ", message='" + message + '\'' +
                '}';
    }
}
