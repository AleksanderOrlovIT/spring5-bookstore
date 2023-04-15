package com.example.bookstore.controllers;

import com.example.bookstore.service.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @RequestMapping({"/customers", "/customers/show"})
    public String getCustomers(Model model){
        model.addAttribute("customers", customerService.findAll());
        return "customer/index";
    }
}
