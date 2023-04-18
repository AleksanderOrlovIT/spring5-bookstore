package com.example.bookstore.controllers.doublecontrollers;

import com.example.bookstore.model.Book;
import com.example.bookstore.model.Customer;
import com.example.bookstore.service.BookService;
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
@RequestMapping("/book/{bookId}")
public class BookCustomersController {
    private final static String bookCustomerForm = "book/bookcustomers/bookcustomerform";

    private final BookService bookService;

    private final CustomerService customerService;

    private Book currentBook;

    public BookCustomersController(BookService bookService, CustomerService customerService) {
        this.bookService = bookService;
        this.customerService = customerService;
    }

    @RequestMapping({"/customers", "customers/show"})
    public String getAllBookCustomers(@PathVariable Long bookId, Model model){
        currentBook = bookService.findById(bookId);
        model.addAttribute("customers", currentBook.getCustomers());
        model.addAttribute("book", currentBook);
        return "book/bookcustomers/index";
    }

    @GetMapping("/customer/new")
    public String initCreationForm(Model model){
        model.addAttribute("customer", Customer.builder().build());
        return bookCustomerForm;
    }

    @PostMapping("/customer/new")
    public String processCreationForm(@Valid Customer customer, BindingResult result, @PathVariable Long bookId) {
        if (result.hasErrors()) {
            return bookCustomerForm;
        } else {
            Customer foundCustomer = customerService.findByUserName(customer.getUserName());
            currentBook = bookService.findById(bookId);

            if (foundCustomer != null) {
                foundCustomer.setBalance(customer.getBalance());
                customer = foundCustomer;
            }
            customer.getBooks().add(currentBook);
            currentBook.getCustomers().add(customer);

            customerService.save(customer);
            bookService.save(currentBook);
            return "redirect:/book/" + bookId + "/customers";
        }
    }

    @RequestMapping("/customer/{customerId}/delete")
    public String deleteAuthor(@PathVariable Long customerId, @PathVariable Long bookId){
        Customer deletedCustomer = customerService.findById(customerId);
        currentBook = bookService.findById(bookId);

        currentBook.getCustomers().remove(deletedCustomer);
        deletedCustomer.getBooks().remove(currentBook);

        customerService.save(deletedCustomer);
        bookService.save(currentBook);
        return "redirect:/book/" + bookId + "/customers";
    }
}
