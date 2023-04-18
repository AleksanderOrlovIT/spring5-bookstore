package com.example.bookstore.controllers;

import com.example.bookstore.model.Book;
import com.example.bookstore.service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class BookController {

    private static final String bookForm = "/book/bookform";

    private final BookService bookService;

    public BookController(BookService bookService){
        this.bookService = bookService;
    }

    @RequestMapping({"/books", "/books/show"})
    public String getAllBooks(Model model){
      model.addAttribute("books", bookService.findAll());
      return "book/index";
    }

    @RequestMapping("/book/{id}/show")
    public String showBookById(@PathVariable String id, Model model){
        model.addAttribute("book", bookService.findById(Long.valueOf(id)));
        return "book/show";
    }

    @GetMapping("/book/new")
    public String initCreationForm(Model model){
        model.addAttribute("book", Book.builder().build());
        return bookForm;
    }

    @PostMapping("/book/new")
    public String processCreationForm(@Valid Book book, BindingResult result){
        if(result.hasErrors()){
            return bookForm;
        }else{
            Book savedBook = bookService.save(book);
            return "redirect:/book/" + savedBook.getId() + "/show";
        }
    }

    @GetMapping("/book/{id}/update")
    public String initUpdateBookForm(@PathVariable Long id, Model model){
        model.addAttribute(bookService.findById(id));
        return bookForm;
    }

    @PostMapping("/book/{id}/update")
    public String processUpdateBookForm(@Valid Book book, BindingResult result, @PathVariable Long id){
        if(result.hasErrors()){
            return bookForm;
        }else{
            bookService.saveBookSets(book, bookService.findById(id));
            bookService.save(book);
            return "redirect:/book/" + book.getId() + "/show";
        }
    }

    @RequestMapping("/book/{id}/delete")
    public String deleteBook(@PathVariable String id){
        bookService.deleteById(Long.valueOf(id));
        return "redirect:/books";
    }
}
