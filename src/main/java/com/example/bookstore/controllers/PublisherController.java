package com.example.bookstore.controllers;

import com.example.bookstore.service.PublisherService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PublisherController {
    private final PublisherService publisherService;

    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @RequestMapping({"/publishers", "/publishers/show"})
    public String getPublishers(Model model){
        model.addAttribute("publishers", publisherService.findAll());
        return "publisher/index";
    }

    @RequestMapping("/publisher/{id}/show")
    public String showById(@PathVariable String id, Model model){
        model.addAttribute("publisher", publisherService.findById(Long.valueOf(id)));
        return "publisher/show";
    }
}
