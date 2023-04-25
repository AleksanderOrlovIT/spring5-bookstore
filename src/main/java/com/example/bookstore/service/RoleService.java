package com.example.bookstore.service;

import com.example.bookstore.model.Role;

public interface RoleService extends CrudService<Role, Long>{
    Role findByName(String name);
}
