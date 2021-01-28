package com.careydevelopment.contact;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.careydevelopment.contact","com.careydevelopment.ecosystem.jwt"})
public class CustomerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerApplication.class,args);
    }
}
