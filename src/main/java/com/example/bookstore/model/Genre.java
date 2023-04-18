package com.example.bookstore.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "genres")
public class Genre extends BaseEntity{

    @Builder
    public Genre(Long id, String name, Set<Book> books, Set<Author> authors) {
        super(id);
        this.name = name;
        if(books != null)
            this.books = books;
        if(authors != null)
            this.authors = authors;
    }

    @NotEmpty
    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "genres")
    private Set<Book> books = new HashSet<>();

    @ManyToMany(mappedBy = "genres")
    private Set<Author> authors = new HashSet<>();
}
