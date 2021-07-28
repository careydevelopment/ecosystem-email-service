package com.careydevelopment.ecosystem.email.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.careydevelopment.ecosystem.email.util.JwtUtil;

import us.careydevelopment.ecosystem.jwt.config.JwtOnlyAuthenticationProvider;

@Component
public class JwtAuthenticationProvider extends JwtOnlyAuthenticationProvider {

    private static final Logger LOG = LoggerFactory.getLogger(JwtAuthenticationProvider.class);

    
    public JwtAuthenticationProvider(@Autowired JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }
}
