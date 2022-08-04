package com.example.mscustomer.error;

public class InvalidCustomerTypeException extends Exception {

  public InvalidCustomerTypeException() {
    super("This type of customer is invalid");
  }

}
