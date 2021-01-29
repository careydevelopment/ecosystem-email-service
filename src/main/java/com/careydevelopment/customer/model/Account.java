package com.careydevelopment.customer.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class Account extends AccountLightweight {

    private Address address;
    private Phone phone;
    private Industry industry;
    
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
