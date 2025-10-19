package com.beyancoback.beyanco.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class HomeController {

    @GetMapping("/")
    @ResponseBody
    public String home(HttpServletRequest req) {
        return "Welcome to the application! Req: " + req.getSession().getId();
    }
}
