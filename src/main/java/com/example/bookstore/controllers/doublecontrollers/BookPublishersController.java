package com.example.bookstore.controllers.doublecontrollers;

import com.example.bookstore.model.Book;
import com.example.bookstore.model.Publisher;
import com.example.bookstore.service.AuthorService;
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
@RequestMapping({"/book/{bookId}"})
public class BookPublishersController {
    private final static String bookPublisherForm = "book/bookpublishers/bookpublisherform";

    private final BookService bookService;

    private final PublisherService publisherService;

    private Book currentBook;

    public BookPublishersController(BookService bookService, PublisherService publisherService) {
        this.bookService = bookService;
        this.publisherService = publisherService;
    }

    @RequestMapping({"/publishers", "/publishers/show"})
    public String getAllBookAuthors(@PathVariable Long bookId, Model model){
        currentBook = bookService.findById(bookId);
        model.addAttribute("publishers", currentBook.getPublishers());
        model.addAttribute("book", currentBook);
        return "book/bookpublishers/index";
    }

    @GetMapping("/publisher/new")
    public String initCreationForm(Model model){
        model.addAttribute("publisher", Publisher.builder().build());
        return bookPublisherForm;
    }

    @PostMapping("/publisher/new")
    public String processCreationForm(@Valid Publisher publisher, BindingResult result, @PathVariable Long bookId){
        if(result.hasErrors()){
            return bookPublisherForm;
        }else{
            Publisher foundPublisher = publisherService.findByName(publisher.getName());
            currentBook = bookService.findById(bookId);

            if(foundPublisher != null){
                publisher = foundPublisher;
            }
            publisher.getBooks().add(currentBook);
            currentBook.getPublishers().add(publisher);

            publisherService.save(publisher);
            bookService.save(currentBook);
            return "redirect:/book/" + bookId + "/publishers";
        }
    }


    @RequestMapping("/publisher/{publisherId}/delete")
    public String deletePublisher(@PathVariable Long publisherId, @PathVariable Long bookId){
        Publisher deletedPublisher = publisherService.findById(publisherId);
        currentBook = bookService.findById(bookId);

        currentBook.getPublishers().remove(deletedPublisher);
        deletedPublisher.getBooks().remove(currentBook);

        publisherService.save(deletedPublisher);
        bookService.save(currentBook);
        return "redirect:/book/" + bookId + "/publishers";
    }
}
