package com.example.bookstore.controllers;

import com.example.bookstore.model.Author;
import com.example.bookstore.service.AuthorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
public class AuthorController {
    private final static String authorForm = "author/authorform";

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @RequestMapping({"/authors", "/authors/show"})
    public String getAllAuthors(Model model){
        model.addAttribute("authors", authorService.findAll());
        return "author/index";
    }

    @RequestMapping("/author/{id}/show")
    public String getAuthorById(@PathVariable String id, Model model){
        model.addAttribute("author", authorService.findById(Long.valueOf(id)));
        return "author/show";
    }

    @GetMapping("/author/new")
    public String initCreationForm(Model model){
        model.addAttribute("author", Author.builder().build());
        return authorForm;
    }

    @PostMapping("/author/new")
    public String processCreationForm(@Valid Author author, BindingResult result){
        if (result.hasErrors()){
            return authorForm;
        } else {
          Author savedAuthor = authorService.save(author);
          return "redirect:/author/" + savedAuthor.getId() + "/show";
        }
    }

    @GetMapping("/author/{id}/update")
    public String initUpdateAuthorForm(@PathVariable Long id, Model model){
        model.addAttribute(authorService.findById(id));
        return authorForm;
    }

    @PostMapping("/author/{id}/update")
    public String processUpdateAuthorForm(@Valid Author author, BindingResult result, @PathVariable Long id){
        if(result.hasErrors()){
            return authorForm;
        }else{
            authorService.save(author);
            return "redirect:/author/" + author.getId() + "/show";
        }
    }

    @RequestMapping("author/{id}/delete")
    public String deleteAuthor(@PathVariable Long id){
        authorService.deleteById(id);
        return "redirect:/authors";
    }
}
