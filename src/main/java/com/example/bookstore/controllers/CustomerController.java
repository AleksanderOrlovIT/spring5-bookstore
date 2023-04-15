package com.example.bookstore.controllers;

import com.example.bookstore.model.Customer;
import com.example.bookstore.service.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
public class CustomerController {

    private final static String customerForm = "customer/customerform";

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

    @GetMapping("/customer/new")
    public String initCreationForm(Model model){
        model.addAttribute("customer", Customer.builder().build());
        return customerForm;
    }

    @PostMapping("/customer/new")
    public String processCreationForm(@Valid Customer customer, BindingResult result){
        if(result.hasErrors()){
            return customerForm;
        }else{
            Customer savedCustomer = customerService.save(customer);
            return "redirect:/customer/" + savedCustomer.getId() + "/show";
        }
    }

    @GetMapping("/customer/{id}/update")
    public String initUpdateCustomerForm(@PathVariable Long id, Model model){
        model.addAttribute(customerService.findById(id));
        return customerForm;
    }

    @PostMapping("/customer/{id}/update")
    public String processUpdateCustomerForm(@Valid Customer customer, BindingResult result, @PathVariable Long id){
        if(result.hasErrors()){
            return customerForm;
        }else{
            customerService.save(customer);
            return "redirect:/customer/" + customer.getId() + "/show";
        }
    }
}
