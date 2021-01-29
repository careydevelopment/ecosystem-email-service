package com.careydevelopment.ecosystem.product.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "#{@environment.getProperty('mongo.product.collection')}")
public class Product {

    @Id
    private String id;
    
    @NotBlank(message = "Please provide a product name")
    @Size(max = 50, message = "Product name must be between 1 and 50 characters")
    private String name;

    @Size(max = 255, message = "Product must not exceed 255 characters")
    private String description;
    
    @NotNull(message = "Please include a product line of business")
    private LineOfBusiness lineOfBusiness;
    
    @NotNull(message = "Please provide a product type")
    private ProductType productType;

    private List<Price> prices = new ArrayList<>();
    
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LineOfBusiness getLineOfBusiness() {
        return lineOfBusiness;
    }

    public void setLineOfBusiness(LineOfBusiness lineOfBusiness) {
        this.lineOfBusiness = lineOfBusiness;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }
    
    public List<Price> getPrices() {
        return prices;
    }

    public void setPrices(List<Price> prices) {
        this.prices = prices;
    }

    public void addPrice(Price price) {
        prices.add(price);
    }
    
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
