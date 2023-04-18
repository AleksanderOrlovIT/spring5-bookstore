package com.example.bookstore.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "authors")
public class Author extends BaseEntity{

    @Builder
    public Author(Long id, String firstName, String lastName, Set<Book> books){
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        if(books != null){
            this.books = books;
        }
    }

    @NotEmpty
    @Column(name = "firstName")
    private String firstName;

    @NotEmpty
    @Column(name = "lastName")
    private String lastName;

    @Lob
    Byte[] image;

    @ManyToMany(mappedBy = "authors")
    private Set<Book> books = new HashSet<>();
}
