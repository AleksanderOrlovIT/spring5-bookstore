package com.example.bookstore.controllers;

import com.example.bookstore.model.Author;
import com.example.bookstore.service.AuthorService;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthorControllerTest {

    private static final String authorForm = "/author/authorform";

    private static final String errorPage = "/error/400error";

    @Mock
    AuthorService authorService;

    @InjectMocks
    AuthorController authorController;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authorController).build();
    }

    @Test
    void getAllAuthors() throws Exception{
        Set<Author> authors = new HashSet<>();
        authors.add(Author.builder().id(1L).build());
        authors.add(Author.builder().id(2L).build());
        when(authorService.findAll()).thenReturn(authors);

        mockMvc.perform(get("/authors"))
                .andExpect(status().isOk())
                .andExpect(view().name("author/index"))
                .andExpect(model().attribute("authors", hasSize(2)));

        verify(authorService, times(1)).findAll();
    }

    @Test
    void showAuthorById() throws Exception{
        when(authorService.findById(anyLong())).thenReturn(Author.builder().id(1L).build());

        mockMvc.perform(get("/author/1/show"))
                .andExpect(status().isOk())
                .andExpect(view().name("author/show"))
                .andExpect(model().attribute("author", hasProperty("id", is(1L))));

        verify(authorService, times(1)).findById(anyLong());
    }

    @Test
    void showAuthorWithBrokenId() throws Exception{
        mockMvc.perform(get("/author/1/show"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("exception"))
                .andExpect(view().name(errorPage));

        verify(authorService, times(1)).findById(anyLong());
    }

    @Test
    void initCreationForm() throws Exception{
        mockMvc.perform(get("/author/new"))
                .andExpect(status().isOk())
                .andExpect(view().name(authorForm))
                .andExpect(model().attributeExists("author"));

        verifyNoInteractions(authorService);
    }

    @Test
    void processCreationForm() throws Exception{
        Author author = Author.builder().id(1L).firstName("first").lastName("last").build();
        when(authorService.save(any())).thenReturn(author);

        mockMvc.perform(post("/author/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("firstName", author.getFirstName())
                .param("lastName", author.getLastName()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/author/" + author.getId() + "/show"));

        verify(authorService, times(1)).save(any());
    }

    @Test
    void processCreationFormWithoutParams() throws Exception{
        mockMvc.perform(post("/author/new"))
                .andExpect(status().isOk())
                .andExpect(view().name(authorForm));

        verifyNoInteractions(authorService);
    }

    @Test
    void initUpdateAuthorForm() throws Exception{
        when(authorService.findById(anyLong())).thenReturn(Author.builder().id(1L).build());

        mockMvc.perform(get("/author/1/update"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("author"))
                .andExpect(view().name(authorForm));

        verify(authorService, times(1)).findById(anyLong());
    }

    @Test
    void initUpdateAuthorFormWithBrokenId() throws Exception{
        mockMvc.perform(get("/author/1/update"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("exception"))
                .andExpect(view().name(errorPage));

        verify(authorService, times(1)).findById(anyLong());
    }


    @Test
    void processUpdateAuthorForm() throws Exception{
        Author author = Author.builder().id(1L).firstName("first").lastName("last").build();
        when(authorService.findById(anyLong())).thenReturn(author);
        when(authorService.save(any())).thenReturn(author);

        mockMvc.perform(post("/author/1/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("firstName", author.getFirstName())
                .param("lastName", author.getLastName()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/author/" + author.getId() + "/show"));

        verify(authorService, times(1)).save(any());
    }

    @Test
    void processUpdateAuthorFormWithoutParams() throws Exception{
        when(authorService.findById(anyLong())).thenReturn(Author.builder().build());

        mockMvc.perform(post("/author/1/update"))
                .andExpect(status().isOk())
                .andExpect(view().name(authorForm));
        verify(authorService, times(1)).findById(anyLong());
    }

    @Test
    void processUpdateAuthorFormWithBrokenId() throws Exception{
        mockMvc.perform(post("/author/1/update"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("exception"))
                .andExpect(view().name(errorPage));

        verify(authorService, times(1)).findById(anyLong());
    }

    @Test
    void deleteAuthor() throws Exception{
        when(authorService.findById(anyLong())).thenReturn(Author.builder().build());

        mockMvc.perform(get("/author/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/authors"));

        verify(authorService).deleteById(anyLong());
    }

    @Test
    void deleteAuthorWithBrokenId() throws Exception{
        mockMvc.perform(get("/author/1/delete"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("exception"))
                .andExpect(view().name(errorPage));

        verify(authorService, times(1)).findById(anyLong());
    }
}