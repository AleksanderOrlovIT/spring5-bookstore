package com.example.bookstore.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "customers")
public class Customer extends BaseEntity{

    @Builder
    public Customer(Long id, String userName, String password, BigDecimal balance, Set<Book> books, Role role) {
        super(id);
        this.userName = userName;
        this.password = password;
        this.balance = balance;
        if(books != null) {
            this.books = books;
        }
        this.role = role;
    }

    @NotEmpty
    @Column(name = "userName", unique = true)
    private String userName;

    @Column(name = "password", nullable = false)
    private String password;

    @NotNull
    @DecimalMin("0.0")
    @Column(name = "balance")
    private BigDecimal balance;

    @Lob
    private Byte[] image;

    @ManyToMany(mappedBy = "customers")
    private Set<Book> books = new HashSet<>();

    @ManyToOne
    private Role role;
}