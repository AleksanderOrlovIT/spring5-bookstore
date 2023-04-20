package com.example.bookstore.service.impl;

import com.example.bookstore.model.Author;
import com.example.bookstore.model.Book;
import com.example.bookstore.model.Customer;
import com.example.bookstore.model.Publisher;
import com.example.bookstore.repositories.BookRepository;
import com.example.bookstore.service.BookService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Set<Book> findAll() {
        Set<Book> books = new HashSet<>();
        bookRepository.findAll().forEach(books::add);
        return books;
    }

    @Override
    public Book findById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    @Override
    public Book save(Book book) {
        if(book != null)
            return bookRepository.save(book);
        else
            return null;
    }

    @Override
    public void delete(Book book) {
        if(book != null && bookRepository.findById(book.getId()).isPresent())
            bookRepository.delete(book);
    }

    @Override
    public void deleteById(Long id) {
        if(bookRepository.findById(id).isPresent())
            bookRepository.deleteById(id);
    }

    @Override
    public Book saveBookSets(Book newBook, Book oldBook) {
        if(newBook != null && oldBook != null) {
            for (Author author : oldBook.getAuthors()) {
                newBook.getAuthors().add(author);
            }
            for (Publisher publisher : oldBook.getPublishers()) {
                newBook.getPublishers().add(publisher);
            }
            for (Customer customer : oldBook.getCustomers()) {
                newBook.getCustomers().add(customer);
            }
            return newBook;
        }
        return null;
    }

    @Override
    public Book findByName(String name) {
        for(Book book : bookRepository.findAll()){
            if(book.getName().equals(name))
                return book;
        }
        return null;
    }
}
