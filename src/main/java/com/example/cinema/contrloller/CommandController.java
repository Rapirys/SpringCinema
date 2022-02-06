package com.example.cinema.contrloller;


import com.example.cinema.model.service.CookieManager;


import org.apache.log4j.Logger;
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
    private final static Logger logger = Logger.getLogger(RegisterController.class);
    @Autowired
    CookieManager cookieManager;

    @GetMapping("/lang")
    public ResponseEntity<?> lang(HttpServletRequest request, HttpServletResponse response){
        cookieManager.changLang();
        return ResponseEntity.ok().body(HttpStatus.OK);
    }

}
