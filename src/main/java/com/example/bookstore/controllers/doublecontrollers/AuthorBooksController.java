package com.example.bookstore.controllers.doublecontrollers;

import com.example.bookstore.model.Author;
import com.example.bookstore.model.Book;
import com.example.bookstore.service.AuthorService;
import com.example.bookstore.service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.math.BigDecimal;

@Controller
@RequestMapping("/author/{authorId}")
public class AuthorBooksController {

    private final static String authorBookForm = "author/authorbooks/authorbookform";

    private final static String errorPage = "/error/400error";

    private final AuthorService authorService;

    private final BookService bookService;

    private Author currentAuthor;

    public AuthorBooksController(AuthorService authorService, BookService bookService) {
        this.authorService = authorService;
        this.bookService = bookService;
    }

    @RequestMapping({"/books", "/books/show"})
    public String getAllAuthorsBooks(@PathVariable Long authorId, Model model) {
        if(checkIfAuthorIdIsWrong(authorId, model))
            return errorPage;

        currentAuthor = authorService.findById(authorId);
        model.addAttribute("books", currentAuthor.getBooks());
        model.addAttribute("author", currentAuthor);
        return "author/authorbooks/index";
    }


    @GetMapping("/book/new")
    public String initCreationForm(Model model, @PathVariable Long authorId) {
        if(checkIfAuthorIdIsWrong(authorId, model))
            return errorPage;

        model.addAttribute("book", Book.builder().price(BigDecimal.valueOf(0.0)).build());
        return authorBookForm;
    }

    @PostMapping("/book/new")
    public String processCreationForm(@Valid Book book, BindingResult result, @PathVariable Long authorId,
                                      Model model) {
        if(checkIfAuthorIdIsWrong(authorId, model))
            return errorPage;
        if (result.hasErrors()) {
            return authorBookForm;
        } else {
            Book findBook = bookService.findByName(book.getName());
            currentAuthor = authorService.findById(authorId);
            if (findBook != null) {
                book = findBook;
            } else {
                book.setPrice(BigDecimal.valueOf(10));
            }
            currentAuthor.getBooks().add(book);
            book.getAuthors().add(currentAuthor);

            authorService.save(currentAuthor);
            bookService.save(book);
            return "redirect:/author/" + authorId + "/books";
        }
    }

    @RequestMapping("/book/{bookId}/delete")
    public String deleteBook(@PathVariable Long bookId, @PathVariable Long authorId, Model model) {
        if(checkIfAuthorIdIsWrong(authorId, model))
            return errorPage;

        Book deletedBook = bookService.findById(bookId);
        if(deletedBook == null){
            model.addAttribute("exception", new Exception("There is no book with id: " + bookId));
            return errorPage;
        }
        currentAuthor = authorService.findById(authorId);

        currentAuthor.getBooks().remove(deletedBook);
        deletedBook.getAuthors().remove(currentAuthor);

        authorService.save(currentAuthor);
        bookService.save(deletedBook);
        return "redirect:/author/" + authorId + "/books";
    }


    public boolean checkIfAuthorIdIsWrong(@PathVariable Long authorId, Model model) {
        if (authorService.findById(authorId) == null) {
            model.addAttribute("exception", new Exception("There is no author with id: " + authorId));
            return true;
        } else return false;
    }
}
