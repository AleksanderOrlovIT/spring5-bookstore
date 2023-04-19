package com.example.bookstore.controllers.imagecontrollers;

import com.example.bookstore.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.ui.Model;
import com.example.bookstore.model.Book;
import com.example.bookstore.service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Controller
public class BookImageController {

    private final static String errorPage = "error/400error";

    private final ImageService imageService;
    private final BookService bookService;

    public BookImageController(ImageService imageService, BookService bookService) {
        this.imageService = imageService;
        this.bookService = bookService;
    }

    @GetMapping("book/{bookId}/newImage")
    public String showUploadForm(@PathVariable Long bookId, Model model) {
        Book book = bookService.findById(bookId);
        if (book == null) {
            model.addAttribute("exception", new Exception("There is no book with id: " + bookId));
            return errorPage;
        }
        model.addAttribute("book", book);

        return "book/imageuploadform";
    }

    @PostMapping("book/{bookId}/newImage")
    public String handleImagePost(@PathVariable Long bookId, @RequestParam("imagefile") MultipartFile file, Model model) {
        Book savedBook = bookService.findById(bookId);
        if (savedBook != null) {
            imageService.saveBookImage(savedBook, file);
        } else {
            model.addAttribute("exception", new Exception("There is no book with id: " + bookId));
            return errorPage;
        }

        return "redirect:/book/" + bookId + "/show";
    }

    @GetMapping("book/{bookId}/image")
    public void renderImageFromDB(@PathVariable Long bookId, HttpServletResponse response) throws IOException {
        Book book = bookService.findById(bookId);
        if (book == null) {
            log.error("BookImageController.renderImageFromDB(), error : there is no book with id" + bookId);
        } else if (book.getImage() != null) {
            byte[] byteArray = new byte[book.getImage().length];
            int i = 0;

            for (Byte wrappedByte : book.getImage()) {
                byteArray[i++] = wrappedByte;
            }

            response.setContentType("image/jpeg");
            InputStream is = new ByteArrayInputStream(byteArray);
            IOUtils.copy(is, response.getOutputStream());
        }
    }
}
