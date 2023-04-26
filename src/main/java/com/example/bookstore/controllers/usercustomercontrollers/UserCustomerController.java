package com.example.bookstore.controllers.usercustomercontrollers;

import com.example.bookstore.config.CustomerDetails;
import com.example.bookstore.service.CustomerService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserCustomerController {

    private final CustomerService customerService;

    public UserCustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @RequestMapping("/user/{userId}/homePage")
    public String getCustomerHomePage(){
        return "user/index";
    }

    @RequestMapping("/findpath")
    public String getCustomerId(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails){
            String userName = ((UserDetails) principal).getUsername();
            if(userName.equals("admin"))
                return "redirect:/homepage";
            return "redirect:/user/" + customerService.findByUserName(userName).getId() + "/homePage";
        } else return "/error/404error";
    }
}
