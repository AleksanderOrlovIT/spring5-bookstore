package com.example.bookstore.controllers;

import com.example.bookstore.service.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
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

    @RequestMapping("/customer/{id}/show")
    public String getCustomerById(@PathVariable String id, Model model){
        model.addAttribute("customer", customerService.findById(Long.valueOf(id)));
        return "customer/show";
    }
}
