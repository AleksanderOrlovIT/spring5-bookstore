package com.example.bookstore.controllers;

import com.example.bookstore.service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @RequestMapping({"/books", "/books/show"})
    public String getAllBooks(Model model){
      model.addAttribute("books", bookService.findAll());
      return "book/index";
    }

    @RequestMapping("/book/{id}/show")
    public String showById(@PathVariable String id, Model model){
        model.addAttribute("book", bookService.findById(Long.valueOf(id)));
        return "book/show";
    }
}
