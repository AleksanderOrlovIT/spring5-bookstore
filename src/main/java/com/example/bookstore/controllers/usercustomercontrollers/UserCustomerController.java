package com.example.bookstore.controllers.usercustomercontrollers;

import com.example.bookstore.model.Book;
import com.example.bookstore.model.Customer;
import com.example.bookstore.service.BookService;
import com.example.bookstore.service.CustomerService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.transaction.Transactional;
import java.util.Set;

@Controller
public class UserCustomerController {

    private final static String errorPage = "/error/400error";

    private Long authorizedId;


    private final BookService bookService;
    private final CustomerService customerService;

    public UserCustomerController(BookService bookService, CustomerService customerService) {
        this.bookService = bookService;
        this.customerService = customerService;
    }

    @RequestMapping("/findPath")
    public String getRightPath() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String userName = ((UserDetails) principal).getUsername();
            if (userName.equals("admin"))
                return "redirect:/homePage";
            authorizedId = customerService.findByUserName(userName).getId();
            return "redirect:/user/" + authorizedId + "/homePage";
        } else return "/error/404error";
    }

    @RequestMapping("/user/{userId}/homePage")
    public String getCustomerHomePage(Model model, @PathVariable Long userId) {
        if(authorizedId.equals(userId)) {
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

    @RequestMapping("/user/{userId}/myProfile")
    public String getUserProfile(Model model, @PathVariable Long userId) {
        if(authorizedId.equals(userId)) {
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

    @RequestMapping("/user/{userId}/books")
    public String getUserBooks(Model model, @PathVariable Long userId) {
        if(authorizedId.equals(userId)) {
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

    @RequestMapping("/user/{userId}/shop")
    public String openShop(Model model, @PathVariable Long userId) {
        if(authorizedId.equals(userId)) {
            Customer customer = customerService.findById(userId);
            if (customer == null) {
                model.addAttribute("exception", new Exception("There is no customer with id: " + userId));
                return errorPage;
            }
            Set<Book> availableBooks = bookService.findAll();
            for(Book book : customer.getBooks()){
                availableBooks.remove(book);
            }
            model.addAttribute("books", availableBooks);
            model.addAttribute("user", customer);
            return "user/shop";
        } else {
            model.addAttribute("exception", new Exception("You didn't authorize with id: " + userId));
            return errorPage;
        }
    }


    @RequestMapping("/user/{userId}/buy/book/{bookId}")
    public String buyBook(@PathVariable Long userId, @PathVariable Long bookId, Model model){
        if(authorizedId.equals(userId)) {
            Customer customer = customerService.findById(userId);
            Book book = bookService.findById(bookId);
            if (customer == null || book == null) {
                model.addAttribute("exception",
                        new Exception("There is no customer with id: " + userId + " or there is no book with id " + bookId));
                return errorPage;
            }
            for (Book tempBook : customer.getBooks()) {
                if (tempBook == book) {
                    model.addAttribute("exception",
                            new Exception("You have already bought this book " + book.getName()));
                    return errorPage;
                }
            }
            int comparingPrice = book.getPrice().compareTo(customer.getBalance());
            if (comparingPrice >= 0) {
                model.addAttribute("exception",
                        new Exception("Book price" + book.getName() + " " + book.getPrice() +
                                "$ is greater than your balance " + customer.getBalance() + "$"));
                return errorPage;
            }
            addBookToCustomer(customer, book);
            return "redirect:/user/" + userId + "/myProfile";
        }else {
            model.addAttribute("exception", new Exception("You didn't authorize with id: " + userId));
            return errorPage;
        }
    }

    public void addBookToCustomer(Customer customer, Book book){
        customer.setBalance(customer.getBalance().subtract(book.getPrice()));

        book.getCustomers().add(customer);
        customer.getBooks().add(book);

        customerService.save(customer);
        bookService.save(book);
    }
}
