package com.careydevelopment.customer.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "#{@environment.getProperty('mongo.account.collection')}")
public class AccountLightweight {

    @Id
    private String id;
    
    @NotBlank(message = "Please provide a business name")
    @Size(max = 50, message = "Business name must be between 1 and 50 characters")
    private String name;
        
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
    
}
