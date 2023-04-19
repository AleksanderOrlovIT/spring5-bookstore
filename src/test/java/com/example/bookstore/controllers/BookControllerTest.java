package com.example.bookstore.controllers;

import com.example.bookstore.model.Book;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    private static final String bookForm = "/book/bookform";

    private static final String errorPage = "/error/400error";

    @Mock
    BookService bookService;

    @InjectMocks
    BookController bookController;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }

    @Test
    void getAllBooks() throws Exception{
        Set<Book> books = new HashSet<>();
        books.add(Book.builder().id(1L).build());
        books.add(Book.builder().id(2L).build());
        when(bookService.findAll()).thenReturn(books);

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(view().name("book/index"))
                .andExpect(model().attribute("books", hasSize(2)));

        verify(bookService, times(1)).findAll();
    }

    @Test
    void showById() throws Exception {
        when(bookService.findById(anyLong())).thenReturn(Book.builder().id(1L).build());

        mockMvc.perform(get("/book/1/show"))
                .andExpect(status().isOk())
                .andExpect(view().name("book/show"))
                .andExpect(model().attribute("book", hasProperty("id", is(1L))));

        verify(bookService, times(1)).findById(anyLong());
    }

    @Test
    void showByIdWithBrokenId() throws Exception{
        mockMvc.perform(get("/book/1/show"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("exception"))
                .andExpect(view().name(errorPage));

        verify(bookService, times(1)).findById(anyLong());
    }

    @Test
    void initCreationForm() throws Exception{
        mockMvc.perform(get("/book/new"))
                .andExpect(status().isOk())
                .andExpect(view().name(bookForm))
                .andExpect(model().attributeExists("book"));

        verifyNoInteractions(bookService);
    }

    @Test
    void processCreationForm() throws Exception{
        Book book = Book.builder().id(1L).name("book").price(BigDecimal.valueOf(1.0)).build();
        when(bookService.save(any())).thenReturn(book);

        mockMvc.perform(post("/book/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", book.getName())
                .param("price", book.getPrice().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/book/" + book.getId() + "/show"));

        verify(bookService, times(1)).save(any());
    }

    @Test
    void processCreationFormWithoutParams() throws Exception{
        mockMvc.perform(post("/book/new"))
                .andExpect(status().isOk())
                .andExpect(view().name(bookForm));

        verifyNoInteractions(bookService);
    }

    @Test
    void initUpdateBookForm() throws Exception{
        when(bookService.findById(anyLong())).thenReturn(Book.builder().id(1L).build());

        mockMvc.perform(get("/book/1/update"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("book"))
                .andExpect(view().name(bookForm));

        verify(bookService, times(1)).findById(anyLong());
    }

    @Test
    void initUpdateBookFormWitBrokenId() throws Exception{
        mockMvc.perform(get("/book/1/update"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("exception"))
                .andExpect(view().name(errorPage));

        verify(bookService, times(1)).findById(anyLong());
    }

    @Test
    void processUpdateBookForm() throws Exception{
        Book book = Book.builder().id(1L).name("book").price(BigDecimal.valueOf(1.0)).build();
        when(bookService.findById(anyLong())).thenReturn(book);
        when(bookService.saveBookSets(any(),any())).thenReturn(book);
        when(bookService.save(any())).thenReturn(book);

        mockMvc.perform(post("/book/1/update")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", book.getName())
                        .param("price", book.getPrice().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/book/" + book.getId() + "/show"));

        verify(bookService, times(1)).saveBookSets(any(), any());
        verify(bookService, times(1)).save(any());
        verify(bookService, times(2)).findById(anyLong());
    }

    @Test
    void processUpdateBookFormWithoutParams() throws Exception{
        when(bookService.findById(anyLong())).thenReturn(Book.builder().build());

        mockMvc.perform(post("/book/1/update"))
                .andExpect(status().isOk())
                .andExpect(view().name(bookForm));

        verify(bookService, times(1)).findById(anyLong());
    }

    @Test
    void processUpdateBookFormWitBrokenId() throws Exception{
        mockMvc.perform(post("/book/1/update"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("exception"))
                .andExpect(view().name(errorPage));

        verify(bookService, times(1)).findById(anyLong());
    }


    @Test
    void deleteBook() throws Exception{
        when(bookService.findById(anyLong())).thenReturn(Book.builder().build());

        mockMvc.perform(get("/book/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/books"));

        verify(bookService).deleteById(anyLong());
    }

    @Test
    void deleteBookWithBrokenId() throws Exception{
        mockMvc.perform(get("/book/1/delete"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("exception"))
                .andExpect(view().name(errorPage));

        verify(bookService, times(1)).findById(anyLong());
    }
}