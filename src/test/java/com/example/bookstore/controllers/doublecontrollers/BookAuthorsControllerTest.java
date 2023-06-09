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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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
class BookAuthorsControllerTest {

    private final static String bookAuthorForm = "book/bookauthors/bookauthorform";

    private static final String errorPage = "/error/400error";

    @Mock
    BookService bookService;

    @Mock
    AuthorService authorService;

    @InjectMocks
    BookAuthorsController bookAuthorsController;

    MockMvc mockMvc;

    Book bookMock = Book.builder().build();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookAuthorsController).build();
    }

    @Test
    void getAllBookAuthors() throws Exception{
        Set<Author> authorsSet = new HashSet<>();
        authorsSet.add(Author.builder().id(1L).build());
        authorsSet.add(Author.builder().id(2L).build());
        Book book = Book.builder().id(1L).authors(authorsSet).build();

        when(bookService.findById(anyLong())).thenReturn(book);

        mockMvc.perform(get("/book/" + book.getId() + "/authors"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("authors", hasSize(2)))
                .andExpect(model().attributeExists("book"))
                .andExpect(view().name("book/bookauthors/index"));

        verify(bookService, times(2)).findById(anyLong());
        verifyNoInteractions(authorService);
    }

    @Test
    void getAllBookAuthorsWithBrokenBookId() throws Exception{
        mockMvc.perform(get("/book/1/authors"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("exception"))
                .andExpect(view().name(errorPage));

        verify(bookService, times(1)).findById(anyLong());
    }

    @Test
    void initCreationForm() throws Exception{
        when(bookService.findById(anyLong())).thenReturn(bookMock);

        mockMvc.perform(get("/book/1/author/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("author"))
                .andExpect(view().name(bookAuthorForm));

        verify(bookService, times(1)).findById(anyLong());
        verifyNoInteractions(authorService);
    }

    @Test
    void initCreationFormWithBrokenBookId() throws Exception{
        mockMvc.perform(get("/book/1/author/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("exception"))
                .andExpect(view().name(errorPage));

        verify(bookService, times(1)).findById(anyLong());
    }

    @Test
    void processCreationForm() throws Exception{
        Author author = Author.builder().id(1L).firstName("first").lastName("last").build();
        Book book = Book.builder().id(1L).build();

        when(authorService.findByFullName(anyString(), anyString())).thenReturn(author);
        when(authorService.save(any())).thenReturn(author);
        when(bookService.findById(anyLong())).thenReturn(book);
        when(bookService.save(any())).thenReturn(book);

        mockMvc.perform(post("/book/" + book.getId() + "/author/new")
                .param("firstName", author.getFirstName())
                .param("lastName", author.getLastName()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/book/" + book.getId() + "/authors"));

        verify(authorService, times(1)).findByFullName(anyString(), anyString());
        verify(authorService, times(1)).save(any());
        verify(bookService, times(2)).findById(anyLong());
        verify(bookService, times(1)).save(any());
    }

    @Test
    void processCreationFormWithBrokenBookId() throws Exception{
        mockMvc.perform(post("/book/1/author/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("exception"))
                .andExpect(view().name(errorPage));

        verify(bookService, times(1)).findById(anyLong());
    }

    @Test
    void deleteAuthor() throws Exception{
        Author author = Author.builder().id(1L).build();
        Book book = Book.builder().id(1L).build();
        author.getBooks().add(book);
        book.getAuthors().add(author);

        when(authorService.findById(anyLong())).thenReturn(author);
        when(authorService.save(any())).thenReturn(author);
        when(bookService.findById(anyLong())).thenReturn(book);
        when(bookService.save(any())).thenReturn(book);

        mockMvc.perform(get("/book/" + book.getId() + "/author/" + author.getId() + "/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/book/" + book.getId() + "/authors"));

        verify(authorService, times(1)).findById(anyLong());
        verify(authorService, times(1)).save(any());
        verify(bookService, times(2)).findById(anyLong());
        verify(bookService, times(1)).save(any());
    }

    @Test
    void deleteAuthorWithBrokenBookId() throws Exception{
        mockMvc.perform(get("/book/1/author/1/delete"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("exception"))
                .andExpect(view().name(errorPage));

        verify(bookService, times(1)).findById(anyLong());
    }

    @Test
    void deleteAuthorWithBrokenAuthorId() throws Exception{
        when(bookService.findById(anyLong())).thenReturn(bookMock);
        mockMvc.perform(get("/book/1/author/1/delete"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("exception"))
                .andExpect(view().name(errorPage));

        verify(bookService, times(1)).findById(anyLong());
        verify(authorService, times(1)).findById(anyLong());
    }
}