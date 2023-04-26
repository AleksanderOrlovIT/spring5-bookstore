package com.example.bookstore.controllers.usercustomercontrollers;

import com.example.bookstore.config.CustomerDetails;
import com.example.bookstore.model.Customer;
import com.example.bookstore.service.CustomerService;
import org.apache.logging.log4j.message.LoggerNameAwareMessage;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserCustomerController {

    private final static String errorPage = "/error/400error";

    private final CustomerService customerService;

    public UserCustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @RequestMapping("/user/{userId}/homePage")
    public String getCustomerHomePage(Model model, @PathVariable Long userId){
        Customer customer = customerService.findById(userId);
        if(customer == null){
            model.addAttribute("exception", new Exception("There is no customer with id: " + userId));
            return errorPage;
        }
        model.addAttribute("user", customer);
        return "user/index";
    }

    @RequestMapping("/findPath")
    public String getCustomerId(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails){
            String userName = ((UserDetails) principal).getUsername();
            if(userName.equals("admin"))
                return "redirect:/homePage";
            return "redirect:/user/" + customerService.findByUserName(userName).getId() + "/homePage";
        } else return "/error/404error";
    }
}
