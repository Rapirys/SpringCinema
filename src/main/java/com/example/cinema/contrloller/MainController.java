package com.example.cinema.contrloller;


import com.example.cinema.entities.Film;
import com.example.cinema.model.repository.UserRepository;
import com.example.cinema.model.service.CookieManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Controller
public class MainController {
    private final static Logger logger = Logger.getLogger(RegisterController.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    CookieManager cookieManager;

    @GetMapping()
    public String main(Model model) {

        return "main";
    }
    @PostMapping("/")
    public String menu(@RequestParam(name = "search") String search, @RequestParam(name = "availability", defaultValue = "false") boolean availability,
                       @RequestParam(name = "session", defaultValue = "time") String session, @RequestParam(name = "films", defaultValue = "title") String films,
                       @RequestParam(name="date1") LocalDate date1, @RequestParam(name="date2") LocalDate date2, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("sortStrategy","аааааааааааааааа");
        return "redirect:/";
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
    }

}