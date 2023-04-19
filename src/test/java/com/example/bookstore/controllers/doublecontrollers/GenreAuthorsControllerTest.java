package com.example.bookstore.controllers.doublecontrollers;

import com.example.bookstore.model.Author;
import com.example.bookstore.model.Genre;
import com.example.bookstore.service.AuthorService;
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
class GenreAuthorsControllerTest {

    private final static String genreAuthorForm = "genre/genreauthors/genreauthorform";

    private static final String errorPage = "/error/400error";

    @Mock
    AuthorService authorService;

    @Mock
    GenreService genreService;

    @InjectMocks
    GenreAuthorsController genreAuthorsController;

    MockMvc mockMvc;

    Genre genreMock = Genre.builder().build();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(genreAuthorsController).build();
    }

    @Test
    void getAllGenresAuthors() throws Exception{
        Set<Author> authorsSet = new HashSet<>();
        authorsSet.add(Author.builder().id(1L).build());
        authorsSet.add(Author.builder().id(2L).build());
        Genre genre = Genre.builder().id(1L).authors(authorsSet).build();

        when(genreService.findById(anyLong())).thenReturn(genre);

        mockMvc.perform(get("/genre/" + genre.getId() + "/authors"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("authors", hasSize(2)))
                .andExpect(model().attributeExists("genre"))
                .andExpect(view().name("genre/genreauthors/index"));

        verify(genreService, times(2)).findById(anyLong());
        verifyNoInteractions(authorService);
    }

    @Test
    void getAllGenresAuthorsWithBrokenGenreId() throws Exception{
        mockMvc.perform(get("/genre/1/authors"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("exception"))
                .andExpect(view().name(errorPage));

        verify(genreService, times(1)).findById(anyLong());
    }

    @Test
    void initCreationForm() throws Exception{
        when(genreService.findById(anyLong())).thenReturn(genreMock);

        mockMvc.perform(get("/genre/1/author/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("author"))
                .andExpect(view().name(genreAuthorForm));

        verify(genreService, times(1)).findById(anyLong());
        verifyNoInteractions(authorService);
    }

    @Test
    void initCreationFormWithBrokenGenreId() throws Exception{
        mockMvc.perform(get("/genre/1/author/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("exception"))
                .andExpect(view().name(errorPage));

        verify(genreService, times(1)).findById(anyLong());
    }

    @Test
    void processCreationForm() throws Exception{
        Genre genre = Genre.builder().id(1L).build();
        Author author = Author.builder().firstName("first").lastName("last").build();

        when(genreService.save(any())).thenReturn(genre);
        when(genreService.findById(anyLong())).thenReturn(genre);
        when(authorService.save(any())).thenReturn(author);
        when(authorService.findByFullName(anyString(), anyString())).thenReturn(author);

        mockMvc.perform(post("/genre/" + genre.getId() + "/author/new")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("firstName", author.getFirstName())
                        .param("lastName", author.getLastName()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/genre/" + genre.getId() + "/authors"));

        verify(genreService, times(1)).save(any());
        verify(genreService, times(2)).findById(anyLong());
        verify(authorService, times(1)).save(any());
        verify(authorService, times(1)).findByFullName(anyString(), anyString());
    }

    @Test
    void processCreationFormWithBrokenGenreId() throws Exception{
        mockMvc.perform(post("/genre/1/author/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("exception"))
                .andExpect(view().name(errorPage));

        verify(genreService, times(1)).findById(anyLong());
    }

    @Test
    void deleteGenresAuthor() throws Exception{
        Genre genre = Genre.builder().id(1L).build();
        Author author = Author.builder().id(1L).build();
        genre.getAuthors().add(author);
        author.getGenres().add(genre);

        when(genreService.findById(anyLong())).thenReturn(genre);
        when(genreService.save(any())).thenReturn(genre);
        when(authorService.findById(anyLong())).thenReturn(author);
        when(authorService.save(any())).thenReturn(author);

        mockMvc.perform(get("/genre/" + genre.getId() + "/author/" + author.getId() + "/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/genre/" + genre.getId() + "/authors"));

        verify(genreService, times(1)).save(any());
        verify(genreService, times(2)).findById(anyLong());
        verify(authorService, times(1)).save(any());
        verify(authorService, times(1)).findById(anyLong());
    }

    @Test
    void deleteGenresAuthorWithBrokenGenreId() throws Exception{
        mockMvc.perform(get("/genre/1/author/1/delete"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("exception"))
                .andExpect(view().name(errorPage));

        verify(genreService, times(1)).findById(anyLong());
    }

    @Test
    void deleteGenresAuthorWithBrokenAuthorId() throws Exception{
        when(genreService.findById(anyLong())).thenReturn(genreMock);

        mockMvc.perform(get("/genre/1/author/1/delete"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("exception"))
                .andExpect(view().name(errorPage));

        verify(genreService, times(1)).findById(anyLong());
        verify(authorService, times(1)).findById(anyLong());
    }
}