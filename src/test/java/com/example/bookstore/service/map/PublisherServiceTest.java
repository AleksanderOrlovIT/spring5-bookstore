package com.example.bookstore.service.map;

import com.example.bookstore.model.Publisher;
import com.example.bookstore.service.PublisherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PublisherServiceTest {

    PublisherService publisherService;

    Long publisherId = 1L;

    @BeforeEach
    void setUp() {
        publisherService = new PublisherServiceImpl();
        publisherService.save(Publisher.builder().build());
    }

    @Test
    void findAll() {
        assertEquals(1, publisherService.findAll().size());
    }

    @Test
    void findById() {
        Publisher publisher = publisherService.findById(publisherId);
        assertEquals(publisher.getId(), publisherId);
    }

    @Test
    void saveWithId() {
        Long savedId = 2L;
        Publisher publisher = Publisher.builder().id(savedId).build();
        Publisher savedPublisher = publisherService.save(publisher);
        assertEquals(savedPublisher.getId(), savedId);
    }

    @Test
    void saveWithoutId(){
        Publisher publisher = publisherService.save(Publisher.builder().build());

        assertNotNull(publisher);
        assertNotNull(publisher.getId());
    }

    @Test
    void delete() {
        publisherService.delete(publisherService.findById(publisherId));
        assertEquals(0, publisherService.findAll().size());
    }

    @Test
    void deleteById() {
        publisherService.deleteById(publisherId);
        assertEquals(0, publisherService.findAll().size());
    }
}
