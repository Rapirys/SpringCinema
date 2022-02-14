package com.example.cinema.contrloller.Admin;


import com.example.cinema.entities.Film;
import com.example.cinema.entities.Session;
import com.example.cinema.model.service.Hall.HallTopology;
import com.example.cinema.model.repository.FilmRepository;
import com.example.cinema.model.repository.SessionRepository;
import com.example.cinema.model.service.SortManager;
import com.example.cinema.model.service.Validator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/session")
public class SessionController {
    private final static Logger logger = Logger.getLogger(SessionController.class);
    @Autowired
    SortManager sortManager;
    @Autowired
    HallTopology hallTopology;
    @Autowired
    FilmRepository filmRepository;
    @Autowired
    Validator validator;
    @Autowired
    SessionRepository sessionRepository;


    @GetMapping
    public String session(@RequestParam(name = "search", defaultValue = "") String search,
                         @RequestParam(name ="sort", defaultValue = "time") String sort,
                         @RequestParam(name = "status", defaultValue = "Any") String status,
                         @RequestParam(name = "direction", defaultValue = "false") boolean direction,
                         @RequestParam (name="page", defaultValue = "1") int page,
                         @RequestParam (name="quantity", defaultValue = "10") int quantity, Model model) {
        List<Film> films = filmRepository.findByBoxOfficeTrueOrderByTitleEn();
        model.addAttribute("films", films);
        List<Session> sessions =sortManager.findSession(search, sort, status, page,quantity, direction);
        if (!model.containsAttribute("sessions")) {
            model.addAttribute("maxPage", 1);
            model.addAttribute("sessions", sessions);
        }
        model.addAttribute("maxPage", sessions.size()/quantity+1);
        model.addAttribute("page",page);
        model.addAttribute("quantity",quantity);
        model.addAttribute("search",search);


        return "session";
    }
    @PostMapping("/add")
    public String film_add(@RequestParam(name="date1") LocalDate date1,
                           @RequestParam(name="date2") LocalDate date2,
                           @RequestParam(name = "price") int price,
                           @RequestParam(name = "film_id") Long film_id,
                           @RequestParam(name = "time") LocalTime time,
                           RedirectAttributes redirectAttributes) {
        Optional<Film> filmO=filmRepository.findById(film_id);
         if (filmO.isEmpty()){
             redirectAttributes.addFlashAttribute("error","No_movie_found");
             return "redirect:/admin/session";
         }
         Film film=filmO.get();
         Session session = new Session();
         session.setFilm(film).setTime(time).setPrice(price);
         Optional<List<Session>> sessionCollisionO =sortManager.findSessionCollisionOrSave(date1, date2,session);
         if (sessionCollisionO.isPresent()){
             List<Session> sessionCollision=sessionCollisionO.get();
             redirectAttributes.addFlashAttribute("error","There_is_a_session_at_this_time");
             redirectAttributes.addFlashAttribute("sessions",sessionCollision);
             return "redirect:/admin/session";
         }

        return "redirect:/admin/session";
    }

    @GetMapping("/delete")
    public ResponseEntity<HttpStatus> delete(@RequestParam("id") Long id) {
        sessionRepository.deleteById(id);
        logger.debug("Delete session session_id:"+id);
        return ResponseEntity.ok().body(HttpStatus.OK);
    }
    @ModelAttribute
    public void newEntity(Model model)
    {
        model.addAttribute("hallCapacity", hallTopology.size());
        model.addAttribute("currentDate", LocalDate.now());
        model.addAttribute("currentTime", LocalDateTime.now().getSecond());
    }

}
