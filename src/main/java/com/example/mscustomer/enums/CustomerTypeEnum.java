package com.example.mscustomer.enums;

public enum CustomerTypeEnum {
  PERSONNEL("PERSONNEL"),
  BUSINESS("BUSINESS");
  private String value;

  CustomerTypeEnum(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
