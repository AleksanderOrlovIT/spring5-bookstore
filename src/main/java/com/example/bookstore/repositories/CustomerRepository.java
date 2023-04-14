package com.example.bookstore.repositories;

import com.example.bookstore.model.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
}
