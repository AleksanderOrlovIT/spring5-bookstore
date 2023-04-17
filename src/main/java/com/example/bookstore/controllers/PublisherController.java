package com.example.bookstore.controllers;

import com.example.bookstore.model.Publisher;
import com.example.bookstore.service.PublisherService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
public class PublisherController {

    private final static String publisherForm = "/publisher/publisherform";

    private final PublisherService publisherService;

    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @RequestMapping({"/publishers", "/publishers/show"})
    public String getAllPublishers(Model model){
        model.addAttribute("publishers", publisherService.findAll());
        return "publisher/index";
    }

    @RequestMapping("/publisher/{id}/show")
    public String showPublisherById(@PathVariable String id, Model model){
        model.addAttribute("publisher", publisherService.findById(Long.valueOf(id)));
        return "publisher/show";
    }

    @GetMapping("/publisher/new")
    public String initCreationForm(Model model){
        model.addAttribute("publisher", Publisher.builder().build());
        return publisherForm;
    }

    @PostMapping("/publisher/new")
    public String processCreationForm(@Valid Publisher publisher, BindingResult result){
        if(result.hasErrors()){
            return publisherForm;
        }else{
            Publisher savedPublisher = publisherService.save(publisher);
            return "redirect:/publisher/" + savedPublisher.getId() + "/show";
        }
    }

    @GetMapping("/publisher/{id}/update")
    public String initUpdatePublisherForm(@PathVariable Long id, Model model){
        model.addAttribute(publisherService.findById(id));
        return publisherForm;
    }

    @PostMapping("/publisher/{id}/update")
    public String processUpdatePublisherForm(@Valid Publisher publisher, BindingResult result, @PathVariable Long id){
        if(result.hasErrors()){
            return publisherForm;
        }else{
            publisherService.save(publisher);
            return "redirect:/publisher/" + publisher.getId() + "/show";
        }
    }

    @RequestMapping("publisher/{id}/delete")
    public String deletePublisher(@PathVariable Long id){
        publisherService.deleteById(id);
        return "redirect:/publishers";
    }
}
