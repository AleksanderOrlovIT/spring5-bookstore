package com.example.bookstore.controllers.doublecontrollers;

import com.example.bookstore.model.Book;
import com.example.bookstore.model.Customer;
import com.example.bookstore.service.BookService;
import com.example.bookstore.service.CustomerService;
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
class CustomerBooksControllerTest {

    private final static String customerBookForm = "customer/customerbooks/customerbookform";

    private static final String errorPage = "/error/400error";

    @Mock
    BookService bookService;

    @Mock
    CustomerService customerService;

    @InjectMocks
    CustomerBooksController customerBooksController;

    MockMvc mockMvc;

    Customer customerMock = Customer.builder().build();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(customerBooksController).build();
    }

    @Test
    void getAllCustomersBooks() throws Exception{
        Set<Book> booksSet = new HashSet<>();
        booksSet.add(Book.builder().id(1L).build());
        booksSet.add(Book.builder().id(2L).build());
        Customer customer = Customer.builder().id(1L).books(booksSet).build();

        when(customerService.findById(anyLong())).thenReturn(customer);

        mockMvc.perform(get("/customer/" + customer.getId() + "/books"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("books", hasSize(2)))
                .andExpect(model().attributeExists("customer"))
                .andExpect(view().name("customer/customerbooks/index"));

        verify(customerService, times(2)).findById(anyLong());
        verifyNoInteractions(bookService);
    }

    @Test
    void getAllCustomersBooksWithBrokenCustomerId() throws Exception{
        mockMvc.perform(get("/customer/1/books"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("exception"))
                .andExpect(view().name(errorPage));

        verify(customerService, times(1)).findById(anyLong());
    }

    @Test
    void initCreationForm() throws Exception{
        when(customerService.findById(anyLong())).thenReturn(customerMock);
        mockMvc.perform(get("/customer/1/book/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("book"))
                .andExpect(view().name(customerBookForm));

        verify(customerService, times(1)).findById(anyLong());
        verifyNoInteractions(bookService);
    }

    @Test
    void initCreationFormWithBrokenCustomerId() throws Exception{
        mockMvc.perform(get("/customer/1/book/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("exception"))
                .andExpect(view().name(errorPage));

        verify(customerService, times(1)).findById(anyLong());
    }

    @Test
    void processCreationForm() throws Exception{
        Customer customer = Customer.builder().id(1L).build();
        Book book = Book.builder().name("book").price(BigDecimal.valueOf(1.0)).build();

        when(customerService.save(any())).thenReturn(customer);
        when(customerService.findById(anyLong())).thenReturn(customer);
        when(bookService.save(any())).thenReturn(book);
        when(bookService.findByName(anyString())).thenReturn(book);

        mockMvc.perform(post("/customer/" + customer.getId() + "/book/new")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", book.getName())
                        .param("price", book.getPrice().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/customer/" + customer.getId() + "/books"));

        verify(customerService, times(1)).save(any());
        verify(customerService, times(2)).findById(anyLong());
        verify(bookService, times(1)).save(any());
        verify(bookService, times(1)).findByName(anyString());
    }

    @Test
    void processCreationFormWithBrokenCustomerId() throws Exception{
        mockMvc.perform(post("/customer/1/book/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("exception"))
                .andExpect(view().name(errorPage));

        verify(customerService, times(1)).findById(anyLong());
    }

    @Test
    void deleteCustomerBook() throws Exception{
        Customer customer = Customer.builder().id(1L).build();
        Book book = Book.builder().id(1L).build();
        customer.getBooks().add(book);
        book.getCustomers().add(customer);

        when(customerService.findById(anyLong())).thenReturn(customer);
        when(customerService.save(any())).thenReturn(customer);
        when(bookService.findById(anyLong())).thenReturn(book);
        when(bookService.save(any())).thenReturn(book);

        mockMvc.perform(get("/customer/" + customer.getId() + "/book/" + book.getId() + "/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/customer/" + customer.getId() + "/books"));

        verify(customerService, times(1)).save(any());
        verify(customerService, times(2)).findById(anyLong());
        verify(bookService, times(1)).save(any());
        verify(bookService, times(1)).findById(anyLong());
    }

    @Test
    void deleteCustomerBookWithBrokenCustomerId() throws Exception{
        mockMvc.perform(get("/customer/1/book/1/delete"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("exception"))
                .andExpect(view().name(errorPage));

        verify(customerService, times(1)).findById(anyLong());
    }

    @Test
    void deleteCustomerBookWithBrokenBookId() throws Exception{
        when(customerService.findById(anyLong())).thenReturn(customerMock);

        mockMvc.perform(get("/customer/1/book/1/delete"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("exception"))
                .andExpect(view().name(errorPage));

        verify(customerService, times(1)).findById(anyLong());
        verify(bookService, times(1)).findById(anyLong());
    }
}