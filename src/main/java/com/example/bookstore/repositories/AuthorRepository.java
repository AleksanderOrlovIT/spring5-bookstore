package com.example.bookstore.repositories;

import com.example.bookstore.model.Author;
import org.springframework.data.repository.CrudRepository;

public interface AuthorRepository extends CrudRepository<Author, Long> {
}
