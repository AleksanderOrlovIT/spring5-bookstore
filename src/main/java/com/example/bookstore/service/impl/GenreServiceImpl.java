package com.example.bookstore.service.impl;

import com.example.bookstore.model.Author;
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
        if (genre != null)
            return genreRepository.save(genre);
        else
            return null;
    }

    @Override
    public void delete(Genre genre) {
        if (genre != null && genreRepository.findById(genre.getId()).isPresent()) {
            removeBeforeDelete(genre);
            genreRepository.delete(genre);
        }
    }

    @Override
    public void deleteById(Long id) {
        Genre genre = genreRepository.findById(id).orElse(null);
        if (genre != null) {
            removeBeforeDelete(genre);
            genreRepository.deleteById(id);
        }
    }

    public void removeBeforeDelete(Genre genre){
        if (genre != null) {
            Set<Book> books = genre.getBooks();
            if (books != null) {
                for (Book book : books) {
                    book.getGenres().remove(genre);
                }
            }
            Set<Author> authors = genre.getAuthors();
            if (authors != null) {
                for (Author author : authors) {
                    author.getGenres().remove(genre);
                }
            }
        }
    }

    @Override
    public Genre findByName(String name) {
        for (Genre genre : genreRepository.findAll()) {
            if (genre.getName().equals(name))
                return genre;
        }
        return null;
    }
}
