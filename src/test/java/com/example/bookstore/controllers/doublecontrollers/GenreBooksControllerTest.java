package com.example.bookstore.controllers.doublecontrollers;

import com.example.bookstore.model.Book;
import com.example.bookstore.model.Genre;
import com.example.bookstore.service.BookService;
import com.example.bookstore.service.GenreService;
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

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class GenreBooksControllerTest {

    private final static String genreBookForm = "genre/genrebooks/genrebookform";

    @Mock
    BookService bookService;

    @Mock
    GenreService genreService;

    @InjectMocks
    GenreBooksController genreBooksController;

    MockMvc mockMvc;

    Genre genreMock = Genre.builder().build();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(genreBooksController).build();
    }

    @Test
    void getAllCustomersBooks() throws Exception{
        Set<Book> booksSet = new HashSet<>();
        booksSet.add(Book.builder().id(1L).build());
        booksSet.add(Book.builder().id(2L).build());
        Genre genre = Genre.builder().id(1L).books(booksSet).build();

        when(genreService.findById(anyLong())).thenReturn(genre);

        mockMvc.perform(get("/genre/" + genre.getId() + "/books"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("books", hasSize(2)))
                .andExpect(model().attributeExists("genre"))
                .andExpect(view().name("genre/genrebooks/index"));

        verify(genreService, times(2)).findById(anyLong());
        verifyNoInteractions(bookService);
    }

    @Test
    void initCreationForm() throws Exception{
        when(genreService.findById(anyLong())).thenReturn(genreMock);

        mockMvc.perform(get("/genre/1/book/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("book"))
                .andExpect(view().name(genreBookForm));

        verify(genreService, times(1)).findById(anyLong());
        verifyNoInteractions(bookService);
    }

    @Test
    void processCreationForm() throws Exception{
        Genre genre = Genre.builder().id(1L).build();
        Book book = Book.builder().name("book").price(BigDecimal.valueOf(1.0)).build();

        when(genreService.save(any())).thenReturn(genre);
        when(genreService.findById(anyLong())).thenReturn(genre);
        when(bookService.save(any())).thenReturn(book);
        when(bookService.findByName(anyString())).thenReturn(book);

        mockMvc.perform(post("/genre/" + genre.getId() + "/book/new")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", book.getName())
                        .param("price", book.getPrice().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/genre/" + genre.getId() + "/books"));

        verify(genreService, times(1)).save(any());
        verify(genreService, times(2)).findById(anyLong());
        verify(bookService, times(1)).save(any());
        verify(bookService, times(1)).findByName(anyString());
    }

    @Test
    void deleteBook() throws Exception{
        Genre genre = Genre.builder().id(1L).build();
        Book book = Book.builder().id(1L).build();
        genre.getBooks().add(book);
        book.getGenres().add(genre);

        when(genreService.findById(anyLong())).thenReturn(genre);
        when(genreService.save(any())).thenReturn(genre);
        when(bookService.findById(anyLong())).thenReturn(book);
        when(bookService.save(any())).thenReturn(book);

        mockMvc.perform(get("/genre/" + genre.getId() + "/book/" + book.getId() + "/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/genre/" + genre.getId() + "/books"));

        verify(genreService, times(1)).save(any());
        verify(genreService, times(2)).findById(anyLong());
        verify(bookService, times(1)).save(any());
        verify(bookService, times(1)).findById(anyLong());
    }
}