package com.example.cinema.contrloller;


import com.example.cinema.config.entities.User;
import com.example.cinema.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
public class MainController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping()
    public String main(Model model) {
        model.addAttribute("localDate", LocalDate.now());
        return "main";
    }
    @PostMapping()
    public String menu(@RequestParam(name = "search") String search, @RequestParam(name = "availability", defaultValue = "false") boolean availability,
                       @RequestParam(name = "session", defaultValue = "time") String session, @RequestParam(name = "films", defaultValue = "title") String films,
                       @RequestParam(name="date1") String date1, @RequestParam(name="date2") String date2) {
        System.out.println(search+" "+availability+" "+session+" "+films+" "+date1+" "+date2);
        return "redirect:/main";
    }


}