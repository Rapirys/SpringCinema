package com.example.cinema.contrloller;


import com.example.cinema.config.entities.User;
import com.example.cinema.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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

    @GetMapping("/command/lang")
    public void lang(Model model) {
        System.out.println(11111);
    }

}