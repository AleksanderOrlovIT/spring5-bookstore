package com.example.bookstore.service;

import com.example.bookstore.model.Publisher;

public interface PublisherService extends CrudService<Publisher, Long>{
    public Publisher findByName(String name);

    public Publisher copyOldPublisherDataInNewOne(Publisher newPublisher, Publisher oldPublisher);
}
