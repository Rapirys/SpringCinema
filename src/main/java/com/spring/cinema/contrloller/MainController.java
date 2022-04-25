package com.spring.cinema.contrloller;


import com.spring.cinema.entities.Film;
import com.spring.cinema.entities.Session;
import com.spring.cinema.model.repository.FilmRepository;
import com.spring.cinema.model.service.CookieManager;
import com.spring.cinema.model.service.SortManager;
import com.spring.cinema.model.service.Validator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.*;

@Controller
public class MainController {
    private final static Logger logger = Logger.getLogger(MainController.class);
    @Autowired
    SortManager sortManager;
    @Autowired
    FilmRepository filmRepository;
    @Autowired
    Validator validator;
    @Autowired
    CookieManager cookieManager;

    @GetMapping()
    public String main(Model model,
                       @RequestParam(name = "search", defaultValue = "") String search,
                       @RequestParam(name = "availability", defaultValue = "false") Boolean availability,
                       @RequestParam(name = "sort_session", defaultValue = "time") String sort_session,
                       @RequestParam(name = "sort_film", defaultValue = "titleEn") String sort_film,
                       @RequestParam(name="date1", required = false) LocalDate date1,
                       @RequestParam(name="date2", required = false) LocalDate date2) {
        LinkedList<Film> films=sortManager.findSimpleFilms(search,sort_film);
        date1=validator.toValidDate(date1,0);
        date2=validator.toValidDate(date2,7);
        if (date2.isBefore(date1))
            date2=date1;
        if (date1.plusDays(14).isBefore(date2))
            date2=date1.plusDays(14);
        HashMap<Film, List<List<Session>>> sessions=sortManager.tableSessionByFilm(films,sort_session, sort_film, date1, date2, availability);
        if (films.size()==1)
            films.add(films.get(0));
        if (films.size()==0)
            return "redirect:/zero_films";
        model.addAttribute("sessions",sessions);
        model.addAttribute("films",films);
        return "main";
    }
    @GetMapping("/zero_films")
    public String zero() {
        return "zero_films";
    }

    @GetMapping("/command/lang")
    public ResponseEntity<?> lang(HttpServletRequest request, HttpServletResponse response){
        cookieManager.changLang();
        return ResponseEntity.ok().body(HttpStatus.OK);
    }

    @ModelAttribute
    public void newEntity(Model model)
    {
        model.addAttribute("localDate", LocalDate.now());
    }}