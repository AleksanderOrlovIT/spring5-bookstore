package com.example.bookstore.service;

import com.example.bookstore.model.Author;
import com.example.bookstore.model.Book;
import com.example.bookstore.model.Customer;
import com.example.bookstore.model.Publisher;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    void saveBookImage(Book book, MultipartFile multipartFile);

    void saveAuthorImage(Author author, MultipartFile multipartFile);
    void savePublisherImage(Publisher publisher, MultipartFile multipartFile);
    void saveCustomerImage(Customer customer, MultipartFile multipartFile);
}
