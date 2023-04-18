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
@Table(name = "publishers")
public class Publisher extends BaseEntity{

    @Builder
    public Publisher(Long id, String name, String address, Set<Book> books) {
        super(id);
        this.name = name;
        this.address = address;
        if(books != null) {
            this.books = books;
        }
    }

    @NotEmpty
    @Column(name = "name")
    private String name;

    @NotEmpty
    @Column(name = "address")
    private String address;

    @Lob
    private Byte[] image;

    @ManyToMany(mappedBy = "publishers")
    private Set<Book> books = new HashSet<>();
}
