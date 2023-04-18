package com.example.bookstore.service.impl;

import com.example.bookstore.model.Book;
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
    public void saveBookImage(Long bookId, MultipartFile multipartFile) {
        if(bookRepository.findById(bookId).isPresent()){
            Book book = bookRepository.findById(bookId).get();
            Byte[] bytes = new Byte[0];
            bytes = returnByteObjects(bytes, multipartFile);

            book.setImage(bytes);
            bookRepository.save(book);
        }else{
            log.error("There is no such book id: "  + bookId);
        }
    }

    @Override
    public void saveAuthorImage(Long authorId, MultipartFile multipartFile) {

    }

    @Override
    public void savePublisherImage(Long publisherId, MultipartFile multipartFile) {

    }

    @Override
    public void saveCustomerImage(Long customerId, MultipartFile multipartFile) {

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
