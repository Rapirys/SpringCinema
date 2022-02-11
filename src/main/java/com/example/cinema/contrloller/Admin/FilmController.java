package com.example.cinema.contrloller.Admin;

import com.example.cinema.entities.Film;
import com.example.cinema.model.repository.FilmRepository;
import com.example.cinema.model.service.SortManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/film")
public class FilmController {
    private final static Logger logger = Logger.getLogger(FilmController.class);
    @Autowired
    SortManager sortManager;
    @Autowired
    FilmRepository filmRepository;
    @Value("${upload.path}")
    String path;

    @GetMapping
    public String film(@RequestParam(name = "search", defaultValue = "") String search,
                       @RequestParam(name ="sort", defaultValue = "titleEn") String sort,
                       @RequestParam(name = "status", defaultValue = "at_box_office") String status,
                       @RequestParam(name = "direction", defaultValue = "false") String direction,
                       @RequestParam (name="page", defaultValue = "1") int page,
                       @RequestParam (name="quantity", defaultValue = "10") int quantity, Model model) {
        List<Film> films = sortManager.findFilms(search, sort, status, page,quantity, direction);
        model.addAttribute("maxPage", films.size()/quantity+1);
        model.addAttribute("page",page);
        model.addAttribute("quantity",quantity);
        model.addAttribute("search",search);
        model.addAttribute("films", films);
        return "film_ad";
    }

    @PostMapping("/add")
    public String film_add(@ModelAttribute("newFilm") Film newFilm, @RequestParam("image") MultipartFile file) {
        newFilm=filmRepository.save(newFilm);
        try {
            file.transferTo(new File(path+newFilm.getFilm_id()+".jpeg"));
            logger.debug("Add new movie film_id:"+newFilm.getFilm_id());
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Problem with loading poster for film_id:"+newFilm.getFilm_id(), e);
        }
        return "redirect:/admin/film";
    }

    @GetMapping("/delete")
    public ResponseEntity<HttpStatus> delete(@RequestParam("id") Long id) {
        filmRepository.deleteById(id);
        if (new File(path+id+".jpeg").delete())
            logger.debug("Delete movie film_id:"+id);
        else logger.warn("Problem with delete poster for film_id:+newFilm.getFilm_id()");
        return ResponseEntity.ok().body(HttpStatus.OK);
    }

    @GetMapping("/swap_status")
    public ResponseEntity<HttpStatus> swap_status(@RequestParam("id") Long id, @RequestParam("status") boolean current) {
        filmRepository.update_status(id, current);
        return ResponseEntity.ok().body(HttpStatus.OK);
    }

    @ModelAttribute
    public void newEntity(Model model)
    {
        model.addAttribute("newFilm",new Film());
    }

}
