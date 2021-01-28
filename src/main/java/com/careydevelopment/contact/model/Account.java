package com.careydevelopment.contact.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "#{@environment.getProperty('mongo.account.collection')}")
public class Account {

    @Id
    private String id;
    
    @NotBlank(message = "Please provide a business name")
    @Size(max = 50, message = "Business name must be between 1 and 50 characters")
    private String name;
    
    private Address address;
    private Phone phone;
    private Industry industry;
    
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
    public Address getAddress() {
        return address;
    }
    public void setAddress(Address address) {
        this.address = address;
    }
    public Phone getPhone() {
        return phone;
    }
    public void setPhone(Phone phone) {
        this.phone = phone;
    }
    public Industry getIndustry() {
        return industry;
    }
    public void setIndustry(Industry industry) {
        this.industry = industry;
    }
    
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
    
}
