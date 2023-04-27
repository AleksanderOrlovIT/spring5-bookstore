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
        if(author != null)
            return authorRepository.save(author);
        else
            return null;
    }

    @Override
    public void delete(Author author) {
        if(author != null && authorRepository.findById(author.getId()).isPresent()) {
            removeBeforeDelete(author);
            authorRepository.delete(author);
        }
    }

    @Override
    public void deleteById(Long id) {
        Author author = authorRepository.findById(id).orElse(null);
        if(author != null) {
            removeBeforeDelete(author);
            authorRepository.deleteById(id);
        }
    }

    public void removeBeforeDelete(Author author){
        Set<Book> books = author.getBooks();
        if (books != null) {
            for (Book book : books) {
                book.getAuthors().remove(author);
            }
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

    @Override
    public Author copyOldAuthorDataInNewOne(Author newAuthor, Author oldAuthor) {
        if(newAuthor != null && oldAuthor != null) {
            newAuthor.setBooks(oldAuthor.getBooks());
            newAuthor.setGenres(oldAuthor.getGenres());
            newAuthor.setImage(oldAuthor.getImage());
            return newAuthor;
        } else
            return null;
    }
}
