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
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class AuthorController {
    private final static String authorForm = "/author/authorform";

    private final static String errorPage = "/error/400error";

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
    public String showAuthorById(@PathVariable Long id, Model model){
        Author author = authorService.findById(id);
        if(author == null){
            model.addAttribute("exception", new Exception("There is no author with id: " + id));
            return errorPage;
        }
        model.addAttribute("author", author);
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
        Author author = authorService.findById(id);
        if(author == null){
            model.addAttribute("exception", new Exception("There is no author with id: " + id));
            return errorPage;
        }
        model.addAttribute(author);
        return authorForm;
    }

    @PostMapping("/author/{id}/update")
    public String processUpdateAuthorForm(@Valid Author author, BindingResult result, @PathVariable Long id, Model model){
        if(authorService.findById(id) == null){
            model.addAttribute("exception", new Exception("There is no author with id: " + id));
            return errorPage;
        }
        if(result.hasErrors()){
            return authorForm;
        }else{
            authorService.save(author);
            return "redirect:/author/" + author.getId() + "/show";
        }
    }

    @RequestMapping("author/{id}/delete")
    public String deleteAuthor(@PathVariable Long id, Model model){
        if(authorService.findById(id) == null){
            model.addAttribute("exception", new Exception("There is no author with id: " + id));
            return errorPage;
        }
        authorService.deleteById(id);
        return "redirect:/authors";
    }
}
