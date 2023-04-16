package com.example.bookstore.controllers.doublecontrollers;

import com.example.bookstore.model.Book;
import com.example.bookstore.model.Publisher;
import com.example.bookstore.service.BookService;
import com.example.bookstore.service.PublisherService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/publisher/{publisherId}")
public class PublisherBooksController {
    private final static String publisherBookForm = "publisher/publisherbooks/publisherbookform";

    private final PublisherService publisherService;

    private final BookService bookService;

    private Publisher currentPublisher;

    public PublisherBooksController(PublisherService publisherService, BookService bookService) {
        this.publisherService = publisherService;
        this.bookService = bookService;
    }

    @RequestMapping({"/books", "books/show"})
    public String getAllPublishersBooks(@PathVariable Long publisherId, Model model){
        currentPublisher = publisherService.findById(publisherId);
        model.addAttribute("books", currentPublisher.getBooks());
        model.addAttribute("publisher", currentPublisher);
        return "publisher/publisherbooks/index";
    }

    @GetMapping("/book/new")
    public String initCreationForm(Model model){
        model.addAttribute("book", Book.builder().build());
        return publisherBookForm;
    }

    @PostMapping("/book/new")
    public String processCreationForm(@Valid Book book, BindingResult result, @PathVariable Long publisherId){
        if(result.hasErrors()){
            return publisherBookForm;
        }else{
            Book findBook = bookService.findByName(book.getName());
            currentPublisher = publisherService.findById(publisherId);
            if(findBook != null){
                findBook.setPrice(book.getPrice());
                book = findBook;
            }
            currentPublisher.getBooks().add(book);
            book.getPublishers().add(currentPublisher);

            publisherService.save(currentPublisher);
            bookService.save(book);
            return "redirect:/publisher/" + publisherId + "/books";
        }
    }

    @RequestMapping("/book/{bookId}/delete")
    public String deleteBook(@PathVariable Long bookId, @PathVariable Long publisherId){
        Book deletedBook = bookService.findById(bookId);
        currentPublisher = publisherService.findById(publisherId);

        currentPublisher.getBooks().remove(deletedBook);
        deletedBook.getPublishers().remove(currentPublisher);

        publisherService.save(currentPublisher);
        bookService.save(deletedBook);
        return "redirect:/publisher/" + publisherId + "/books";
    }
}
