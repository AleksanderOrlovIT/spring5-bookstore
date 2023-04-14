package com.example.bookstore.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customers")
public class Customer extends BaseEntity{

    @Column(name = "userName")
    private String userName;

    @Column(name = "balance")
    private Double balance;

    @ManyToMany(mappedBy = "customers")
    private Set<Book> books = new HashSet<>();
}
