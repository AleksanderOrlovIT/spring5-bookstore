package com.example.bookstore.controllers.doublecontrollers;

import com.example.bookstore.model.Book;
import com.example.bookstore.model.Publisher;
import com.example.bookstore.service.BookService;
import com.example.bookstore.service.PublisherService;
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
class BookPublishersControllerTest {

    private final static String bookPublisherForm = "book/bookpublishers/bookpublisherform";

    @Mock
    BookService bookService;

    @Mock
    PublisherService publisherService;

    @InjectMocks
    BookPublishersController bookPublishersController;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookPublishersController).build();
    }

    @Test
    void getAllBookPublishers() throws Exception{
        Set<Publisher> publishersSet = new HashSet<>();
        publishersSet.add(Publisher.builder().id(1L).build());
        publishersSet.add(Publisher.builder().id(2L).build());
        Book book = Book.builder().id(1L).publishers(publishersSet).build();

        when(bookService.findById(anyLong())).thenReturn(book);

        mockMvc.perform(get("/book/" + book.getId() + "/publishers"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("publishers", hasSize(2)))
                .andExpect(model().attributeExists("book"))
                .andExpect(view().name("book/bookpublishers/index"));

        verify(bookService, times(1)).findById(anyLong());
        verifyNoInteractions(publisherService);
    }

    @Test
    void initCreationForm() throws Exception{
        mockMvc.perform(get("/book/1/publisher/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("publisher"))
                .andExpect(view().name(bookPublisherForm));

        verifyNoInteractions(bookService);
        verifyNoInteractions(publisherService);
    }

    @Test
    void processCreationForm() throws Exception{
        Publisher publisher = Publisher.builder().id(1L).name("name").address("address").build();
        Book book = Book.builder().id(1L).build();

        when(publisherService.findByName(anyString())).thenReturn(publisher);
        when(publisherService.save(any())).thenReturn(publisher);
        when(bookService.findById(anyLong())).thenReturn(book);
        when(bookService.save(any())).thenReturn(book);

        mockMvc.perform(post("/book/" + book.getId() + "/publisher/new")
                        .param("name", publisher.getName())
                        .param("address", publisher.getAddress()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/book/" + book.getId() + "/publishers"));

        verify(publisherService, times(1)).findByName(anyString());
        verify(publisherService, times(1)).save(any());
        verify(bookService, times(1)).findById(anyLong());
        verify(bookService, times(1)).save(any());
    }

    @Test
    void deletePublisher() throws Exception{
        Publisher publisher = Publisher.builder().id(1L).build();
        Book book = Book.builder().id(1L).build();
        publisher.getBooks().add(book);
        book.getPublishers().add(publisher);

        when(publisherService.findById(anyLong())).thenReturn(publisher);
        when(publisherService.save(any())).thenReturn(publisher);
        when(bookService.findById(anyLong())).thenReturn(book);
        when(bookService.save(any())).thenReturn(book);

        mockMvc.perform(get("/book/" + book.getId() + "/publisher/" + publisher.getId() + "/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/book/" + book.getId() + "/publishers"));

        verify(publisherService, times(1)).findById(anyLong());
        verify(publisherService, times(1)).save(any());
        verify(bookService, times(1)).findById(anyLong());
        verify(bookService, times(1)).save(any());
    }
}