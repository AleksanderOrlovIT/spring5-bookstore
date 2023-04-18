package com.example.bookstore.controllers;

import com.example.bookstore.model.Publisher;
import com.example.bookstore.service.PublisherService;
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
class PublisherControllerTest {

    private static final String publisherForm = "/publisher/publisherform";

    @Mock
    PublisherService publisherService;

    @InjectMocks
    PublisherController publisherController;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(publisherController).build();
    }

    @Test
    void getAllPublishers() throws Exception{
        Set<Publisher> publishers = new HashSet<>();
        publishers.add(Publisher.builder().id(1L).build());
        publishers.add(Publisher.builder().id(2L).build());

        when(publisherService.findAll()).thenReturn(publishers);

        mockMvc.perform(get("/publishers"))
                .andExpect(status().isOk())
                .andExpect(view().name("publisher/index"))
                .andExpect(model().attribute("publishers", hasSize(2)));

        verify(publisherService, times(1)).findAll();
    }

    @Test
    void showPublisherById() throws Exception{
        when(publisherService.findById(anyLong())).thenReturn(Publisher.builder().id(1L).build());

        mockMvc.perform(get("/publisher/1/show"))
                .andExpect(status().isOk())
                .andExpect(view().name("publisher/show"))
                .andExpect(model().attribute("publisher", hasProperty("id", is(1L))));

        verify(publisherService, times(1)).findById(anyLong());
    }

    @Test
    void initCreationForm() throws Exception{
        mockMvc.perform(get("/publisher/new"))
                .andExpect(status().isOk())
                .andExpect(view().name(publisherForm))
                .andExpect(model().attributeExists("publisher"));

        verifyNoInteractions(publisherService);
    }

    @Test
    void processCreationForm() throws Exception{
        Publisher publisher = Publisher.builder().id(1L).name("publisher").address("address").build();
        when(publisherService.save(any())).thenReturn(publisher);

        mockMvc.perform(post("/publisher/new")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", publisher.getName())
                        .param("address", publisher.getAddress()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/publisher/" + publisher.getId() + "/show"));

        verify(publisherService, times(1)).save(any());
    }

    @Test
    void processCreationFormWithoutParams() throws Exception{
        mockMvc.perform(post("/publisher/new"))
                .andExpect(status().isOk())
                .andExpect(view().name(publisherForm));

        verifyNoInteractions(publisherService);
    }

    @Test
    void initUpdatePublisherForm() throws Exception{
        when(publisherService.findById(anyLong())).thenReturn(Publisher.builder().id(1L).build());

        mockMvc.perform(get("/publisher/1/update"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("publisher"))
                .andExpect(view().name(publisherForm));

        verify(publisherService, times(1)).findById(anyLong());
    }

    @Test
    void processUpdatePublisherForm() throws Exception{
        Publisher publisher = Publisher.builder().id(1L).name("publisher").address("address").build();
        when(publisherService.save(any())).thenReturn(publisher);

        mockMvc.perform(post("/publisher/1/update")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", publisher.getName())
                        .param("address", publisher.getAddress()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/publisher/" + publisher.getId() + "/show"));

        verify(publisherService, times(1)).save(any());
    }

    @Test
    void processUpdatePublisherFormWithoutParams() throws Exception{
        mockMvc.perform(post("/publisher/1/update"))
                .andExpect(status().isOk())
                .andExpect(view().name(publisherForm));
        verifyNoInteractions(publisherService);
    }

    @Test
    void deletePublisher() throws Exception{
        mockMvc.perform(get("/publisher/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/publishers"));

        verify(publisherService).deleteById(anyLong());
    }
}