package com.example.bookstore.service.impl;

import com.example.bookstore.model.Book;
import com.example.bookstore.repositories.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    public static final Long bookId = 1L;

    public static final String savedBookName = "SavedBook";

    @Mock
    BookRepository bookRepository;

    @InjectMocks
    BookServiceImpl bookService;

    Book returnBook;

    @BeforeEach
    void setUp() {
        returnBook = Book.builder().id(1L).name(savedBookName).build();
    }

    @Test
    void findAll() {
        Set<Book> returnBooks = new HashSet<>();
        returnBooks.add(Book.builder().id(1L).build());
        returnBooks.add(Book.builder().id(2L).build());

        when(bookRepository.findAll()).thenReturn(returnBooks);

        Set<Book> books = bookService.findAll();

        assertNotNull(books);
        assertEquals(2, books.size());
    }

    @Test
    void findById() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(returnBook));

        Book book = bookService.findById(bookId);

        assertNotNull(book);
    }

    @Test
    void findByIdNotFound() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        Book book = bookService.findById(bookId);

        assertNull(book);
    }

    @Test
    void saveWithId() {
        when(bookRepository.save(any())).thenReturn(returnBook);

        Book newBook = bookService.save(Book.builder().id(1L).build());

        assertNotNull(newBook);

        verify(bookRepository).save(any());
    }

    @Test
    void saveWithoutId(){
        when(bookRepository.save(any())).thenReturn(returnBook);

        Book newBook = bookService.save(Book.builder().build());

        assertNotNull(newBook);

        verify(bookRepository).save(any());
    }

    @Test
    void delete() {
        bookService.delete(returnBook);
        verify(bookRepository, times(1)).delete(any());
    }

    @Test
    void deleteById() {
        bookService.deleteById(bookId);
        verify(bookRepository).deleteById(anyLong());
    }
}