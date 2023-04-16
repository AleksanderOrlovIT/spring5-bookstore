package com.example.bookstore.service.impl;

import com.example.bookstore.model.Book;
import com.example.bookstore.model.Publisher;
import com.example.bookstore.repositories.PublisherRepository;
import com.example.bookstore.service.PublisherService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class PublisherServiceImpl implements PublisherService {

    private final PublisherRepository publisherRepository;

    public PublisherServiceImpl(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    @Override
    public Set<Publisher> findAll() {
        Set<Publisher> publishers = new HashSet<>();
        publisherRepository.findAll().forEach(publishers::add);
        return publishers;
    }

    @Override
    public Publisher findById(Long id) {
        return publisherRepository.findById(id).orElse(null);
    }

    @Override
    public Publisher save(Publisher publisher) {
        return publisherRepository.save(publisher);
    }

    @Override
    public void delete(Publisher publisher) {
        publisherRepository.delete(publisher);
    }

    @Override
    public void deleteById(Long id) {
        Publisher publisher = findById(id);
        if(publisher != null && publisher.getBooks() != null){
            for(Book book : publisher.getBooks()){
                book.getPublishers().remove(publisher);
            }
        }
        publisherRepository.deleteById(id);
    }

    @Override
    public Publisher findByName(String name) {
        for(Publisher publisher : publisherRepository.findAll()){
            if(publisher.getName().equals(name))
                return publisher;
        }
        return null;
    }
}
