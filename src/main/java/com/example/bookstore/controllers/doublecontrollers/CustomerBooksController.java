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
@RequestMapping("/customer/{customerId}")
public class CustomerBooksController {
    private final static String customerBookForm = "customer/customerbooks/customerbookform";

    private final CustomerService customerService;

    private final BookService bookService;

    private Customer currentCustomer;

    public CustomerBooksController(CustomerService customerService, BookService bookService) {
        this.customerService = customerService;
        this.bookService = bookService;
    }

    @RequestMapping({"/books", "/books/show"})
    public String getAllCustomersBooks(@PathVariable Long customerId, Model model){
        currentCustomer = customerService.findById(customerId);
        model.addAttribute("books", currentCustomer.getBooks());
        model.addAttribute("customer", currentCustomer);
        return "customer/customerbooks/index";
    }

    @GetMapping("/book/new")
    public String initCreationForm(Model model){
        model.addAttribute("book", Book.builder().build());
        return customerBookForm;
    }

    @PostMapping("/book/new")
    public String processCreationForm(@Valid Book book, BindingResult result, @PathVariable Long customerId){
        if(result.hasErrors()){
            return customerBookForm;
        }else{
            Book findBook = bookService.findByName(book.getName());
            currentCustomer = customerService.findById(customerId);
            if(findBook != null){
                findBook.setPrice(book.getPrice());
                book = findBook;
            }
            currentCustomer.getBooks().add(book);
            book.getCustomers().add(currentCustomer);

            customerService.save(currentCustomer);
            bookService.save(book);
            return "redirect:/customer/" + customerId + "/books";
        }
    }

    @RequestMapping("/book/{bookId}/delete")
    public String deleteBook(@PathVariable Long bookId, @PathVariable Long customerId){
        Book deletedBook = bookService.findById(bookId);
        currentCustomer = customerService.findById(customerId);

        currentCustomer.getBooks().remove(deletedBook);
        deletedBook.getCustomers().remove(currentCustomer);

        customerService.save(currentCustomer);
        bookService.save(deletedBook);
        return "redirect:/customer/" + customerId + "/books";
    }
}
