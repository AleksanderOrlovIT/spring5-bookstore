package com.example.bookstore.config;

import com.example.bookstore.model.Customer;
import com.example.bookstore.model.Role;
import com.example.bookstore.service.CustomerService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class CustomerDetailsService implements UserDetailsService {

    private CustomerService customerService;

    public CustomerDetailsService(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    public CustomerDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(username.equals("admin")){
            Set<Role> roles = new HashSet<>();
            roles.add(Role.builder().name("ADMIN").build());
            BCryptPasswordEncoder cryptPasswordEncoder = new BCryptPasswordEncoder();
            return new CustomerDetails(Customer.builder().userName("admin").password(cryptPasswordEncoder.encode("admin"))
                    .balance(BigDecimal.valueOf(10.0)).roles(roles).build());
        }
        Customer customer = customerService.findByUserName(username);
        if (customer == null) {
            throw new UsernameNotFoundException("Customer not found");
        }
        return new CustomerDetails(customer);
    }

}