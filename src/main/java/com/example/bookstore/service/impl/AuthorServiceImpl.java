package com.example.bookstore.service.impl;

import com.example.bookstore.model.Author;
import com.example.bookstore.model.Book;
import com.example.bookstore.repositories.AuthorRepository;
import com.example.bookstore.service.AuthorService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public Set<Author> findAll() {
        Set<Author> authors =  new HashSet<>();
        authorRepository.findAll().forEach(authors::add);
        return authors;
    }

    @Override
    public Author findById(Long id) {
        return authorRepository.findById(id).orElse(null);
    }

    @Override
    public Author save(Author author) {
        return authorRepository.save(author);
    }

    @Override
    public void delete(Author author) {
        if(authorRepository.findById(author.getId()).isPresent()) {
            if (author.getBooks() != null) {
                for (Book book : author.getBooks()) {
                    book.getAuthors().remove(author);
                }
            }
            authorRepository.delete(author);
        }
    }

    @Override
    public void deleteById(Long id) {
        if(authorRepository.findById(id).isPresent()) {
            Author author = findById(id);
            if (author != null && author.getBooks() != null) {
                for (Book book : author.getBooks()) {
                    book.getAuthors().remove(author);
                }
            }
            authorRepository.deleteById(id);
        }
    }

    @Override
    public Author findByFullName(String firstName, String lastName) {
        for(Author author : authorRepository.findAll()){
            if(author.getFirstName().equals(firstName) && author.getLastName().equals(lastName))
                return author;
        }
        return null;
    }
}
