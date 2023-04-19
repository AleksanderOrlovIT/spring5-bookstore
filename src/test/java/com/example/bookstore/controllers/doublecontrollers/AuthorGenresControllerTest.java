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
class AuthorGenresControllerTest {

    private final static String authorGenreForm = "author/authorgenres/authorgenreform";

    private static final String errorPage = "/error/400error";

    @Mock
    GenreService genreService;

    @Mock
    AuthorService authorService;

    @InjectMocks
    AuthorGenresController authorGenresController;

    MockMvc mockMvc;

    Author authorMock = Author.builder().build();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authorGenresController).build();
    }

    @Test
    void getAllAuthorsGenres() throws Exception{
        Set<Genre> genresSet = new HashSet<>();
        genresSet.add(Genre.builder().id(1L).build());
        genresSet.add(Genre.builder().id(2L).build());
        Author author = Author.builder().id(1L).genres(genresSet).build();

        when(authorService.findById(anyLong())).thenReturn(author);

        mockMvc.perform(get("/author/" + author.getId() + "/genres"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("genres", hasSize(2)))
                .andExpect(model().attributeExists("author"))
                .andExpect(view().name("author/authorgenres/index"));

        verify(authorService, times(2)).findById(anyLong());
        verifyNoInteractions(genreService);
    }

    @Test
    void getAllAuthorsGenresWithBrokenAuthorId() throws Exception{
        mockMvc.perform(get("/author/1/genres"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("exception"))
                .andExpect(view().name(errorPage));

        verify(authorService, times(1)).findById(anyLong());
    }

    @Test
    void initCreationForm() throws Exception{
        when(authorService.findById(anyLong())).thenReturn(authorMock);

        mockMvc.perform(get("/author/1/genre/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("genre"))
                .andExpect(view().name(authorGenreForm));

        verify(authorService, times(1)).findById(anyLong());
        verifyNoInteractions(genreService);
    }

    @Test
    void initCreationFormWithBrokenAuthorId() throws Exception{
        mockMvc.perform(get("/author/1/genre/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("exception"))
                .andExpect(view().name(errorPage));

        verify(authorService, times(1)).findById(anyLong());
    }

    @Test
    void processCreationForm() throws Exception{
        Author author = Author.builder().id(1L).build();
        Genre genre = Genre.builder().name("name").build();

        when(authorService.save(any())).thenReturn(author);
        when(authorService.findById(anyLong())).thenReturn(author);
        when(genreService.save(any())).thenReturn(genre);
        when(genreService.findByName(anyString())).thenReturn(genre);

        mockMvc.perform(post("/author/" + author.getId() + "/genre/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", genre.getName()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/author/" + author.getId() + "/genres"));

        verify(authorService, times(1)).save(any());
        verify(authorService, times(2)).findById(anyLong());
        verify(genreService, times(1)).save(any());
        verify(genreService, times(1)).findByName(anyString());
    }

    @Test
    void processCreationFormWithBrokenAuthorId() throws Exception{
        mockMvc.perform(post("/author/1/genre/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("exception"))
                .andExpect(view().name(errorPage));

        verify(authorService, times(1)).findById(anyLong());
    }

    @Test
    void deleteGenre() throws Exception{
        Author author = Author.builder().id(1L).build();
        Genre genre = Genre.builder().id(1L).build();
        author.getGenres().add(genre);
        genre.getAuthors().add(author);

        when(authorService.findById(anyLong())).thenReturn(author);
        when(authorService.save(any())).thenReturn(author);
        when(genreService.findById(anyLong())).thenReturn(genre);
        when(genreService.save(any())).thenReturn(genre);

        mockMvc.perform(get("/author/" + author.getId() + "/genre/" + genre.getId() + "/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/author/" + author.getId() + "/genres"));

        verify(authorService, times(1)).save(any());
        verify(authorService, times(2)).findById(anyLong());
        verify(genreService, times(1)).save(any());
        verify(genreService, times(1)).findById(anyLong());
    }

    @Test
    void deleteGenreWithBrokenAuthorId() throws Exception{
        mockMvc.perform(get("/author/1/genre/1/delete"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("exception"))
                .andExpect(view().name(errorPage));

        verify(authorService, times(1)).findById(anyLong());
    }

    @Test
    void deleteGenreWithBrokenGenreId() throws Exception{
        when(authorService.findById(anyLong())).thenReturn(authorMock);

        mockMvc.perform(get("/author/1/genre/1/delete"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("exception"))
                .andExpect(view().name(errorPage));

        verify(authorService, times(1)).findById(anyLong());
        verify(genreService, times(1)).findById(anyLong());
    }
}