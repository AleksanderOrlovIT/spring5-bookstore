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

@Controller
@RequestMapping("/author/{authorId}")
public class AuthorBooksController {

    private final static String authorBookForm = "author/authorbooks/authorbookform";

    private final AuthorService authorService;

    private final BookService bookService;

    private Author currentAuthor;

    public AuthorBooksController(AuthorService authorService, BookService bookService) {
        this.authorService = authorService;
        this.bookService = bookService;
    }

    @RequestMapping({"/books", "/books/show"})
    public String getAllAuthorsBooks(@PathVariable Long authorId, Model model){
        currentAuthor = authorService.findById(authorId);
        model.addAttribute("books", currentAuthor.getBooks());
        model.addAttribute("author", currentAuthor);
        return "author/authorbooks/index";
    }


    @GetMapping("/book/new")
    public String initCreationForm(Model model){
        model.addAttribute("book", Book.builder().build());
        return authorBookForm;
    }

    @PostMapping("/book/new")
    public String processCreationForm(@Valid Book book, BindingResult result, @PathVariable Long authorId){
        if(result.hasErrors()){
            return authorBookForm;
        }else{
            Book findBook = bookService.findByName(book.getName());
            currentAuthor = authorService.findById(authorId);
            if(findBook != null){
                book = findBook;
            }
            currentAuthor.getBooks().add(book);
            book.getAuthors().add(currentAuthor);

            authorService.save(currentAuthor);
            bookService.save(book);
            return "redirect:/author/" + authorId + "/books";
        }
    }

    @RequestMapping("/book/{bookId}/delete")
    public String deleteBook(@PathVariable Long bookId, @PathVariable Long authorId){
        Book deletedBook = bookService.findById(bookId);
        currentAuthor = authorService.findById(authorId);

        currentAuthor.getBooks().remove(deletedBook);
        deletedBook.getAuthors().remove(currentAuthor);

        authorService.save(currentAuthor);
        bookService.save(deletedBook);
        return "redirect:/author/" + authorId + "/books";
    }

}
