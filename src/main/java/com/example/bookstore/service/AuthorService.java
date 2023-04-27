package com.example.bookstore.service;

import com.example.bookstore.model.Author;

public interface AuthorService extends CrudService<Author, Long>{
    public Author findByFullName(String firstName, String lastName);

    public Author copyOldAuthorDataInNewOne(Author newAuthor, Author oldAuthor);
}
