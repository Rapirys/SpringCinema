package com.example.cinema.contrloller;


import com.example.cinema.config.entities.User;
import com.example.cinema.model.repository.UserRepository;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.http.HttpResponse;

@Controller
public class MainController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping()
    public String greeting(Model model) {
        Iterable<User> users = userRepository.findAll();
        model.addAttribute("users",users);
        return "main";
    }



}