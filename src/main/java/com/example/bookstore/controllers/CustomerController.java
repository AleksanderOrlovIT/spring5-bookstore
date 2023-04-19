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

    private final static String customerForm = "/customer/customerform";

    private final static String errorPage = "/error/400error";

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
    public String showCustomerById(@PathVariable Long id, Model model){
        Customer customer = customerService.findById(id);
        if(customer == null){
            model.addAttribute("exception", new Exception("There is no customer with id: " + id));
            return errorPage;
        }
        model.addAttribute("customer", customer);
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
        Customer customer = customerService.findById(id);
        if(customer == null){
            model.addAttribute("exception", new Exception("There is no customer with id: " + id));
            return errorPage;
        }
        model.addAttribute(customer);
        return customerForm;
    }

    @PostMapping("/customer/{id}/update")
    public String processUpdateCustomerForm(@Valid Customer customer, BindingResult result, @PathVariable Long id,
                                            Model model){
        if(customerService.findById(id) == null){
            model.addAttribute("exception", new Exception("There is no customer with id: " + id));
            return errorPage;
        }
        if(result.hasErrors()){
            return customerForm;
        }else{
            customerService.save(customer);
            return "redirect:/customer/" + customer.getId() + "/show";
        }
    }

    @RequestMapping("customer/{id}/delete")
    public String deleteCustomer(@PathVariable Long id, Model model){
        if(customerService.findById(id) == null){
            model.addAttribute("exception", new Exception("There is no customer with id: " + id));
            return errorPage;
        }
        customerService.deleteById(id);
        return "redirect:/customers";
    }
}
