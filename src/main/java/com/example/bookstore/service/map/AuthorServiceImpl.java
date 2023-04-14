package com.example.bookstore.service.map;

import com.example.bookstore.model.Author;
import com.example.bookstore.service.AuthorService;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AuthorServiceImpl extends AbstractMapService<Author, Long> implements AuthorService {
    @Override
    public Set<Author> findAll() {
        return super.findAll();
    }

    @Override
    public Author findById(Long id) {
        return super.findById(id);
    }

    @Override
    public Author save(Author author) {
        return super.save(author);
    }

    @Override
    public void delete(Author author) {
        super.delete(author);
    }

    @Override
    public void deleteById(Long id) {
        super.deleteById(id);
    }
}
