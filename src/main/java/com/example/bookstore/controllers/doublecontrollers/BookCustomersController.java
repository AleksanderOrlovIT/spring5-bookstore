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

    private final static String errorPage = "/error/400error";

    private final BookService bookService;

    private final CustomerService customerService;

    private Book currentBook;

    public BookCustomersController(BookService bookService, CustomerService customerService) {
        this.bookService = bookService;
        this.customerService = customerService;
    }

    @RequestMapping({"/customers", "customers/show"})
    public String getAllBookCustomers(@PathVariable Long bookId, Model model){
        if(checkIfBookIdIsWrong(bookId, model))
            return errorPage;
        currentBook = bookService.findById(bookId);
        model.addAttribute("customers", currentBook.getCustomers());
        model.addAttribute("book", currentBook);
        return "book/bookcustomers/index";
    }

    @GetMapping("/customer/new")
    public String initCreationForm(Model model, @PathVariable Long bookId){
        if(checkIfBookIdIsWrong(bookId, model))
            return errorPage;
        model.addAttribute("customer", Customer.builder().build());
        return bookCustomerForm;
    }

    @PostMapping("/customer/new")
    public String processCreationForm(@Valid Customer customer, BindingResult result, @PathVariable Long bookId,
                                      Model model) {
        if(checkIfBookIdIsWrong(bookId, model))
            return errorPage;
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
    public String deleteAuthor(@PathVariable Long customerId, @PathVariable Long bookId, Model model){
        if(checkIfBookIdIsWrong(bookId, model))
            return errorPage;

        Customer deletedCustomer = customerService.findById(customerId);
        if(deletedCustomer == null){
            model.addAttribute("exception", new Exception("There is no customer with id: " + customerId));
            return errorPage;
        }
        currentBook = bookService.findById(bookId);

        currentBook.getCustomers().remove(deletedCustomer);
        deletedCustomer.getBooks().remove(currentBook);

        customerService.save(deletedCustomer);
        bookService.save(currentBook);
        return "redirect:/book/" + bookId + "/customers";
    }

    public boolean checkIfBookIdIsWrong(@PathVariable Long bookId, Model model) {
        if (bookService.findById(bookId) == null) {
            model.addAttribute("exception", new Exception("There is no book with id: " + bookId));
            return true;
        } else return false;
    }
}
