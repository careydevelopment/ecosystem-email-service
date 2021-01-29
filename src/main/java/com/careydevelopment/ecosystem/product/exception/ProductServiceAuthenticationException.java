package com.careydevelopment.ecosystem.product.exception;

import org.springframework.security.core.AuthenticationException;

public class ProductServiceAuthenticationException extends AuthenticationException {
    
    private static final long serialVersionUID = 6356844005269578058L;

    public ProductServiceAuthenticationException(String s) {
        super(s);
    }
}
