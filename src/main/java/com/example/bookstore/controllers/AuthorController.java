package com.example.bookstore.controllers;

import com.example.bookstore.service.AuthorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AuthorController {
    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @RequestMapping({"/authors", "/authors/show"})
    public String getAllAuthors(Model model){
        model.addAttribute("authors", authorService.findAll());
        return "author/index";
    }
}
