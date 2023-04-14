package com.example.bookstore.service.map;

import com.example.bookstore.model.Book;
import com.example.bookstore.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookServiceTest {

    BookService bookService;

    final Long bookId = 1L;

    @BeforeEach
    void setUp() {
        bookService = new BookServiceImpl();

        bookService.save(Book.builder().build());
    }

    @Test
    void findAll() {
        assertEquals(1, bookService.findAll().size());
    }

    @Test
    void findById() {
        Book book = bookService.findById(1L);
        assertEquals(book.getId(), bookId);
    }

    @Test
    void saveWithId() {
        Long savedId = 2L;
        Book book2 = Book.builder().id(savedId).build();
        Book savedBook = bookService.save(book2);
        assertEquals(savedId, savedBook.getId());
    }

    @Test
    void saveWithoutId(){
        Book savedBook = bookService.save(Book.builder().build());
        assertNotNull(savedBook);
        assertNotNull(savedBook.getId());
    }

    @Test
    void delete() {
        bookService.delete(bookService.findById(bookId));
        assertEquals(0, bookService.findAll().size());
    }

    @Test
    void deleteById() {
        bookService.deleteById(bookId);
        assertEquals(0, bookService.findAll().size());
    }
}