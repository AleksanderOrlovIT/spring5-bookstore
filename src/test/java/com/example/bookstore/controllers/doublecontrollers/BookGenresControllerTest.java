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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BookGenresControllerTest {

    private final static String bookGenreForm = "book/bookgenres/bookgenreform";

    @Mock
    BookService bookService;

    @Mock
    GenreService genreService;

    @InjectMocks
    BookGenresController bookGenresController;

    MockMvc mockMvc;

    Book bookMock = Book.builder().build();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookGenresController).build();
    }

    @Test
    void getAllBookCustomers() throws Exception{
        Set<Genre> genresSet = new HashSet<>();
        genresSet.add(Genre.builder().id(1L).build());
        genresSet.add(Genre.builder().id(2L).build());
        Book book = Book.builder().id(1L).genres(genresSet).build();

        when(bookService.findById(anyLong())).thenReturn(book);

        mockMvc.perform(get("/book/" + book.getId() + "/genres"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("genres", hasSize(2)))
                .andExpect(model().attributeExists("book"))
                .andExpect(view().name("book/bookgenres/index"));

        verify(bookService, times(2)).findById(anyLong());
        verifyNoInteractions(genreService);
    }

    @Test
    void initCreationForm() throws Exception{
        when(bookService.findById(anyLong())).thenReturn(bookMock);

        mockMvc.perform(get("/book/1/genre/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("genre"))
                .andExpect(view().name(bookGenreForm));

        verify(bookService, times(1)).findById(anyLong());
        verifyNoInteractions(genreService);
    }

    @Test
    void processCreationForm() throws Exception{
        Genre genre = Genre.builder().name("name").build();
        Book book = Book.builder().id(1L).build();

        when(genreService.findByName(anyString())).thenReturn(genre);
        when(genreService.save(any())).thenReturn(genre);
        when(bookService.findById(anyLong())).thenReturn(book);
        when(bookService.save(any())).thenReturn(book);

        mockMvc.perform(post("/book/" + book.getId() + "/genre/new")
                        .param("name", genre.getName()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/book/" + book.getId() + "/genres"));

        verify(genreService, times(1)).findByName(anyString());
        verify(genreService, times(1)).save(any());
        verify(bookService, times(2)).findById(anyLong());
        verify(bookService, times(1)).save(any());
    }

    @Test
    void deleteAuthor() throws Exception{
        Genre genre = Genre.builder().id(1L).build();
        Book book = Book.builder().id(1L).build();
        genre.getBooks().add(book);
        book.getGenres().add(genre);

        when(genreService.findById(anyLong())).thenReturn(genre);
        when(genreService.save(any())).thenReturn(genre);
        when(bookService.findById(anyLong())).thenReturn(book);
        when(bookService.save(any())).thenReturn(book);

        mockMvc.perform(get("/book/" + book.getId() + "/genre/" + genre.getId() + "/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/book/" + book.getId() + "/genres"));

        verify(genreService, times(1)).findById(anyLong());
        verify(genreService, times(1)).save(any());
        verify(bookService, times(2)).findById(anyLong());
        verify(bookService, times(1)).save(any());
    }
}