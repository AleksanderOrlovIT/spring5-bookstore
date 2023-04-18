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

    private final ImageService imageService;
    private final AuthorService authorService;

    public AuthorImageController(ImageService imageService, AuthorService authorService) {
        this.imageService = imageService;
        this.authorService = authorService;
    }

    @GetMapping("author/{authorId}/image")
    public String showUploadForm(@PathVariable Long authorId, Model model){
        model.addAttribute("author", authorService.findById(authorId));

        return "author/imageuploadform";
    }

    @PostMapping("author/{authorId}/image")
    public String handleImagePost(@PathVariable Long authorId, @RequestParam("imagefile") MultipartFile file){
        Author savedAuthor = authorService.findById(authorId);

        if(savedAuthor != null) {
            imageService.saveAuthorImage(authorService.findById(authorId), file);
        }else {
            log.error("AuthorImageController savedAuthor with id :" + authorId + " is null");
        }
        return "redirect:/author/" + authorId + "/show";
    }

    @GetMapping("author/{authorId}/authorimage")
    public void renderImageFromDB(@PathVariable Long authorId, HttpServletResponse response) throws IOException {
        Author author = authorService.findById(authorId);
        if (author.getImage() != null) {
            byte[] byteArray = new byte[author.getImage().length];
            int i = 0;

            for (Byte wrappedByte : author.getImage()){
                byteArray[i++] = wrappedByte;
            }

            response.setContentType("image/jpeg");
            InputStream is = new ByteArrayInputStream(byteArray);
            IOUtils.copy(is, response.getOutputStream());
        }
    }
}
