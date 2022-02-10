package com.example.cinema.contrloller;

import com.example.cinema.entities.Role;
import com.example.cinema.entities.User;
import com.example.cinema.model.repository.UserRepository;
import com.example.cinema.model.service.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Controller
@RequestMapping("/register")
public class RegisterController {



    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Validator valid;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String register (){
        return "register";
    }

    @PostMapping
    public String addUser (User user, RedirectAttributes redirectAttributes){
        List<String> s=valid.validUserFields(user);
        if (s.size()!=0) {
            redirectAttributes.addFlashAttribute("message",s);
            return "redirect:/register";
        }
        Set<Role> roles= new HashSet<>();
        roles.add(Role.USER);
        user.setRoles(roles).setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            userRepository.save(user);
        } catch (Exception e){
            s.add("User_already_exist");
            redirectAttributes.addFlashAttribute("message",s);
            return "redirect:/register";
        }
        return "redirect:/login";
    }

}
