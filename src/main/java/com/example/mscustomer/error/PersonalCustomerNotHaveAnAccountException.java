package com.example.mscustomer.error;

public class PersonalCustomerNotHaveAnAccountException extends Exception {
    public PersonalCustomerNotHaveAnAccountException() {
        super("Personal customer must have an account");
    }
}
