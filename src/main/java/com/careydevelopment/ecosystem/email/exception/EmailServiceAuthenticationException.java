package com.careydevelopment.ecosystem.email.exception;

import org.springframework.security.core.AuthenticationException;

public class EmailServiceAuthenticationException extends AuthenticationException {
    
    private static final long serialVersionUID = 6356844005269578058L;

    public EmailServiceAuthenticationException(String s) {
        super(s);
    }
}
