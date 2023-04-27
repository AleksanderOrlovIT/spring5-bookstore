package com.example.bookstore.controllers.imagecontrollers;

import com.example.bookstore.model.Publisher;
import com.example.bookstore.service.ImageService;
import com.example.bookstore.service.PublisherService;
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
public class PublisherImageController {

    private final static String errorPage = "error/400error";

    private final ImageService imageService;
    private final PublisherService publisherService;

    public PublisherImageController(ImageService imageService, PublisherService publisherService) {
        this.imageService = imageService;
        this.publisherService = publisherService;
    }

    @GetMapping("/publisher/{publisherId}/newImage")
    public String showUploadForm(@PathVariable Long publisherId, Model model){
        Publisher publisher = publisherService.findById(publisherId);
        if(publisher == null){
            model.addAttribute("exception", new Exception("There is no publisher with id: " + publisherId));
            return errorPage;
        }
        model.addAttribute("publisher", publisher);
        return "publisher/imageuploadform";
    }

    @PostMapping("/publisher/{publisherId}/newImage")
    public String handleImagePost(@PathVariable Long publisherId, @RequestParam("imagefile") MultipartFile file, Model model){
        Publisher savedPublisher = publisherService.findById(publisherId);
        if(savedPublisher != null) {
            imageService.savePublisherImage(savedPublisher, file);
        }else {
            model.addAttribute("exception", new Exception("There is no publisher with id: " + publisherId));
            return errorPage;
        }
        return "redirect:/publisher/" + publisherId + "/show";
    }

    @GetMapping("publisher/{publisherId}/image")
    public void renderImageFromDB(@PathVariable Long publisherId, HttpServletResponse response) throws IOException {
        Publisher publisher = publisherService.findById(publisherId);
        if(publisher == null){
            log.error("PublisherImageController.renderImageFromDB(), error : there is no publisher with id" + publisherId);
        } else if (publisher.getImage() != null) {
            byte[] byteArray = new byte[publisher.getImage().length];
            int i = 0;

            for (Byte wrappedByte : publisher.getImage()){
                byteArray[i++] = wrappedByte;
            }

            response.setContentType("image/jpeg");
            InputStream is = new ByteArrayInputStream(byteArray);
            IOUtils.copy(is, response.getOutputStream());
        }
    }
}
