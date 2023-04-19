package com.example.bookstore.controllers.imagecontrollers;

import com.example.bookstore.model.Author;
import com.example.bookstore.service.AuthorService;
import com.example.bookstore.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Controller
public class AuthorImageController {

    private final static String errorPage = "error/400error";

    private final ImageService imageService;
    private final AuthorService authorService;

    public AuthorImageController(ImageService imageService, AuthorService authorService) {
        this.imageService = imageService;
        this.authorService = authorService;
    }

    @GetMapping("author/{authorId}/newImage")
    public String showUploadForm(@PathVariable Long authorId, Model model) {
        Author author = authorService.findById(authorId);
        if (author == null) {
            model.addAttribute("exception", new Exception("There is no author with id: " + authorId));
            return errorPage;
        }
        model.addAttribute("author", author);
        return "author/imageuploadform";
    }

    @PostMapping("author/{authorId}/newImage")
    public String handleImagePost(@PathVariable Long authorId, @RequestParam("imagefile") MultipartFile file, Model model) {
        Author savedAuthor = authorService.findById(authorId);
        if (savedAuthor != null) {
            imageService.saveAuthorImage(savedAuthor, file);
        } else {
            model.addAttribute("exception", new Exception("There is no author with id: " + authorId));
            return errorPage;
        }
        return "redirect:/author/" + authorId + "/show";
    }

    @GetMapping("author/{authorId}/image")
    public void renderImageFromDB(@PathVariable Long authorId, HttpServletResponse response) throws IOException {
        log.debug("Invoked");
        Author author = authorService.findById(authorId);
        if (author == null) {
            log.error("AuthorImageController.renderImageFromDB(), error : there is no author with id" + authorId);
        } else if (author.getImage() != null) {
                byte[] byteArray = new byte[author.getImage().length];
                int i = 0;

                for (Byte wrappedByte : author.getImage()) {
                    byteArray[i++] = wrappedByte;
                }

                response.setContentType("image/jpeg");
                InputStream is = new ByteArrayInputStream(byteArray);
                IOUtils.copy(is, response.getOutputStream());
                log.debug("Every thing works fine");
        }
    }
}
