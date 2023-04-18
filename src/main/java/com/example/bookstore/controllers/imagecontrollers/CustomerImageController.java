package com.example.bookstore.controllers.imagecontrollers;

import com.example.bookstore.model.Customer;
import com.example.bookstore.model.Publisher;
import com.example.bookstore.service.CustomerService;
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
public class CustomerImageController {

    private final ImageService imageService;
    private final CustomerService customerService;

    public CustomerImageController(ImageService imageService, CustomerService customerService) {
        this.imageService = imageService;
        this.customerService = customerService;
    }

    @GetMapping("customer/{customerId}/image")
    public String showUploadForm(@PathVariable Long customerId, Model model){
        model.addAttribute("customer", customerService.findById(customerId));

        return "customer/imageuploadform";
    }

    @PostMapping("customer/{customerId}/image")
    public String handleImagePost(@PathVariable Long customerId, @RequestParam("imagefile") MultipartFile file){
        Customer savedCustomer = customerService.findById(customerId);

        if(savedCustomer != null) {
            imageService.saveCustomerImage(customerService.findById(customerId), file);
        }else {
            log.error("CustomerImageController savedCustomer with id :" + customerId + " is null");
        }
        return "redirect:/customer/" + customerId + "/show";
    }

    @GetMapping("customer/{customerId}/customerimage")
    public void renderImageFromDB(@PathVariable Long customerId, HttpServletResponse response) throws IOException {
        Customer customer = customerService.findById(customerId);
        if (customer.getImage() != null) {
            byte[] byteArray = new byte[customer.getImage().length];
            int i = 0;

            for (Byte wrappedByte : customer.getImage()){
                byteArray[i++] = wrappedByte;
            }

            response.setContentType("image/jpeg");
            InputStream is = new ByteArrayInputStream(byteArray);
            IOUtils.copy(is, response.getOutputStream());
        }
    }
}
