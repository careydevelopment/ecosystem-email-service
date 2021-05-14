package com.careydevelopment.ecosystem.email.service;

public class GoogleApiException extends Exception {

    private static final long serialVersionUID = 100398469691128775L;

    private String error;
    private int statusCode;
    
    public GoogleApiException(String error, String errorDescription, int statusCode) {
        super(errorDescription);
        
        this.error = error;
        this.statusCode = statusCode;
    }
    
    
    public String getError() {
        return error;
    }
    
    
    public int getStatusCode() {
        return statusCode;
    }
}
