package com.example.bookstore.service;

import com.example.bookstore.model.Customer;

public interface CustomerService extends CrudService<Customer, Long>{
    public Customer findByUserName(String userName);
}
