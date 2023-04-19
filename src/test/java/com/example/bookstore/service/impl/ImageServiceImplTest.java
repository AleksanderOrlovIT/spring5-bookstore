package com.example.bookstore.service.impl;

import com.example.bookstore.model.Author;
import com.example.bookstore.model.Book;
import com.example.bookstore.model.Customer;
import com.example.bookstore.model.Publisher;
import com.example.bookstore.repositories.AuthorRepository;
import com.example.bookstore.repositories.BookRepository;
import com.example.bookstore.repositories.CustomerRepository;
import com.example.bookstore.repositories.PublisherRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImageServiceImplTest {

    @Mock
    BookRepository bookRepository;

    @Mock
    AuthorRepository authorRepository;

    @Mock
    PublisherRepository publisherRepository;

    @Mock
    CustomerRepository customerRepository;

    @InjectMocks
    ImageServiceImpl imageService;

    @Test
    void saveBookImage() throws Exception{
        //given
        MultipartFile multipartFile = new MockMultipartFile("imagefile", "testing.txt",
                "text/plain", "NewBook".getBytes());

        Book book = Book.builder().build();

        ArgumentCaptor<Book> argumentCaptor = ArgumentCaptor.forClass(Book.class);

        //when
        imageService.saveBookImage(book, multipartFile);

        //then
        verify(bookRepository, times(1)).save(argumentCaptor.capture());
        Book savedBook = argumentCaptor.getValue();
        assertEquals(multipartFile.getBytes().length, savedBook.getImage().length);
    }

    @Test
    void saveAuthorImage() throws Exception{
        //given
        MultipartFile multipartFile = new MockMultipartFile("imagefile", "testing.txt",
                "text/plain", "NewAuthor".getBytes());

        Author author = Author.builder().build();

        ArgumentCaptor<Author> argumentCaptor = ArgumentCaptor.forClass(Author.class);

        //when
        imageService.saveAuthorImage(author, multipartFile);

        //then
        verify(authorRepository, times(1)).save(argumentCaptor.capture());
        Author savedAuthor = argumentCaptor.getValue();
        assertEquals(multipartFile.getBytes().length, savedAuthor.getImage().length);
    }

    @Test
    void savePublisherImage() throws Exception{
        //given
        MultipartFile multipartFile = new MockMultipartFile("imagefile", "testing.txt",
                "text/plain", "NewPublisher".getBytes());

        Publisher publisher = Publisher.builder().build();

        ArgumentCaptor<Publisher> argumentCaptor = ArgumentCaptor.forClass(Publisher.class);

        //when
        imageService.savePublisherImage(publisher, multipartFile);

        //then
        verify(publisherRepository, times(1)).save(argumentCaptor.capture());
        Publisher savedPublisher = argumentCaptor.getValue();
        assertEquals(multipartFile.getBytes().length, savedPublisher.getImage().length);
    }

    @Test
    void saveCustomerImage() throws Exception{
        //given
        MultipartFile multipartFile = new MockMultipartFile("imagefile", "testing.txt",
                "text/plain", "NewCustomer".getBytes());

        Customer customer = Customer.builder().build();

        ArgumentCaptor<Customer> argumentCaptor = ArgumentCaptor.forClass(Customer.class);

        //when
        imageService.saveCustomerImage(customer, multipartFile);

        //then
        verify(customerRepository, times(1)).save(argumentCaptor.capture());
        Customer savedCustomer = argumentCaptor.getValue();
        assertEquals(multipartFile.getBytes().length, savedCustomer.getImage().length);
    }
}