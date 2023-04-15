package com.example.bookstore.controllers;

import com.example.bookstore.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @RequestMapping({"/books", "/books/show"})
    public String showBooks(Model model){
      log.debug("Getting show books");

      model.addAttribute("books", bookService.findAll());
      return "book/index";
    }

    @RequestMapping("/book/{id}/show")
    public String showById(@PathVariable String id, Model model){
        model.addAttribute("book", bookService.findById(Long.valueOf(id)));
        return "book/show";
    }
}
