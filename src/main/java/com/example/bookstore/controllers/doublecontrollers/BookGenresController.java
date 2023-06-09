package com.example.bookstore.controllers.doublecontrollers;

import com.example.bookstore.model.Book;
import com.example.bookstore.model.Genre;
import com.example.bookstore.service.BookService;
import com.example.bookstore.service.GenreService;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/book/{bookId}")
public class BookGenresController {
    private final static String bookGenreForm = "book/bookgenres/bookgenreform";

    private final static String errorPage = "/error/400error";

    private final BookService bookService;

    private final GenreService genreService;

    private Book currentBook;

    public BookGenresController(BookService bookService, GenreService genreService) {
        this.bookService = bookService;
        this.genreService = genreService;
    }

    @RequestMapping({"/genres", "genres/show"})
    public String getAllBookGenres(@PathVariable Long bookId, Model model){
        if(checkIfBookIdIsWrong(bookId, model))
            return errorPage;
        currentBook = bookService.findById(bookId);
        model.addAttribute("genres", currentBook.getGenres());
        model.addAttribute("book", currentBook);
        return "book/bookgenres/index";
    }

    @GetMapping("/genre/new")
    public String initCreationForm(Model model, @PathVariable Long bookId){
        if(checkIfBookIdIsWrong(bookId, model))
            return errorPage;
        model.addAttribute("genre", Genre.builder().build());
        return bookGenreForm;
    }

    @PostMapping("/genre/new")
    public String processCreationForm(@Valid Genre genre, BindingResult result, @PathVariable Long bookId, Model model) {
        if(checkIfBookIdIsWrong(bookId, model))
            return errorPage;
        if (result.hasErrors()) {
            return bookGenreForm;
        } else {
            Genre foundGenre = genreService.findByName(genre.getName());
            currentBook = bookService.findById(bookId);

            if (foundGenre != null) {
                genre = foundGenre;
            }
            genre.getBooks().add(currentBook);
            currentBook.getGenres().add(genre);

            genreService.save(genre);
            bookService.save(currentBook);
            return "redirect:/book/" + bookId + "/genres";
        }
    }

    @RequestMapping("/genre/{genreId}/delete")
    public String deleteGenre(@PathVariable Long genreId, @PathVariable Long bookId, Model model){
        if(checkIfBookIdIsWrong(bookId, model))
            return errorPage;

        Genre deletedGenre = genreService.findById(genreId);
        if(deletedGenre == null){
            model.addAttribute("exception", new Exception("There is no genre with id: " + genreId));
            return errorPage;
        }
        currentBook = bookService.findById(bookId);

        currentBook.getGenres().remove(deletedGenre);
        deletedGenre.getBooks().remove(currentBook);

        genreService.save(deletedGenre);
        bookService.save(currentBook);
        return "redirect:/book/" + bookId + "/genres";
    }

    public boolean checkIfBookIdIsWrong(@PathVariable Long bookId, Model model) {
        if (bookService.findById(bookId) == null) {
            model.addAttribute("exception", new Exception("There is no book with id: " + bookId));
            return true;
        } else return false;
    }
}
