package com.example.cinema;

import com.example.cinema.model.repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


@SpringBootApplication
public class SpringCinemaApplication {

    public static void main(String[] args) {
        LocalDate date;
        LocalTime time;
        System.out.println();
        SpringApplication.run(SpringCinemaApplication.class, args);
    }



}
