package com.example.bookstore.service.impl;

import com.example.bookstore.model.Customer;
import com.example.bookstore.model.Role;
import com.example.bookstore.repositories.RoleRepository;
import com.example.bookstore.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {

    private RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Set<Role> findAll() {
        Set<Role> roles = new HashSet<>();
        roleRepository.findAll().forEach(roles::add);
        return roles;
    }

    @Override
    public Role findById(Long id) {
        return roleRepository.findById(id).orElse(null);
    }

    @Override
    public Role save(Role role) {
        if(role != null)
            return roleRepository.save(role);
        else
            return null;
    }

    @Override
    public void delete(Role role) {
        if(role != null && roleRepository.findById(role.getId()).isPresent()){
            removeBeforeDelete(role);
            roleRepository.delete(role);
        }
    }

    @Override
    public void deleteById(Long id) {
        Role role = roleRepository.findById(id).orElse(null);
        if(role != null){
            removeBeforeDelete(role);
            roleRepository.deleteById(id);
        }
    }

    public void removeBeforeDelete(Role role){
        Set<Customer> customers = role.getCustomers();
        if(customers != null){
            for(Customer customer : customers){
                customer.getRoles().remove(role);
            }
        }
    }

    public Role findByName(String name){
        for(Role role : roleRepository.findAll()){
            if(role.getName().equals(name))
                return role;
        }
        return null;
    }
}
