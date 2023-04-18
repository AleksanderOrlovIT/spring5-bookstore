package com.example.bookstore.controllers.doublecontrollers;

import com.example.bookstore.model.Author;
import com.example.bookstore.model.Genre;
import com.example.bookstore.service.AuthorService;
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
@RequestMapping("/author/{authorId}")
public class AuthorGenresController {

    private final static String authorGenreForm = "author/authorgenres/authorgenreform";

    private final static String errorPage = "/error/400error";

    private final AuthorService authorService;

    private final GenreService genreService;

    private Author currentAuthor;

    public AuthorGenresController(AuthorService authorService, GenreService genreService) {
        this.authorService = authorService;
        this.genreService = genreService;
    }

    @RequestMapping({"/genres", "/genres/show"})
    public String getAllAuthorsGenres(@PathVariable Long authorId, Model model){
        if(checkIfAuthorIdIsWrong(authorId, model))
            return errorPage;
        currentAuthor = authorService.findById(authorId);
        model.addAttribute("genres", currentAuthor.getGenres());
        model.addAttribute("author", currentAuthor);
        return "author/authorgenres/index";
    }


    @GetMapping("/genre/new")
    public String initCreationForm(Model model, @PathVariable Long authorId){
        if(checkIfAuthorIdIsWrong(authorId, model))
            return errorPage;
        model.addAttribute("genre", Genre.builder().build());
        return authorGenreForm;
    }

    @PostMapping("/genre/new")
    public String processCreationForm(@Valid Genre genre, BindingResult result, @PathVariable Long authorId, Model model){
        if(checkIfAuthorIdIsWrong(authorId, model))
            return errorPage;
        if(result.hasErrors()){
            return authorGenreForm;
        }else{
            Genre findGenre = genreService.findByName(genre.getName());
            currentAuthor = authorService.findById(authorId);
            if(findGenre != null){
                genre = findGenre;
            }
            currentAuthor.getGenres().add(genre);
            genre.getAuthors().add(currentAuthor);

            genreService.save(genre);
            authorService.save(currentAuthor);
            return "redirect:/author/" + authorId + "/genres";
        }
    }

    @RequestMapping("/genre/{genreId}/delete")
    public String deleteGenre(@PathVariable Long genreId, @PathVariable Long authorId, Model model){
        if(checkIfAuthorIdIsWrong(authorId, model))
            return errorPage;

        Genre deletedGenre = genreService.findById(genreId);
        if(deletedGenre == null){
            model.addAttribute("exception", new Exception("There is no genre with id: " + genreId));
            return errorPage;
        }
        currentAuthor = authorService.findById(authorId);

        currentAuthor.getGenres().remove(deletedGenre);
        deletedGenre.getAuthors().remove(currentAuthor);

        genreService.save(deletedGenre);
        authorService.save(currentAuthor);
        return "redirect:/author/" + authorId + "/genres";
    }

    public boolean checkIfAuthorIdIsWrong(@PathVariable Long authorId, Model model) {
        if (authorService.findById(authorId) == null) {
            model.addAttribute("exception", new Exception("There is no author with id: " + authorId));
            return true;
        } else return false;
    }

}
