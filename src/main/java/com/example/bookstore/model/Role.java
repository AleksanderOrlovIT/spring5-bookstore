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
@Table(name = "roles")
public class Role extends BaseEntity{

    @Builder
    public Role(Long id, String name, Set<Customer> customers, Set<Privilege> privileges) {
        super(id);
        this.name = name;
        if(customers != null)
            this.customers = customers;
        if(privileges != null)
            this.privileges = privileges;
    }

    @NotEmpty
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Set<Customer> customers = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "roles_privileges", joinColumns = @JoinColumn(name = "role_id"),
    inverseJoinColumns = @JoinColumn(name = "privilege_id"))
    private Set<Privilege> privileges = new HashSet<>();
}
