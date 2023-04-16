package com.example.bookstore.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "books")
public class Book extends BaseEntity{

    @Builder
    public Book(Long id, String name, BigDecimal price, Set<Author> authors, Set<Publisher> publishers,
                Set<Customer> customers) {
        super(id);
        this.name = name;
        this.price = price;
        if(authors != null) {
            this.authors = authors;
        }
        if(publishers != null) {
            this.publishers = publishers;
        }
        if(customers != null) {
            this.customers = customers;
        }
    }

    @NotEmpty
    @Column(name = "name")
    private String name;

    @NotNull
    @DecimalMin(value = "0.0")
    @Column(name = "price")
    private BigDecimal price;

    @ManyToMany
    @JoinTable(name = "book_authors", joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    private Set<Author> authors = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "book_publishers", joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "publisher_id"))
    private Set<Publisher> publishers = new HashSet<>();


    @ManyToMany
    @JoinTable(name = "book_customers", joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "customer_id"))
    private Set<Customer> customers = new HashSet<>();
}
