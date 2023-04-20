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
        if(customer != null)
            return customerRepository.save(customer);
        else
            return null;
    }

    @Override
    public void delete(Customer customer) {
        if(customer != null && customerRepository.findById(customer.getId()).isPresent()){
            removeBeforeDelete(customer);
            customerRepository.delete(customer);
        }
    }

    @Override
    public void deleteById(Long id) {
        Customer customer = findById(id);
        if(customer != null){
            removeBeforeDelete(customer);
            customerRepository.deleteById(id);
        }
    }

    public void removeBeforeDelete(Customer customer){
        Set<Book> books = customer.getBooks();
        if(books != null){
            for(Book book : books){
                book.getCustomers().remove(customer);
            }
        }
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