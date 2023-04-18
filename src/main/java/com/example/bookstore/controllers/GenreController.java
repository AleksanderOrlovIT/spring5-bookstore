package com.example.bookstore.controllers;

import com.example.bookstore.model.Book;
import com.example.bookstore.model.Genre;
import com.example.bookstore.service.GenreService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
public class GenreController {

    private final static String genreForm = "/genre/genreform";

    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @RequestMapping({"/genres", "/genres/show"})
    public String getGenres(Model model){
        model.addAttribute("genres", genreService.findAll());
        return "genre/index";
    }

    @RequestMapping("/genre/{id}/show")
    public String showGenreById(@PathVariable Long id, Model model){
        Genre genre = genreService.findById(id);
        if(genre == null){
            model.addAttribute("exception", new Exception("There is no genre with id: " + id));
            return "/error/400error";
        }
        model.addAttribute("genre", genre);
        return "genre/show";
    }

    @GetMapping("/genre/new")
    public String initCreationForm(Model model){
        model.addAttribute("genre", Genre.builder().build());
        return genreForm;
    }

    @PostMapping("/genre/new")
    public String processCreationForm(@Valid Genre genre, BindingResult result){
        if(result.hasErrors()){
            return genreForm;
        }else{
            Genre savedGenre = genreService.save(genre);
            return "redirect:/genre/" + savedGenre.getId() + "/show";
        }
    }

    @GetMapping("/genre/{id}/update")
    public String initUpdateGenreForm(@PathVariable Long id, Model model){
        Genre genre = genreService.findById(id);
        if(genre == null){
            model.addAttribute("exception", new Exception("There is no genre with id: " + id));
            return "/error/400error";
        }
        model.addAttribute(genre);
        return genreForm;
    }

    @PostMapping("/genre/{id}/update")
    public String processUpdateGenreForm(@Valid Genre genre, BindingResult result, @PathVariable Long id,
                                         Model model){
        if(genreService.findById(id) == null){
            model.addAttribute("exception", new Exception("There is no genre with id: " + id));
            return "/error/400error";
        }
        if(result.hasErrors()){
            return genreForm;
        }else{
            genreService.save(genre);
            return "redirect:/genre/" + genre.getId() + "/show";
        }
    }

    @RequestMapping("genre/{id}/delete")
    public String deleteGenre(@PathVariable Long id, Model model){
        if(genreService.findById(id) == null){
            model.addAttribute("exception", new Exception("There is no genre with id: " + id));
            return "/error/400error";
        }
        genreService.deleteById(id);
        return "redirect:/genres";
    }
}
