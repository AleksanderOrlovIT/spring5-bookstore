package com.example.bookstore.service.map;

import com.example.bookstore.model.Customer;
import com.example.bookstore.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CustomerServiceTest {

    Long customerId = 1L;

    CustomerService customerService;

    @BeforeEach
    void setUp() {
        customerService = new CustomerServiceImpl();
        customerService.save(Customer.builder().build());
    }

    @Test
    void findAll() {
        assertEquals(1, customerService.findAll().size());
    }

    @Test
    void findById() {
        Customer customer = customerService.findById(customerId);
        assertEquals(customer.getId(), customerId);
    }

    @Test
    void saveWithId() {
        Long savedId = 2L;
        Customer customer = Customer.builder().id(savedId).build();

        Customer savedCustomer = customerService.save(customer);
        assertEquals(savedCustomer.getId(), savedId);
    }

    @Test
    void saveWithoutId(){
        Customer customer = customerService.save(Customer.builder().build());

        assertNotNull(customer);
        assertNotNull(customer.getId());
    }

    @Test
    void delete() {
        customerService.delete(customerService.findById(customerId));
        assertEquals(0, customerService.findAll().size());
    }

    @Test
    void deleteById() {
        customerService.deleteById(customerId);
        assertEquals(0, customerService.findAll().size());
    }
}
