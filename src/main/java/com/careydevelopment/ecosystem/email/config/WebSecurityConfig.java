package com.careydevelopment.ecosystem.email.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import us.careydevelopment.ecosystem.jwt.config.JwtOnlySecurityConfig;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends JwtOnlySecurityConfig {

    public WebSecurityConfig(@Autowired JwtAuthenticationProvider jwtAuthenticationProvider) {
        this.authenticationProvider = jwtAuthenticationProvider;
    }
}
