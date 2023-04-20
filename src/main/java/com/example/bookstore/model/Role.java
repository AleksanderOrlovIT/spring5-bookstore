package com.example.bookstore.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Role extends BaseEntity{

    @Builder
    public Role(Long id, String name, Set<Customer> customers) {
        super(id);
        this.name = name;
        if(customers != null)
            this.customers = customers;
    }

    @NotEmpty
    private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "role")
    private Set<Customer> customers = new HashSet<>();
}
