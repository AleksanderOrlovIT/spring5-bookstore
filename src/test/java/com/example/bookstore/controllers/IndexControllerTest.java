package com.example.bookstore.controllers;

import com.example.bookstore.service.CustomerService;
import com.example.bookstore.service.RoleService;
import com.example.bookstore.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(indexController).build();
    }

    @Test
    void getHomePage() throws Exception {
        mockMvc.perform(get("/homePage"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }
}