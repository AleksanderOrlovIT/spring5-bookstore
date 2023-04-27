package com.example.bookstore.service.impl;

import com.example.bookstore.model.Book;
import com.example.bookstore.model.Customer;
import com.example.bookstore.repositories.CustomerRepository;
import com.example.bookstore.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

@Service
public class CustomerServiceImpl implements CustomerService {

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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
        if (customer != null) {
            encodePassword(customer);
            return customerRepository.save(customer);
        } else
            return null;
    }

    @Override
    public void delete(Customer customer) {
        if (customer != null && customerRepository.findById(customer.getId()).isPresent()) {
            removeBeforeDelete(customer);
            customerRepository.delete(customer);
        }
    }

    @Override
    public void deleteById(Long id) {
        Customer customer = findById(id);
        if (customer != null) {
            removeBeforeDelete(customer);
            customerRepository.deleteById(id);
        }
    }

    public void removeBeforeDelete(Customer customer) {
        Set<Book> books = customer.getBooks();
        if (books != null) {
            for (Book book : books) {
                book.getCustomers().remove(customer);
            }
        }
    }

    @Override
    public Customer findByUserName(String userName) {
        for (Customer customer : customerRepository.findAll()) {
            if (customer.getUserName().equals(userName)) {
                return customer;
            }
        }
        return null;
    }

    private void encodePassword(Customer customer) {
        Pattern BCRYPT_PATTERN = Pattern.compile("\\A\\$2a?\\$\\d\\d\\$[./0-9A-Za-z]{53}");
        if (findByUserName(customer.getUserName()) == null && !BCRYPT_PATTERN.matcher(customer.getPassword()).matches()) {
            String encodedPassword = passwordEncoder.encode(customer.getPassword());
            customer.setPassword(encodedPassword);
        } else {
            Customer foundCustomer = findById(customer.getId());
            String encodedPassword = passwordEncoder.encode(customer.getPassword());
            if (!passwordEncoder.matches(foundCustomer.getPassword(), encodedPassword)
                    && !passwordEncoder.matches(foundCustomer.getPassword(), customer.getPassword()))
                customer.setPassword(encodedPassword);
        }
    }

    @Override
    public Customer copyOldCustomerDataInNewOne(Customer newCustomer, Customer oldCustomer) {
        if(newCustomer != null && oldCustomer != null){
            newCustomer.setBooks(oldCustomer.getBooks());
            newCustomer.setRoles(oldCustomer.getRoles());
            newCustomer.setImage(oldCustomer.getImage());
            return newCustomer;
        }
        return null;
    }
}