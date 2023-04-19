package com.example.bookstore.controllers.doublecontrollers;

import com.example.bookstore.model.Author;
import com.example.bookstore.model.Book;
import com.example.bookstore.service.AuthorService;
import com.example.bookstore.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthorBooksControllerTest {

    private final static String authorBookForm = "author/authorbooks/authorbookform";

    private static final String errorPage = "/error/400error";

    @Mock
    BookService bookService;

    @Mock
    AuthorService authorService;

    @InjectMocks
    AuthorBooksController authorBooksController;

    MockMvc mockMvc;

    Author authorMock = Author.builder().build();


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authorBooksController).build();
    }

    @Test
    void getAllAuthorsBooks() throws Exception{
        Set<Book> booksSet = new HashSet<>();
        booksSet.add(Book.builder().id(1L).build());
        booksSet.add(Book.builder().id(2L).build());
        Author author = Author.builder().id(1L).books(booksSet).build();

        when(authorService.findById(anyLong())).thenReturn(author);

        mockMvc.perform(get("/author/" + author.getId() + "/books"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("books", hasSize(2)))
                .andExpect(model().attributeExists("author"))
                .andExpect(view().name("author/authorbooks/index"));

        verify(authorService, times(2)).findById(anyLong());
        verifyNoInteractions(bookService);
    }

    @Test
    void getAllAuthorsBooksWithBrokenAuthorId() throws Exception{
        mockMvc.perform(get("/author/1/books"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("exception"))
                .andExpect(view().name(errorPage));

        verify(authorService, times(1)).findById(anyLong());
    }

    @Test
    void initCreationForm() throws Exception{
        when(authorService.findById(anyLong())).thenReturn(authorMock);

        mockMvc.perform(get("/author/1/book/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("book"))
                .andExpect(view().name(authorBookForm));

        verify(authorService, times(1)).findById(anyLong());
        verifyNoInteractions(bookService);
    }

    @Test
    void initCreationFormWithBrokenAuthorId() throws Exception{
        mockMvc.perform(get("/author/1/book/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("exception"))
                .andExpect(view().name(errorPage));

        verify(authorService, times(1)).findById(anyLong());
    }

    @Test
    void processCreationForm() throws Exception{
        Author author = Author.builder().id(1L).build();
        Book book = Book.builder().name("book").price(BigDecimal.valueOf(1.0)).build();

        when(authorService.save(any())).thenReturn(author);
        when(authorService.findById(anyLong())).thenReturn(author);
        when(bookService.save(any())).thenReturn(book);
        when(bookService.findByName(anyString())).thenReturn(book);

        mockMvc.perform(post("/author/" + author.getId() + "/book/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", book.getName())
                .param("price", book.getPrice().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/author/" + author.getId() + "/books"));

        verify(authorService, times(1)).save(any());
        verify(authorService, times(2)).findById(anyLong());
        verify(bookService, times(1)).save(any());
        verify(bookService, times(1)).findByName(anyString());
    }

    @Test
    void processCreationFormWithBrokenAuthorId() throws Exception{
        mockMvc.perform(post("/author/1/book/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("exception"))
                .andExpect(view().name(errorPage));

        verify(authorService, times(1)).findById(anyLong());
    }

    @Test
    void deleteBook() throws Exception{
        Author author = Author.builder().id(1L).build();
        Book book = Book.builder().id(1L).build();
        author.getBooks().add(book);
        book.getAuthors().add(author);

        when(authorService.findById(anyLong())).thenReturn(author);
        when(authorService.save(any())).thenReturn(author);
        when(bookService.findById(anyLong())).thenReturn(book);
        when(bookService.save(any())).thenReturn(book);

        mockMvc.perform(get("/author/" + author.getId() + "/book/" + book.getId() + "/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/author/" + author.getId() + "/books"));

        verify(authorService, times(1)).save(any());
        verify(authorService, times(2)).findById(anyLong());
        verify(bookService, times(1)).save(any());
        verify(bookService, times(1)).findById(anyLong());
    }

    @Test
    void deleteBookWithBrokenAuthorId() throws Exception{
        mockMvc.perform(get("/author/1/book/1/delete"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("exception"))
                .andExpect(view().name(errorPage));

        verify(authorService, times(1)).findById(anyLong());
    }

    @Test
    void deleteBookWithBrokenBookId() throws Exception{
        when(authorService.findById(anyLong())).thenReturn(authorMock);

        mockMvc.perform(get("/author/1/book/1/delete"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("exception"))
                .andExpect(view().name(errorPage));

        verify(authorService, times(1)).findById(anyLong());
        verify(bookService, times(1)).findById(anyLong());
    }
}