package com.example.bookstore.model;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "customers")
public class Customer extends BaseEntity{

    @Builder
    public Customer(Long id, String userName, Double balance, Set<Book> books) {
        super(id);
        this.userName = userName;
        this.balance = balance;
        if(books != null) {
            this.books = books;
        }
    }


    @Column(name = "userName")
    private String userName;

    @Column(name = "balance")
    private Double balance;

    @ManyToMany(mappedBy = "customers")
    private Set<Book> books = new HashSet<>();
}
