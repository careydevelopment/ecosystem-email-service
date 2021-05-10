package com.careydevelopment.ecosystem.email.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.careydevelopment.ecosystem.email.model.User;
import com.careydevelopment.ecosystem.email.repository.UserRepository;

@Component
public class ApplicationListenerInitialize implements ApplicationListener<ApplicationReadyEvent>  {
    
    @Autowired
    private UserRepository userRep;
    
    public void onApplicationEvent(ApplicationReadyEvent event) {
    }
}