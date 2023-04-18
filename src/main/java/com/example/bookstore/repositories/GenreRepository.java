package com.example.bookstore.repositories;

import com.example.bookstore.model.Genre;
import org.springframework.data.repository.CrudRepository;

public interface GenreRepository extends CrudRepository<Genre, Long> {
}
