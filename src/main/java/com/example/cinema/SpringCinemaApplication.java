package com.example.cinema;

import com.example.cinema.model.repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.Duration;
import java.time.LocalTime;


@SpringBootApplication
public class SpringCinemaApplication {

    public static void main(String[] args) {

        SpringApplication.run(SpringCinemaApplication.class, args);
    }



}
