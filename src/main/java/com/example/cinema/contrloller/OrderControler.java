package com.example.cinema.contrloller;


import com.example.cinema.entities.Session;
import com.example.cinema.model.HallTopology;
import com.example.cinema.model.repository.SessionRepository;
import com.example.cinema.model.service.OrderManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class OrderControler {
    @Autowired
    OrderManager orderManager;
    @Autowired
    HallTopology topology;
    @Autowired
    SessionRepository sessionRepository;
    @GetMapping("/place")
    public String place(Model model, HttpServletRequest request
                       ,@RequestParam(name = "id") Long id) {
        Optional<Session> o= sessionRepository.findById(id);
        Session session;
        if (o.isPresent())
            session = o.get();
        else return "redirect:/main";
        model.addAttribute("mySession", session);
        model.addAttribute("topology", topology.get());
        return "place";
    }
    @PostMapping(value = "/order")
    public ResponseEntity<?> order(@RequestParam(name = "data") List<String> data,
                                   @RequestParam(name = "session_id") Long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (orderManager.book(data,id,auth.getName()))
          return ResponseEntity.ok().body(HttpStatus.OK);
        else return ResponseEntity.ok().body(HttpStatus.EXPECTATION_FAILED);
    }




}
