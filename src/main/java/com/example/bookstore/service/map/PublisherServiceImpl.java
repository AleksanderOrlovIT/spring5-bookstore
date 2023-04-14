package com.example.bookstore.service.map;

import com.example.bookstore.model.Publisher;
import com.example.bookstore.service.PublisherService;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class PublisherServiceImpl extends AbstractMapService<Publisher, Long> implements PublisherService {
    @Override
    public Set<Publisher> findAll() {
        return super.findAll();
    }

    @Override
    public Publisher findById(Long id) {
        return super.findById(id);
    }

    @Override
    public Publisher save(Publisher publisher) {
        return super.save(publisher);
    }

    @Override
    public void delete(Publisher publisher) {
        super.delete(publisher);
    }

    @Override
    public void deleteById(Long id) {
        super.deleteById(id);
    }
}
