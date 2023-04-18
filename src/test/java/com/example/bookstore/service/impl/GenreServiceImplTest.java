package com.example.bookstore.service.impl;

import com.example.bookstore.model.Genre;
import com.example.bookstore.repositories.GenreRepository;
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
public class GenreServiceImplTest {

    public static final String genreName = "SavedName";

    public static final Long genreId = 1L;

    @Mock
    GenreRepository genreRepository;

    @InjectMocks
    GenreServiceImpl genreService;

    Genre returnGenre;

    @BeforeEach
    void setUp() {
        returnGenre = Genre.builder().id(genreId).name(genreName).build();
    }

    @Test
    void findAll() {
        Set<Genre> genreSet = new HashSet<>();
        genreSet.add(Genre.builder().id(1L).build());
        genreSet.add(Genre.builder().id(2L).build());

        when(genreRepository.findAll()).thenReturn(genreSet);

        Set<Genre> genres = genreService.findAll();

        assertNotNull(genres);
        assertEquals(2, genres.size());
    }

    @Test
    void findById() {
        when(genreRepository.findById(anyLong())).thenReturn(Optional.of(returnGenre));

        Genre genre = genreService.findById(genreId);

        assertNotNull(genre);
    }

    @Test
    void findByIdNotFound() {
        when(genreRepository.findById(anyLong())).thenReturn(Optional.empty());

        Genre genre = genreService.findById(genreId);

        assertNull(genre);
    }


    @Test
    void saveWithId() {
        when(genreRepository.save(any())).thenReturn(returnGenre);

        Genre genre = genreService.save(Genre.builder().id(1L).build());

        assertNotNull(genre);
        verify(genreRepository).save(any());
    }

    @Test
    void saveWithoutId(){
        when(genreRepository.save(any())).thenReturn(returnGenre);

        Genre genre = genreService.save(Genre.builder().build());

        assertNotNull(genre);
        verify(genreRepository).save(any());
    }

    @Test
    void delete() {
        genreService.delete(returnGenre);
        verify(genreRepository,times(1)).delete(any());
    }

    @Test
    void deleteById() {
        genreService.deleteById(genreId);
        verify(genreRepository).deleteById(anyLong());
    }
}
