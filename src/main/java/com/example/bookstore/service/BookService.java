package com.example.bookstore.service;

import com.example.bookstore.model.Book;

public interface BookService extends CrudService<Book, Long>{
    public Book copyOldBookDataInNewOne(Book newBook, Book oldBook);

    public Book findByName(String name);
}
