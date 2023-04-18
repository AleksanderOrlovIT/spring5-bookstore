package com.example.bookstore.service.impl;

import com.example.bookstore.model.Book;
import com.example.bookstore.model.Genre;
import com.example.bookstore.repositories.GenreRepository;
import com.example.bookstore.service.GenreService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public Set<Genre> findAll() {
        Set<Genre> genres = new HashSet<>();
        genreRepository.findAll().forEach(genres::add);
        return genres;
    }

    @Override
    public Genre findById(Long id) {
        return genreRepository.findById(id).orElse(null);
    }

    @Override
    public Genre save(Genre genre) {
        return genreRepository.save(genre);
    }

    @Override
    public void delete(Genre genre) {
        genreRepository.delete(genre);
    }

    @Override
    public void deleteById(Long id) {
        Genre genre = findById(id);
        if(genre != null && genre.getBooks() != null){
            for(Book book : genre.getBooks()){
                book.getGenres().remove(genre);
            }
        }
        genreRepository.deleteById(id);
    }

    @Override
    public Genre findByName(String name) {
        for(Genre genre : genreRepository.findAll()){
            if(genre.getName().equals(name))
                return genre;
        }
        return null;
    }
}
