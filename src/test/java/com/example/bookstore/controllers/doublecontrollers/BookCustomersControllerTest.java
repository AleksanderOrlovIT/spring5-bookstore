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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BookCustomersControllerTest {

    private final static String bookCustomerForm = "book/bookcustomers/bookcustomerform";

    @Mock
    BookService bookService;

    @Mock
    CustomerService customerService;

    @InjectMocks
    BookCustomersController bookCustomersController;

    MockMvc mockMvc;

    Book bookMock = Book.builder().build();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookCustomersController).build();
    }

    @Test
    void getAllBookCustomers() throws Exception{
        Set<Customer> customersSet = new HashSet<>();
        customersSet.add(Customer.builder().id(1L).build());
        customersSet.add(Customer.builder().id(2L).build());
        Book book = Book.builder().id(1L).customers(customersSet).build();

        when(bookService.findById(anyLong())).thenReturn(book);

        mockMvc.perform(get("/book/" + book.getId() + "/customers"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("customers", hasSize(2)))
                .andExpect(model().attributeExists("book"))
                .andExpect(view().name("book/bookcustomers/index"));

        verify(bookService, times(2)).findById(anyLong());
        verifyNoInteractions(customerService);
    }

    @Test
    void initCreationForm() throws Exception{
        when(bookService.findById(anyLong())).thenReturn(bookMock);

        mockMvc.perform(get("/book/1/customer/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("customer"))
                .andExpect(view().name(bookCustomerForm));

        verify(bookService, times(1)).findById(anyLong());
        verifyNoInteractions(customerService);
    }

    @Test
    void processCreationForm() throws Exception{
        Customer customer = Customer.builder().userName("userName").balance(BigDecimal.valueOf(1.0)).build();
        Book book = Book.builder().id(1L).build();

        when(customerService.findByUserName(anyString())).thenReturn(customer);
        when(customerService.save(any())).thenReturn(customer);
        when(bookService.findById(anyLong())).thenReturn(book);
        when(bookService.save(any())).thenReturn(book);

        mockMvc.perform(post("/book/" + book.getId() + "/customer/new")
                        .param("userName", customer.getUserName())
                        .param("balance", customer.getBalance().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/book/" + book.getId() + "/customers"));

        verify(customerService, times(1)).findByUserName(anyString());
        verify(customerService, times(1)).save(any());
        verify(bookService, times(2)).findById(anyLong());
        verify(bookService, times(1)).save(any());
    }

    @Test
    void deleteAuthor() throws Exception{
        Customer customer = Customer.builder().id(1L).build();
        Book book = Book.builder().id(1L).build();
        customer.getBooks().add(book);
        book.getCustomers().add(customer);

        when(customerService.findById(anyLong())).thenReturn(customer);
        when(customerService.save(any())).thenReturn(customer);
        when(bookService.findById(anyLong())).thenReturn(book);
        when(bookService.save(any())).thenReturn(book);

        mockMvc.perform(get("/book/" + book.getId() + "/customer/" + customer.getId() + "/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/book/" + book.getId() + "/customers"));

        verify(customerService, times(1)).findById(anyLong());
        verify(customerService, times(1)).save(any());
        verify(bookService, times(2)).findById(anyLong());
        verify(bookService, times(1)).save(any());
    }
}