package com.careydevelopment.ecosystem.product.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class Price {

    private PriceType priceType;
    private UnitType unitType;
    private CurrencyType currencyType;
    private Integer amount;
    
    public PriceType getPriceType() {
        return priceType;
    }
    public void setPriceType(PriceType priceType) {
        this.priceType = priceType;
    }
    public UnitType getUnitType() {
        return unitType;
    }
    public void setUnitType(UnitType unitType) {
        this.unitType = unitType;
    }
    public CurrencyType getCurrencyType() {
        return currencyType;
    }
    public void setCurrencyType(CurrencyType currencyType) {
        this.currencyType = currencyType;
    }
    public Integer getAmount() {
        return amount;
    }
    public void setAmount(Integer amount) {
        this.amount = amount;
    }
    
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
