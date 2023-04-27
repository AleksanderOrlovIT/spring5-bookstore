package com.example.bookstore.controllers;

import com.example.bookstore.model.Customer;
import com.example.bookstore.service.CustomerService;
import com.example.bookstore.service.RoleService;
import com.example.bookstore.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class IndexControllerTest {

    @Mock
    CustomerService customerService;

    @Mock
    RoleService roleService;

    @InjectMocks
    IndexController indexController;

    MockMvc mockMvc;
    Customer customer;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(indexController).build();

        customer = Customer.builder()
            .userName("costumer")
            .password("123").build();
    }

    @Test
    void getHomePage() throws Exception {
        mockMvc.perform(get("/homePage"))
                .andExpect(status().isOk())
                .andExpect(view().name("administratorhomepage"));
    }

    @Test
    void registerCustumer() throws Exception {
        mockMvc.perform(get("/register"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("customer"))
            .andExpect(view().name("/customer/customerform"));

        verifyNoInteractions(customerService);
        verifyNoInteractions(roleService);
    }

    @Test
    void processCreationForm() throws Exception {
        mockMvc.perform(post("/register")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("username", "customer")
            .param("password", "123"))
            .andExpect(status().isOk())
            .andExpect(view().name("/customer/customerform"));
    }

    @Test
    void getFirstPage() throws Exception {
        mockMvc.perform(get("/"))
        .andExpect(status().isOk())
        .andExpect(view().name("firstpage"));
    }

    @Test
    void returnLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
        .andExpect(status().isOk())
        .andExpect(view().name("loginPage"));
    }
}