package com.careydevelopment.ecosystem.product.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.careydevelopment.ecosystem.product.util.PropertiesUtil;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;


@Configuration
@EnableCaching
@EnableMongoRepositories(basePackages = {"com.careydevelopment.ecosystem.product.repository"})
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Value("${mongo.db.name}") 
    private String productDb;
    
    @Value("${ecosystem.properties.file.location}")
    private String ecosystemPropertiesFile;
    
    @Override
    protected String getDatabaseName() {
        return productDb;
    }
  
    
    @Override
    @Bean
    public MongoClient mongoClient() {
        PropertiesUtil propertiesUtil = new PropertiesUtil(ecosystemPropertiesFile);
        String connectionString = propertiesUtil.getProperty("mongodb.carey-product.connection");
        String fullConnectionString = connectionString + "/" + productDb;
        
        MongoClient client = MongoClients.create(fullConnectionString);
        return client;
    }
}
