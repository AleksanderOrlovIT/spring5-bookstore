package com.example.bookstore.service.map;

import com.example.bookstore.model.Publisher;
import com.example.bookstore.repositories.PublisherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PublisherServiceImplTest {

    public static final String publisherName = "Publisher";

    public static final Long publisherId = 1L;

    @Mock
    PublisherRepository publisherRepository;

    @InjectMocks
    PublisherServiceImpl publisherService;

    Publisher returnPublisher;

    @BeforeEach
    void setUp() {
        returnPublisher = Publisher.builder().id(publisherId).name(publisherName).build();
    }

    @Test
    void findAll() {
        Set<Publisher> publisherSet = new HashSet<>();
        publisherSet.add(Publisher.builder().id(1L).build());
        publisherSet.add(Publisher.builder().id(2L).build());

        when(publisherRepository.findAll()).thenReturn(publisherSet);

        Set<Publisher> publishers = publisherService.findAll();

        assertNotNull(publishers);
        assertEquals(2, publishers.size());
    }

    @Test
    void findById() {
        when(publisherRepository.findById(anyLong())).thenReturn(Optional.of(returnPublisher));

        Publisher publisher = publisherService.findById(publisherId);

        assertNotNull(publisher);
    }

    @Test
    void findByIdNotFound() {
        when(publisherRepository.findById(anyLong())).thenReturn(Optional.empty());

        Publisher publisher = publisherService.findById(publisherId);

        assertNull(publisher);
    }

    @Test
    void saveWithId() {
        when(publisherRepository.save(any())).thenReturn(returnPublisher);

        Publisher publisher = publisherService.save(Publisher.builder().id(1L).build());

        assertNotNull(publisher);
        verify(publisherRepository).save(any());
    }

    @Test
    void saveWithoutId(){
        when(publisherRepository.save(any())).thenReturn(returnPublisher);

        Publisher publisher = publisherService.save(Publisher.builder().build());

        assertNotNull(publisher);
        verify(publisherRepository).save(any());
    }

    @Test
    void delete() {
       publisherService.delete(returnPublisher);
       verify(publisherRepository, times(1)).delete(any());
    }

    @Test
    void deleteById() {
       publisherService.deleteById(publisherId);
       verify(publisherRepository).deleteById(anyLong());
    }
}
