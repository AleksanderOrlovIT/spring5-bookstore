package com.example.bookstore.bootstrap;

import com.example.bookstore.model.Author;
import com.example.bookstore.model.Book;
import com.example.bookstore.model.Customer;
import com.example.bookstore.model.Publisher;
import com.example.bookstore.service.AuthorService;
import com.example.bookstore.service.BookService;
import com.example.bookstore.service.CustomerService;
import com.example.bookstore.service.PublisherService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InitialBootstrap implements CommandLineRunner {

    private final BookService bookService;
    private final AuthorService authorService;
    private final PublisherService publisherService;
    private final CustomerService customerService;

    public InitialBootstrap(BookService bookService, AuthorService authorService, PublisherService publisherService,
                            CustomerService customerService) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.publisherService = publisherService;
        this.customerService = customerService;
    }

    private void loadData(){
        //Init
        Author authorOscar = Author.builder().firstName("Oscar").lastName("Wilde").build();
        Author authorJorj = Author.builder().firstName("Jorj").lastName("Oruell").build();

        Book bookDorian = Book.builder().name("Dorian gray").price(5.0).build();
        Book book1984 = Book.builder().name("1984").price(6.0).build();

        Publisher publisherLondon = Publisher.builder().name("LondonPublicity").address("London").build();

        Customer customer1 = Customer.builder().userName("SashaOrlov").balance(11.0).build();


        //adding to sets
        authorOscar.getBooks().add(bookDorian);
        authorJorj.getBooks().add(book1984);

        bookDorian.getAuthors().add(authorOscar);
        bookDorian.getPublishers().add(publisherLondon);

        book1984.getAuthors().add(authorJorj);
        bookDorian.getPublishers().add(publisherLondon);

        //saving

        authorService.save(authorOscar);
        authorService.save(authorJorj);

        bookService.save(bookDorian);
        bookService.save(book1984);

        publisherService.save(publisherLondon);

        customerService.save(customer1);

        System.out.println("Authors : " + authorService.findAll());
        System.out.println("Books : " + bookService.findAll().toString());
        System.out.println("Publishers : " + publisherService.findAll().toString());
        System.out.println("Customers : " + customerService.findAll().toString());
    }

    @Override
    public void run(String... args) throws Exception {
        loadData();
    }
}
