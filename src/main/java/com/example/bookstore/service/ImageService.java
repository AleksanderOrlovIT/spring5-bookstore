package com.example.bookstore.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    void saveBookImage(Long bookId, MultipartFile multipartFile);
    void saveAuthorImage(Long authorId, MultipartFile multipartFile);
    void savePublisherImage(Long publisherId, MultipartFile multipartFile);
    void saveCustomerImage(Long customerId, MultipartFile multipartFile);
}
