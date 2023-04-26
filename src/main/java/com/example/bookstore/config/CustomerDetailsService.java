package com.example.bookstore.config;

import com.example.bookstore.model.Customer;
import com.example.bookstore.model.Role;
import com.example.bookstore.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class CustomerDetailsService implements UserDetailsService {

    private CustomerService customerService;

    public CustomerDetailsService(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    public CustomerDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = customerService.findByUserName(username);
        if (customer == null) {
            throw new UsernameNotFoundException("Customer not found");
        }
        return new CustomerDetails(customer);
    }
}
