package com.example.cinema.contrloller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

@Controller
public class OrderControler {
    @Autowired
    LocaleResolver localeResolver;
    @GetMapping("/place")
    public String place(Model model, HttpServletRequest request){
        Locale locale=localeResolver.resolveLocale(request);
        System.out.println(locale.getLanguage());
        model.addAttribute("title","2134");
        return "place";
    }
}
