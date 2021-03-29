package com.careydevelopment.ecosystem.email.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationListenerInitialize implements ApplicationListener<ApplicationReadyEvent>  {
    
        
    public void onApplicationEvent(ApplicationReadyEvent event) {
    }
}