package com.example.bookstore.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
    @RequestMapping({"", "/", "/index"})
    public String getHomePage(){
        return "index";
    }
}
