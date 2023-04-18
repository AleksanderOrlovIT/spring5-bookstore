package com.example.bookstore.controllers;

import com.example.bookstore.model.Genre;
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

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class GenreControllerTest {

    private static final String genreForm = "/genre/genreform";

    @Mock
    GenreService genreService;

    @InjectMocks
    GenreController genreController;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(genreController).build();
    }

    @Test
    void getAllAuthors() throws Exception{
        Set<Genre> genres = new HashSet<>();
        genres.add(Genre.builder().id(1L).build());
        genres.add(Genre.builder().id(2L).build());
        when(genreService.findAll()).thenReturn(genres);

        mockMvc.perform(get("/genres"))
                .andExpect(status().isOk())
                .andExpect(view().name("genre/index"))
                .andExpect(model().attribute("genres", hasSize(2)));

        verify(genreService, times(1)).findAll();
    }

    @Test
    void showAuthorById() throws Exception{
        when(genreService.findById(anyLong())).thenReturn(Genre.builder().id(1L).build());

        mockMvc.perform(get("/genre/1/show"))
                .andExpect(status().isOk())
                .andExpect(view().name("genre/show"))
                .andExpect(model().attribute("genre", hasProperty("id", is(1L))));

        verify(genreService, times(1)).findById(anyLong());
    }

    @Test
    void initCreationForm() throws Exception{
        mockMvc.perform(get("/genre/new"))
                .andExpect(status().isOk())
                .andExpect(view().name(genreForm))
                .andExpect(model().attributeExists("genre"));

        verifyNoInteractions(genreService);
    }

    @Test
    void processCreationForm() throws Exception{
        Genre genre = Genre.builder().id(1L).name("name").build();
        when(genreService.save(any())).thenReturn(genre);

        mockMvc.perform(post("/genre/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", genre.getName()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/genre/" + genre.getId() + "/show"));

        verify(genreService, times(1)).save(any());
    }

    @Test
    void processCreationFormWithoutParams() throws Exception{
        mockMvc.perform(post("/genre/new"))
                .andExpect(status().isOk())
                .andExpect(view().name(genreForm));

        verifyNoInteractions(genreService);
    }

    @Test
    void initUpdateAuthorForm() throws Exception{
        when(genreService.findById(anyLong())).thenReturn(Genre.builder().id(1L).build());

        mockMvc.perform(get("/genre/1/update"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("genre"))
                .andExpect(view().name(genreForm));

        verify(genreService, times(1)).findById(anyLong());
    }


    @Test
    void processUpdateAuthorForm() throws Exception{
        Genre genre = Genre.builder().id(1L).name("name").build();
        when(genreService.save(any())).thenReturn(genre);

        mockMvc.perform(post("/genre/1/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", genre.getName()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/genre/" + genre.getId() + "/show"));

        verify(genreService, times(1)).save(any());
    }

    @Test
    void processUpdateAuthorFormWithoutParams() throws Exception{
        mockMvc.perform(post("/genre/1/update"))
                .andExpect(status().isOk())
                .andExpect(view().name(genreForm));
        verifyNoInteractions(genreService);
    }

    @Test
    void deleteAuthor() throws Exception{
        mockMvc.perform(get("/genre/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/genres"));

        verify(genreService).deleteById(anyLong());
    }
}