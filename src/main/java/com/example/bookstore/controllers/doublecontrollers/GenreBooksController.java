package com.example.bookstore.controllers.doublecontrollers;

import com.example.bookstore.model.Book;
import com.example.bookstore.model.Genre;
import com.example.bookstore.service.BookService;
import com.example.bookstore.service.GenreService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/genre/{genreId}")
public class GenreBooksController {
    private final static String genreBookForm = "genre/genrebooks/genrebookform";

    private final static String errorPage = "/error/400error";

    private final GenreService genreService;

    private final BookService bookService;

    private Genre currentGenre;

    public GenreBooksController(GenreService genreService, BookService bookService) {
        this.genreService = genreService;
        this.bookService = bookService;
    }

    @RequestMapping({"/books", "/books/show"})
    public String getAllGenresBooks(@PathVariable Long genreId, Model model){
        if(checkIfGenreIdIsWrong(genreId, model))
            return errorPage;
        currentGenre = genreService.findById(genreId);
        model.addAttribute("books", currentGenre.getBooks());
        model.addAttribute("genre", currentGenre);
        return "genre/genrebooks/index";
    }

    @GetMapping("/book/new")
    public String initCreationForm(Model model, @PathVariable Long genreId){
        if(checkIfGenreIdIsWrong(genreId, model))
            return errorPage;
        model.addAttribute("book", Book.builder().build());
        return genreBookForm;
    }

    @PostMapping("/book/new")
    public String processCreationForm(@Valid Book book, BindingResult result, @PathVariable Long genreId, Model model){
        if(checkIfGenreIdIsWrong(genreId, model))
            return errorPage;
        if(result.hasErrors()){
            return genreBookForm;
        }else{
            Book findBook = bookService.findByName(book.getName());
            currentGenre = genreService.findById(genreId);
            if(findBook != null){
                findBook.setPrice(book.getPrice());
                book = findBook;
            }
            currentGenre.getBooks().add(book);
            book.getGenres().add(currentGenre);

            genreService.save(currentGenre);
            bookService.save(book);
            return "redirect:/genre/" + genreId + "/books";
        }
    }

    @RequestMapping("/book/{bookId}/delete")
    public String deleteBook(@PathVariable Long bookId, @PathVariable Long genreId, Model model){
        if(checkIfGenreIdIsWrong(genreId, model))
            return errorPage;

        Book deletedBook = bookService.findById(bookId);
        if(deletedBook == null){
            model.addAttribute("exception", new Exception("There is no book with id: " + bookId));
            return errorPage;
        }
        currentGenre = genreService.findById(genreId);

        currentGenre.getBooks().remove(deletedBook);
        deletedBook.getGenres().remove(currentGenre);

        genreService.save(currentGenre);
        bookService.save(deletedBook);
        return "redirect:/genre/" + genreId + "/books";
    }

    public boolean checkIfGenreIdIsWrong(@PathVariable Long genreId, Model model) {
        if (genreService.findById(genreId) == null) {
            model.addAttribute("exception", new Exception("There is no genre with id: " + genreId));
            return true;
        } else return false;
    }
}
