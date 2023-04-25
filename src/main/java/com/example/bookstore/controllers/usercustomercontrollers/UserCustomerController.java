package com.example.bookstore.controllers.usercustomercontrollers;

import com.example.bookstore.service.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserCustomerController {

    private final CustomerService customerService;

    public UserCustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @RequestMapping("/user")
    public String getCustomerHomePage(){
        return "user/index";
    }
}
