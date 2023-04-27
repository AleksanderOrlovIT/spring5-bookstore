package com.example.bookstore.controllers;

import com.example.bookstore.model.Customer;
import com.example.bookstore.service.CustomerService;
import com.example.bookstore.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

    private static final String customerForm = "/customer/customerform";

    private static final String errorPage = "/error/400error";

    @Mock
    CustomerService customerService;

    @Mock
    RoleService roleService;

    @InjectMocks
    CustomerController customerController;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
    }

    @Test
    void getCustomers() throws Exception{
        Set<Customer> customers = new HashSet<>();
        customers.add(Customer.builder().id(1L).build());
        customers.add(Customer.builder().id(2L).build());
        when(customerService.findAll()).thenReturn(customers);

        mockMvc.perform(get("/customers"))
                .andExpect(status().isOk())
                .andExpect(view().name("customer/index"))
                .andExpect(model().attribute("customers", hasSize(2)));

        verify(customerService, times(1)).findAll();
    }

    @Test
    void showCustomerById() throws Exception{
        when(customerService.findById(anyLong())).thenReturn(Customer.builder().id(1L).build());

        mockMvc.perform(get("/customer/1/show"))
                .andExpect(status().isOk())
                .andExpect(view().name("customer/show"))
                .andExpect(model().attribute("customer", hasProperty("id", is(1L))));

        verify(customerService, times(1)).findById(anyLong());
    }

    @Test
    void showCustomerByIdWithBrokenId() throws Exception{
        mockMvc.perform(get("/customer/1/delete"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("exception"))
                .andExpect(view().name(errorPage));

        verify(customerService, times(1)).findById(anyLong());
    }

    @Test
    void initCreationForm() throws Exception{
        mockMvc.perform(get("/customer/new"))
                .andExpect(status().isOk())
                .andExpect(view().name(customerForm))
                .andExpect(model().attributeExists("customer"));

        verifyNoInteractions(customerService);
    }

    @Test
    void processCreationForm() throws  Exception{
        Customer customer = Customer.builder().id(1L).userName("userName").password("pass").balance(BigDecimal.valueOf(1.0)).build();
        when(customerService.save(any())).thenReturn(customer);

        mockMvc.perform(post("/customer/new")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("userName", customer.getUserName())
                        .param("balance", customer.getBalance().toString())
                        .param("password", customer.getPassword()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/customer/" + customer.getId() + "/show"));

        verify(customerService, times(1)).save(any());
    }

    @Test
    void processCreationFormWithoutParams() throws Exception{
        mockMvc.perform(post("/customer/new"))
                .andExpect(status().isOk())
                .andExpect(view().name(customerForm));

        verifyNoInteractions(customerService);
    }

    @Test
    void initUpdateCustomerForm() throws Exception{
        when(customerService.findById(anyLong())).thenReturn(Customer.builder().id(1L).build());

        mockMvc.perform(get("/customer/1/update"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("customer"))
                .andExpect(view().name(customerForm));

        verify(customerService, times(1)).findById(anyLong());
    }

    @Test
    void initUpdateCustomerFormWIthBrokenId() throws Exception{
        mockMvc.perform(get("/customer/1/update"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("exception"))
                .andExpect(view().name(errorPage));

        verify(customerService, times(1)).findById(anyLong());
    }

    @Test
    void processUpdateCustomerForm() throws Exception{
        Customer customer = Customer.builder().id(1L).userName("userName").password("pass").balance(BigDecimal.valueOf(1.0)).build();
        when(customerService.save(any())).thenReturn(customer);
        when(customerService.findById(anyLong())).thenReturn(customer);

        mockMvc.perform(post("/customer/1/update")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("userName", customer.getUserName())
                        .param("balance", customer.getBalance().toString())
                        .param("password", customer.getPassword()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/customer/" + customer.getId() + "/show"));

        verify(customerService, times(1)).save(any());
        verify(customerService, times(1)).findById(anyLong());
    }

    @Test
    void processUpdateAuthorFormWithoutParams() throws Exception{
        when(customerService.findById(anyLong())).thenReturn(Customer.builder().build());

        mockMvc.perform(post("/customer/1/update"))
                .andExpect(status().isOk())
                .andExpect(view().name(customerForm));
        verify(customerService, times(1)).findById(anyLong());
    }

    @Test
    void processUpdateCustomerFormWithBrokenId() throws Exception{
        mockMvc.perform(post("/customer/1/update"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("exception"))
                .andExpect(view().name(errorPage));
        verify(customerService, times(1)).findById(anyLong());
    }


    @Test
    void deleteCustomer() throws Exception{
        when(customerService.findById(anyLong())).thenReturn(Customer.builder().build());

        mockMvc.perform(get("/customer/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/customers"));

        verify(customerService).deleteById(anyLong());
    }

    @Test
    void deleteCustomerWIthBrokenID() throws Exception{
        mockMvc.perform(get("/customer/1/delete"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("exception"))
                .andExpect(view().name(errorPage));
        verify(customerService, times(1)).findById(anyLong());
    }
}