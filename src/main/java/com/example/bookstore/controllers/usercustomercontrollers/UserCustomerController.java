package com.example.bookstore.controllers.usercustomercontrollers;

import com.example.bookstore.model.Customer;
import com.example.bookstore.service.CustomerService;
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
    public String getCustomerHomePage(Model model, @PathVariable Long userId) {
        if(checkIfUserIdIsAuthorized(userId)) {
            Customer customer = customerService.findById(userId);
            if (customer == null) {
                model.addAttribute("exception", new Exception("There is no customer with id: " + userId));
                return errorPage;
            }
            model.addAttribute("user", customer);
            return "user/index";
        }else {
            model.addAttribute("exception", new Exception("You didn't authorize with id: " + userId));
            return errorPage;
        }
    }

    @RequestMapping("/findPath")
    public String getCustomerId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String userName = ((UserDetails) principal).getUsername();
            if (userName.equals("admin"))
                return "redirect:/homePage";
            return "redirect:/user/" + customerService.findByUserName(userName).getId() + "/homePage";
        } else return "/error/404error";
    }

    @RequestMapping("user/{userId}/myProfile")
    public String getUserProfile(Model model, @PathVariable Long userId) {
        if(checkIfUserIdIsAuthorized(userId)) {
            Customer customer = customerService.findById(userId);
            if (customer == null) {
                model.addAttribute("exception", new Exception("There is no customer with id: " + userId));
                return errorPage;
            }
            model.addAttribute("user", customer);
            return "user/show";
        }else {
            model.addAttribute("exception", new Exception("You didn't authorize with id: " + userId));
            return errorPage;
        }
    }

    @RequestMapping("user/{userId}/books")
    public String getUserBooks(Model model, @PathVariable Long userId) {
        if(checkIfUserIdIsAuthorized(userId)) {
            Customer customer = customerService.findById(userId);
            if (customer == null) {
                model.addAttribute("exception", new Exception("There is no customer with id: " + userId));
                return errorPage;
            }
            model.addAttribute("user", customer);
            return "user/books";
        } else {
            model.addAttribute("exception", new Exception("You didn't authorize with id: " + userId));
            return errorPage;
        }
    }

    public boolean checkIfUserIdIsAuthorized(Long userId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String userName = ((UserDetails) principal).getUsername();
            return userId.equals(customerService.findByUserName(userName).getId());
        }
        return false;
    }
}
