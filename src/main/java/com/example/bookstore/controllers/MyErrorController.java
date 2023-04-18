package com.example.bookstore.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@Controller
public class MyErrorController implements ErrorController {

    @GetMapping("/error")
    public ModelAndView errorHandler(HttpServletRequest req) {
        ModelAndView mav = new ModelAndView("error/404error");
        Object statusCode = req.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        mav.addObject("code", statusCode);
        return mav;
    }

    public String getErrorPath() {
        return "/error";
    }
}