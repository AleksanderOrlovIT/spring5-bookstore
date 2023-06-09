package com.example.bookstore.bootstrap;

import com.example.bookstore.model.*;
import com.example.bookstore.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class InitialBootstrap implements CommandLineRunner {

    private final BookService bookService;
    private final AuthorService authorService;
    private final PublisherService publisherService;
    private final CustomerService customerService;
    private final GenreService genreService;
    private final ImageService imageService;
    private final RoleService roleService;

    public InitialBootstrap(BookService bookService, AuthorService authorService, PublisherService publisherService,
                            CustomerService customerService,GenreService genreService, ImageService imageService,
                            RoleService roleService) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.publisherService = publisherService;
        this.customerService = customerService;
        this.genreService = genreService;
        this.imageService = imageService;
        this.roleService = roleService;
    }

    private void loadData() {
        //Init
        Author authorOscar = Author.builder().firstName("Oscar").lastName("Wilde").build();
        Author authorGeorge = Author.builder().firstName("George").lastName(    "Orwell").build();

        Book bookDorian = Book.builder().name("Dorian Gray").price(BigDecimal.valueOf(5.0)).build();
        Book book1984 = Book.builder().name("1984").price(BigDecimal.valueOf(6.0)).build();

        Publisher publisherLondon = Publisher.builder().name("LondonPublicity").address("London").build();

        Customer customer1 = Customer.builder().userName("SashaOrlov").password("passSasha")
                .balance(BigDecimal.valueOf(11.0)).build();
        Customer admin = Customer.builder().userName("admin").password("admin")
                .balance(BigDecimal.valueOf(10000)).build();

        Genre genrePhilFic = Genre.builder().name("Philosophical fiction").build();
        Genre genreDistPolFic = Genre.builder().name("Dystopian political fiction").build();

        Role customerRole = Role.builder().name("CustomerRole").build();
        Role adminRole = Role.builder().name("ADMIN").build();


        //adding to sets
        bookDorian.getAuthors().add(authorOscar);
        bookDorian.getPublishers().add(publisherLondon);
        bookDorian.getCustomers().add(customer1);
        bookDorian.getGenres().add(genrePhilFic);

        book1984.getAuthors().add(authorGeorge);
        book1984.getPublishers().add(publisherLondon);
        book1984.getCustomers().add(customer1);
        book1984.getGenres().add(genreDistPolFic);

        authorOscar.getBooks().add(bookDorian);
        authorOscar.getGenres().add(genrePhilFic);

        authorGeorge.getBooks().add(book1984);
        authorGeorge.getGenres().add(genreDistPolFic);

        publisherLondon.getBooks().add(book1984);
        publisherLondon.getBooks().add(bookDorian);

        customer1.getBooks().add(book1984);
        customer1.getBooks().add(bookDorian);
        customer1.getRoles().add(customerRole);

        admin.getRoles().add(adminRole);

        customerRole.getCustomers().add(customer1);
        adminRole.getCustomers().add(admin);

        //saving
        roleService.save(customerRole);
        roleService.save(adminRole);

        genreService.save(genrePhilFic);
        genreService.save(genreDistPolFic);

        publisherService.save(publisherLondon);

        authorService.save(authorOscar);
        authorService.save(authorGeorge);

        customerService.save(customer1);
        customerService.save(admin);

        bookService.save(book1984);
        bookService.save(bookDorian);
    }

    private void loadImages() {
        Path imagePath = Paths.get("src/main/resources/static/images/1984.jpg");
        MultipartFile multipartFile = returnMultiPartFile(imagePath);
        if (multipartFile != null)
            imageService.saveBookImage(bookService.findByName("1984"), multipartFile);

        imagePath = Paths.get("src/main/resources/static/images/dorian.jpg");
        multipartFile = returnMultiPartFile(imagePath);
        if (multipartFile != null)
            imageService.saveBookImage(bookService.findByName("Dorian Gray"), multipartFile);

        imagePath = Paths.get("src/main/resources/static/images/georgeOrwell.jpeg");
        multipartFile = returnMultiPartFile(imagePath);
        if (multipartFile != null)
            imageService.saveAuthorImage(authorService.findByFullName("George", "Orwell"), multipartFile);

        imagePath = Paths.get("src/main/resources/static/images/oscarWilde.jpeg");
        multipartFile = returnMultiPartFile(imagePath);
        if (multipartFile != null)
            imageService.saveAuthorImage(authorService.findByFullName("Oscar", "Wilde"), multipartFile);

        imagePath = Paths.get("src/main/resources/static/images/londonPublish.jpeg");
        multipartFile = returnMultiPartFile(imagePath);
        if (multipartFile != null)
            imageService.savePublisherImage(publisherService.findByName("LondonPublicity"), multipartFile);

        imagePath = Paths.get("src/main/resources/static/images/customerSasha.jpg");
        multipartFile = returnMultiPartFile(imagePath);
        if (multipartFile != null)
            imageService.saveCustomerImage(customerService.findByUserName("SashaOrlov"), multipartFile);
    }

    private MultipartFile returnMultiPartFile(Path path) {
        try {
            return new MockMultipartFile("file",
                    path.getFileName().toString(), "image/jpeg", Files.readAllBytes(path));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void run(String... args) throws Exception {
        loadData();
        loadImages();
    }
}
