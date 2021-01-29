package com.careydevelopment.customer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.careydevelopment.customer.util.PropertiesUtil;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;


@Configuration
@EnableCaching
@EnableMongoRepositories(basePackages = {"com.careydevelopment.customer.repository"})
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Value("${mongo.db.name}") 
    private String contactDb;
    
    @Value("${ecosystem.properties.file.location}")
    private String ecosystemPropertiesFile;
    
    @Override
    protected String getDatabaseName() {
        return contactDb;
    }
  
    
    @Override
    @Bean
    public MongoClient mongoClient() {
        PropertiesUtil propertiesUtil = new PropertiesUtil(ecosystemPropertiesFile);
        String connectionString = propertiesUtil.getProperty("mongodb.carey-customer.connection");
        String fullConnectionString = connectionString + "/" + contactDb;
        
        MongoClient client = MongoClients.create(fullConnectionString);
        return client;
    }
}
