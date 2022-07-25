package com.example.mscustomer.enums;

public enum CustomerTypeEnum {
    PERSONNEL("PERSONAL"),
    BUSINESS("BUSINESS");
    private String customerType;
    CustomerTypeEnum(String customerType){
        this.customerType = customerType;
    }

    public String getCustomerType(){
        return customerType;
    }
}
