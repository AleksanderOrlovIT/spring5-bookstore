package com.example.bookstore.service;

import com.example.bookstore.model.Genre;

public interface GenreService extends CrudService<Genre, Long>{
    public Genre findByName(String name);

    public Genre copyOldGenreDataInNewOne(Genre newGenre, Genre oldGenre);
}
