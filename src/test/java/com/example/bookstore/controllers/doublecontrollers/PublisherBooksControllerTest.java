package com.example.bookstore.controllers.doublecontrollers;

import com.example.bookstore.controllers.PublisherController;
import com.example.bookstore.model.Author;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
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
class PublisherBooksControllerTest {

    private final static String publisherBookForm = "publisher/publisherbooks/publisherbookform";

    @Mock
    BookService bookService;

    @Mock
    PublisherService publisherService;

    @InjectMocks
    PublisherBooksController publisherBooksController;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(publisherBooksController).build();
    }

    @Test
    void getAllPublishersBooks() throws Exception{
        Set<Book> booksSet = new HashSet<>();
        booksSet.add(Book.builder().id(1L).build());
        booksSet.add(Book.builder().id(2L).build());
        Publisher publisher = Publisher.builder().id(1L).books(booksSet).build();

        when(publisherService.findById(anyLong())).thenReturn(publisher);

        mockMvc.perform(get("/publisher/" + publisher.getId() + "/books"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("books", hasSize(2)))
                .andExpect(model().attributeExists("publisher"))
                .andExpect(view().name("publisher/publisherbooks/index"));

        verify(publisherService, times(1)).findById(anyLong());
        verifyNoInteractions(bookService);
    }

    @Test
    void initCreationForm() throws Exception{
        mockMvc.perform(get("/publisher/1/book/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("book"))
                .andExpect(view().name(publisherBookForm));

        verifyNoInteractions(publisherService);
        verifyNoInteractions(bookService);
    }

    @Test
    void processCreationForm() throws Exception{
        Publisher publisher = Publisher.builder().id(1L).build();
        Book book = Book.builder().name("book").price(BigDecimal.valueOf(1.0)).build();

        when(publisherService.save(any())).thenReturn(publisher);
        when(publisherService.findById(anyLong())).thenReturn(publisher);
        when(bookService.save(any())).thenReturn(book);
        when(bookService.findByName(anyString())).thenReturn(book);

        mockMvc.perform(post("/publisher/" + publisher.getId() + "/book/new")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", book.getName())
                        .param("price", book.getPrice().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/publisher/" + publisher.getId() + "/books"));

        verify(publisherService, times(1)).save(any());
        verify(publisherService, times(1)).findById(anyLong());
        verify(bookService, times(1)).save(any());
        verify(bookService, times(1)).findByName(anyString());
    }

    @Test
    void deleteBook() throws Exception{
        Publisher publisher = Publisher.builder().id(1L).build();
        Book book = Book.builder().id(1L).build();
        publisher.getBooks().add(book);
        book.getPublishers().add(publisher);

        when(publisherService.findById(anyLong())).thenReturn(publisher);
        when(publisherService.save(any())).thenReturn(publisher);
        when(bookService.findById(anyLong())).thenReturn(book);
        when(bookService.save(any())).thenReturn(book);

        mockMvc.perform(get("/publisher/" + publisher.getId() + "/book/" + book.getId() + "/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/publisher/" + publisher.getId() + "/books"));

        verify(publisherService, times(1)).save(any());
        verify(publisherService, times(1)).findById(anyLong());
        verify(bookService, times(1)).save(any());
        verify(bookService, times(1)).findById(anyLong());
    }
}