package com.example.bookstore.service.map;

import com.example.bookstore.model.Book;
import com.example.bookstore.service.BookService;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class BookServiceImpl extends AbstractMapService<Book, Long> implements BookService {
    @Override
    public Set<Book> findAll() {
        return super.findAll();
    }

    @Override
    public Book findById(Long id) {
        return super.findById(id);
    }

    @Override
    public Book save(Book book) {
        return super.save(book);
    }

    @Override
    public void delete(Book book) {
        super.delete(book);
    }

    @Override
    public void deleteById(Long id) {
        super.deleteById(id);
    }
}
