package com.example.cinema.contrloller;

import com.example.cinema.config.entities.User;
import com.example.cinema.model.service.CookieManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/command")
public class CommandController {
    static final Logger logger = LogManager.getLogger(CommandController.class);
    @Autowired
    CookieManager cookieManager;

    @GetMapping("/lang")
    public ResponseEntity<?> lang(HttpServletRequest request, HttpServletResponse response){
        cookieManager.changLang();
        logger.info("AAAAAAAAAAAAAA");
        return ResponseEntity.ok().body(HttpStatus.OK);
    }

}
