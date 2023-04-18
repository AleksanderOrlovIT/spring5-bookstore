package com.example.bookstore.service.impl;

import com.example.bookstore.model.Author;
import com.example.bookstore.model.Book;
import com.example.bookstore.model.Customer;
import com.example.bookstore.model.Publisher;
import com.example.bookstore.repositories.AuthorRepository;
import com.example.bookstore.repositories.BookRepository;
import com.example.bookstore.repositories.CustomerRepository;
import com.example.bookstore.repositories.PublisherRepository;
import com.example.bookstore.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@Slf4j
public class ImageServiceImpl implements ImageService {

    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    private final PublisherRepository publisherRepository;

    private final CustomerRepository customerRepository;

    public ImageServiceImpl(BookRepository bookRepository, AuthorRepository authorRepository,
                            PublisherRepository publisherRepository, CustomerRepository customerRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.publisherRepository = publisherRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public void saveBookImage(Book book, MultipartFile multipartFile) {
            Byte[] bytes = new Byte[0];
            bytes = returnByteObjects(bytes, multipartFile);

            book.setImage(bytes);
            bookRepository.save(book);
    }

    @Override
    public void saveAuthorImage(Author author, MultipartFile multipartFile) {
            Byte[] bytes = new Byte[0];
            bytes = returnByteObjects(bytes, multipartFile);

            author.setImage(bytes);
            authorRepository.save(author);
    }

    @Override
    public void savePublisherImage(Publisher publisher, MultipartFile multipartFile) {
        Byte[] bytes = new Byte[0];
        bytes = returnByteObjects(bytes, multipartFile);

        publisher.setImage(bytes);
        publisherRepository.save(publisher);
    }

    @Override
    public void saveCustomerImage(Customer customer, MultipartFile multipartFile) {
        Byte[] bytes = new Byte[0];
        bytes = returnByteObjects(bytes, multipartFile);

        customer.setImage(bytes);
        customerRepository.save(customer);
    }

    public Byte[] returnByteObjects(Byte[] byteObjects, MultipartFile multipartFile){
        try {
            byteObjects = new Byte[multipartFile.getBytes().length];

            int i = 0;

            for (byte b : multipartFile.getBytes()) {
                byteObjects[i++] = b;
            }
        }catch (IOException e){
            log.error("Error occured", e);
            e.printStackTrace();
        }
        return byteObjects;
    }
}
