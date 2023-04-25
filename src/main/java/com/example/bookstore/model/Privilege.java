package com.example.bookstore.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Privilege extends BaseEntity{

    @Builder
    public Privilege(Long id, String name, Set<Role> roles) {
        super(id);
        this.name = name;
        this.roles = roles;
    }

    private String name;

    @ManyToMany(mappedBy = "privileges")
    private Set<Role> roles = new HashSet<>();
}
