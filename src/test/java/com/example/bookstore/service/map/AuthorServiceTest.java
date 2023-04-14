package com.example.bookstore.service.map;

import com.example.bookstore.model.Author;
import com.example.bookstore.service.AuthorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AuthorServiceTest {
    AuthorService authorService;

    final Long authorId = 1L;

    @BeforeEach
    void setUp() {
        authorService = new AuthorServiceImpl();

        authorService.save(Author.builder().build());
    }

    @Test
    void findAll() {
        assertEquals(1, authorService.findAll().size());
    }

    @Test
    void findById() {
        Author author = authorService.findById(authorId);
        assertEquals(author.getId(), authorId);
    }

    @Test
    void saveWithId() {
        Long savedId = 2L;
        Author author = Author.builder().id(savedId).build();

        Author savedAuthor = authorService.save(author);
        assertEquals(savedAuthor.getId(), savedId);
    }

    @Test
    void saveWithoutId(){
        Author author = authorService.save(Author.builder().build());

        assertNotNull(author);
        assertNotNull(author.getId());
    }

    @Test
    void delete() {
        authorService.delete(authorService.findById(authorId));
        assertEquals(0, authorService.findAll().size());
    }

    @Test
    void deleteById() {
        authorService.deleteById(authorId);
        assertEquals(0, authorService.findAll().size());
    }
}
