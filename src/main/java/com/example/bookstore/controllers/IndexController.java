package com.example.bookstore.controllers;

import com.example.bookstore.model.Customer;
import com.example.bookstore.service.CustomerService;
import com.example.bookstore.service.RoleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
public class IndexController {
    private final static String customerForm = "/customer/customerform";

    private final CustomerService customerService;

    private final RoleService roleService;

    public IndexController(CustomerService customerService, RoleService roleService) {
        this.customerService = customerService;
        this.roleService = roleService;
    }

    @RequestMapping("/homepage")
    public String getHomePage(){
        return "index";
    }


    @GetMapping("/register")
    public String registerCustomer(Model model){
        model.addAttribute("customer", Customer.builder().build());

        return customerForm;
    }

    @PostMapping("/register")
    public String processCreationForm(@Valid Customer customer, BindingResult result){
        if(result.hasErrors()){
            return customerForm;
        }else{
            customer.getRoles().add(roleService.findByName("CustomerRole"));
            Customer savedCustomer = customerService.save(customer);
            return "redirect:/user/" + savedCustomer.getId() + "/homePage";
        }
    }

    @RequestMapping("/")
    public String getFirstPage(){
        return "firstpage";
    }
}
