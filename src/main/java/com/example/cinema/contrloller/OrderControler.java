package com.example.cinema.contrloller;


import com.example.cinema.entities.Film;
import com.example.cinema.entities.Session;
import com.example.cinema.model.HallTopology;
import com.example.cinema.model.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Optional;

@Controller
public class OrderControler {
    @Autowired
    LocaleResolver localeResolver;
    @Autowired
    HallTopology topology;
    @Autowired
    SessionRepository sessionRepository;
    @GetMapping("/place")
    public String place(Model model, HttpServletRequest request)
//            ,@RequestParam(name = "id") Long id)
    {
        Locale locale=localeResolver.resolveLocale(request);
//        Optional<Session> o= sessionRepository.findById(id);
//        Session session;
//        if (o.isPresent())
//            session = sessionRepository.findById(id).get();
//        else return "redirect:/main";
//        Film film =session.getFilm();
//        String title_for_model =(locale.getLanguage()=="en")?film.getTitleEn():film.getTitleRu();
//        model.addAttribute("title",title_for_model);
//        model.addAttribute("duration",film.getDuration().toString());
        model.addAttribute("topology", topology.get());
        return "place";
    }


}
