package com.example.bookstore.service.impl;

import com.example.bookstore.model.Book;
import com.example.bookstore.model.Customer;
import com.example.bookstore.repositories.CustomerRepository;
import com.example.bookstore.service.CustomerService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Set<Customer> findAll() {
        Set<Customer> customers = new HashSet<>();
        customerRepository.findAll().forEach(customers::add);
        return customers;
    }

    @Override
    public Customer findById(Long id) {
        return customerRepository.findById(id).orElse(null);
    }

    @Override
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public void delete(Customer customer) {
        customerRepository.delete(customer);
    }

    @Override
    public void deleteById(Long id) {
        Customer customer = findById(id);
        if(customer != null && customer.getBooks() != null){
            for(Book book : customer.getBooks()){
                book.getCustomers().remove(customer);
            }
        }
        customerRepository.deleteById(id);
    }

    @Override
    public Customer findByUserName(String userName) {
        for(Customer customer : customerRepository.findAll()){
            if(customer.getUserName().equals(userName))
                return customer;
        }
        return null;
    }
}
