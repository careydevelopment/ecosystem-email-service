package com.careydevelopment.ecosystem.email.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
@EnableCaching
@EnableMongoRepositories(basePackages= {"com.careydevelopment.ecosystem.email.repository"})
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Value("${mongodb.carey-ecosystem.connection}")
    private String connectionString;
    
    @Override
    protected String getDatabaseName() {
        return "ecosystem";
    }
  
    
    @Override
    @Bean
    public MongoClient mongoClient() {
        String fullConnectionString = connectionString + "/" + getDatabaseName();
        
        MongoClient client = MongoClients.create(fullConnectionString);
        return client;
    }

}
