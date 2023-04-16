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
@RequestMapping("/book/{bookId}")
public class BookAuthorsController {
    private final static String bookAuthorForm = "book/bookauthors/bookauthorform";

    private final BookService bookService;

    private final AuthorService authorService;

    private Book currentBook;

    public BookAuthorsController(BookService bookService, AuthorService authorService) {
        this.bookService = bookService;
        this.authorService = authorService;
    }

    @RequestMapping({"/authors", "/authors/show"})
    public String getAllBookAuthors(@PathVariable Long bookId, Model model){
        currentBook = bookService.findById(bookId);
        model.addAttribute("authors", currentBook.getAuthors());
        model.addAttribute("book", currentBook);
        return "book/bookauthors/index";
    }

    @GetMapping("/author/new")
    public String initCreationForm(Model model){
        model.addAttribute("author", Author.builder().build());
        return bookAuthorForm;
    }

    @PostMapping("/author/new")
    public String processCreationForm(@Valid Author author, BindingResult result, @PathVariable Long bookId){
        if(result.hasErrors()){
            return bookAuthorForm;
        }else{
            Author foundAuthor = authorService.findByFullName(author.getFirstName(), author.getLastName());
            currentBook = bookService.findById(bookId);

            if(foundAuthor != null){
                author = foundAuthor;
            }
            author.getBooks().add(currentBook);
            currentBook.getAuthors().add(author);

            authorService.save(author);
            bookService.save(currentBook);
            return "redirect:/book/" + bookId + "/authors";
        }
    }

    @RequestMapping("/author/{authorId}/delete")
    public String deleteAuthor(@PathVariable Long authorId, @PathVariable Long bookId){
        Author deletedAuthor = authorService.findById(authorId);
        currentBook = bookService.findById(bookId);

        currentBook.getAuthors().remove(deletedAuthor);
        deletedAuthor.getBooks().remove(currentBook);

        authorService.save(deletedAuthor);
        bookService.save(currentBook);
        return "redirect:/book/" + bookId + "/authors";
    }
}
