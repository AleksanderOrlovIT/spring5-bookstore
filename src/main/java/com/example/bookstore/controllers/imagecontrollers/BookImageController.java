package com.example.bookstore.controllers.imagecontrollers;

import com.example.bookstore.service.ImageService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import com.example.bookstore.model.Book;
import com.example.bookstore.service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Controller
public class BookImageController {

    private final ImageService imageService;
    private final BookService bookService;

    public BookImageController(ImageService imageService, BookService bookService) {
        this.imageService = imageService;
        this.bookService = bookService;
    }

    @GetMapping("book/{bookId}/image")
    public String showUploadForm(@PathVariable Long bookId, Model model){
        model.addAttribute("book", bookService.findById(bookId));

        return "book/imageuploadform";
    }

    @PostMapping("book/{bookId}/image")
    public String handleImagePost(@PathVariable Long bookId, @RequestParam("imagefile") MultipartFile file){

        Book savedBook = bookService.findById(bookId);

        if(savedBook != null) {
            imageService.saveBookImage(savedBook, file);
        }

        return "redirect:/book/" + bookId + "/show";
    }

    @GetMapping("book/{bookId}/bookimage")
    public void renderImageFromDB(@PathVariable Long bookId, HttpServletResponse response) throws IOException {
        Book book = bookService.findById(bookId);
        if (book.getImage() != null) {
            byte[] byteArray = new byte[book.getImage().length];
            int i = 0;

            for (Byte wrappedByte : book.getImage()){
                byteArray[i++] = wrappedByte;
            }

            response.setContentType("image/jpeg");
            InputStream is = new ByteArrayInputStream(byteArray);
            IOUtils.copy(is, response.getOutputStream());
        }
    }
}
