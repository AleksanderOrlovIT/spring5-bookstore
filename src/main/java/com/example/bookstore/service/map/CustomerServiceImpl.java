package com.example.bookstore.service.map;

import com.example.bookstore.model.Customer;
import com.example.bookstore.service.CustomerService;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CustomerServiceImpl extends AbstractMapService<Customer, Long> implements CustomerService {
    @Override
    public Set<Customer> findAll() {
        return super.findAll();
    }

    @Override
    public Customer findById(Long id) {
        return super.findById(id);
    }

    @Override
    public Customer save(Customer customer) {
        return super.save(customer);
    }

    @Override
    public void delete(Customer customer) {
        super.delete(customer);
    }

    @Override
    public void deleteById(Long id) {
        super.deleteById(id);
    }
}
