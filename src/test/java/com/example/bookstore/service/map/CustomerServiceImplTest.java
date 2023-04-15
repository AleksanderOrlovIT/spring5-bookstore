package com.example.bookstore.service.map;

import com.example.bookstore.model.Customer;
import com.example.bookstore.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTest {

    public static final String customerName = "Customer";

    public static final Long customerId = 1L;

    @Mock
    CustomerRepository customerRepository;

    @InjectMocks
    CustomerServiceImpl customerService;

    Customer returnCustomer;

    @BeforeEach
    void setUp() {
        returnCustomer = Customer.builder().id(customerId).userName(customerName).build();
    }

    @Test
    void findAll() {
        Set<Customer> returnCustomers = new HashSet<>();
        returnCustomers.add(Customer.builder().id(1L).build());
        returnCustomers.add(Customer.builder().id(2L).build());

        when(customerRepository.findAll()).thenReturn(returnCustomers);

        Set<Customer> savedCustomers = customerService.findAll();

        assertNotNull(savedCustomers);
        assertEquals(2, savedCustomers.size());
    }

    @Test
    void findById() {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(returnCustomer));

        Customer customer = customerService.findById(customerId);

        assertNotNull(customer);
    }

    @Test
    void findByIdNotFound() {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());

        Customer customer = customerService.findById(customerId);

        assertNull(customer);
    }

    @Test
    void saveWithId() {
        when(customerRepository.save(any())).thenReturn(returnCustomer);

        Customer customer = customerService.save(Customer.builder().id(1L).build());

        assertNotNull(customer);
        verify(customerRepository).save(any());
    }

    @Test
    void saveWithoutId(){
        when(customerRepository.save(any())).thenReturn(returnCustomer);

        Customer customer = customerService.save(Customer.builder().build());

        assertNotNull(customer);
        verify(customerRepository).save(any());
    }

    @Test
    void delete() {
        customerService.delete(returnCustomer);
        verify(customerRepository, times(1)).delete(any());
    }

    @Test
    void deleteById() {
        customerService.deleteById(customerId);
        verify(customerRepository).deleteById(anyLong());
    }
}
