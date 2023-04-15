package com.example.bookstore.service.impl;

import com.example.bookstore.model.Author;
import com.example.bookstore.repositories.AuthorRepository;
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
public class AuthorServiceImplTest {

    public static final String authorName = "SavedName";

    public static final Long authorId = 1L;

    @Mock
    AuthorRepository authorRepository;

    @InjectMocks
    AuthorServiceImpl authorService;

    Author returnAuthor;

    @BeforeEach
    void setUp() {
        returnAuthor = Author.builder().id(authorId).firstName(authorName).build();
    }

    @Test
    void findAll() {
        Set<Author> authorSet = new HashSet<>();
        authorSet.add(Author.builder().id(1L).build());
        authorSet.add(Author.builder().id(2L).build());

        when(authorRepository.findAll()).thenReturn(authorSet);

        Set<Author> authors = authorService.findAll();

        assertNotNull(authors);
        assertEquals(2, authors.size());
    }

    @Test
    void findById() {
        when(authorRepository.findById(anyLong())).thenReturn(Optional.of(returnAuthor));

        Author author = authorService.findById(authorId);

        assertNotNull(author);
    }

    @Test
    void findByIdNotFound() {
        when(authorRepository.findById(anyLong())).thenReturn(Optional.empty());

        Author author = authorService.findById(authorId);

        assertNull(author);
    }


    @Test
    void saveWithId() {
        when(authorRepository.save(any())).thenReturn(returnAuthor);

        Author author = authorService.save(Author.builder().id(1L).build());

        assertNotNull(author);
        verify(authorRepository).save(any());
    }

    @Test
    void saveWithoutId(){
        when(authorRepository.save(any())).thenReturn(returnAuthor);

        Author author = authorService.save(Author.builder().build());

        assertNotNull(author);
        verify(authorRepository).save(any());
    }

    @Test
    void delete() {
        authorService.delete(returnAuthor);
        verify(authorRepository,times(1)).delete(any());
    }

    @Test
    void deleteById() {
        authorService.deleteById(authorId);
        verify(authorRepository).deleteById(anyLong());
    }
}
