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
@RequestMapping("/genre/{genreId}")
public class GenreAuthorsController {
    private final static String genreAuthorForm = "genre/genreauthors/genreauthorform";

    private final static String errorPage = "/error/400error";

    private final GenreService genreService;

    private final AuthorService authorService;

    private Genre currentGenre;

    public GenreAuthorsController(GenreService genreService, AuthorService authorService) {
        this.genreService = genreService;
        this.authorService = authorService;
    }

    @RequestMapping({"/authors", "/authors/show"})
    public String getAllGenresAuthors(@PathVariable Long genreId, Model model){
        if(checkIfGenreIdIsWrong(genreId, model))
            return errorPage;

        currentGenre = genreService.findById(genreId);
        model.addAttribute("authors", currentGenre.getAuthors());
        model.addAttribute("genre", currentGenre);
        return "genre/genreauthors/index";
    }

    @GetMapping("/author/new")
    public String initCreationForm(Model model, @PathVariable Long genreId){
        if(checkIfGenreIdIsWrong(genreId, model))
            return errorPage;
        model.addAttribute("author", Author.builder().build());
        return genreAuthorForm;
    }

    @PostMapping("/author/new")
    public String processCreationForm(@Valid Author author, BindingResult result, @PathVariable Long genreId, Model model){
        if(checkIfGenreIdIsWrong(genreId, model))
            return errorPage;
        if(result.hasErrors()){
            return genreAuthorForm;
        }else{
            Author foundAuthor = authorService.findByFullName(author.getFirstName(), author.getLastName());
            currentGenre = genreService.findById(genreId);
            if(foundAuthor != null){
                author = foundAuthor;
            }
            currentGenre.getAuthors().add(author);
            author.getGenres().add(currentGenre);

            genreService.save(currentGenre);
            authorService.save(author);
            return "redirect:/genre/" + genreId + "/authors";
        }
    }

    @RequestMapping("/author/{authorId}/delete")
    public String deleteBook(@PathVariable Long authorId, @PathVariable Long genreId, Model model){
        if(checkIfGenreIdIsWrong(genreId, model))
            return errorPage;

        Author deletedAuthor = authorService.findById(authorId);
        if (deletedAuthor == null){
            model.addAttribute("exception", new Exception("There is no author with id: " + authorId));
            return errorPage;
        }
        currentGenre = genreService.findById(genreId);

        currentGenre.getAuthors().remove(deletedAuthor);
        deletedAuthor.getGenres().remove(currentGenre);

        genreService.save(currentGenre);
        authorService.save(deletedAuthor);
        return "redirect:/genre/" + genreId + "/authors";
    }

    public boolean checkIfGenreIdIsWrong(@PathVariable Long genreId, Model model) {
        if (genreService.findById(genreId) == null) {
            model.addAttribute("exception", new Exception("There is no genre with id: " + genreId));
            return true;
        } else return false;
    }
}
