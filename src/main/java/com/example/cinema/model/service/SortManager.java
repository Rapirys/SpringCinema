package com.example.cinema.model.service;

import com.example.cinema.entities.Film;
import com.example.cinema.entities.Session;
import com.example.cinema.model.repository.FilmRepository;
import com.example.cinema.model.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SortManager {
    @Autowired
    FilmRepository filmRepository;
    @Autowired
    SessionRepository sessionRepository;

    public List<Film> findFilms(String search, String sort, String status, int page, int quantity, String desc) {
        search=search;
        Sort sortPattern;
        if (desc.equals("true"))
            sortPattern=Sort.by(sort).descending();
        else sortPattern=Sort.by(sort).ascending();
        Pageable pageable= PageRequest.of(page-1,quantity,sortPattern);

        if (status.equals("Any"))
            return filmRepository.findAllByTitleEnContainsOrTitleRuContains(search,search);
        else if (status.equals("at_box_office"))
             return filmRepository.findAllByTitleEnContainsOrTitleRuContainsAndBoxOfficeTrue(search,search, pageable);
        else return  filmRepository.findAllByTitleEnContainsOrTitleRuContainsAndBoxOfficeFalse(search,search, pageable);
    }
    public List<Film> findFilmsAtBoxOffice() {
        return  filmRepository.findByBoxOfficeTrueOrderByTitleEn();
    }
    public List<Session> findSession(String search, String sort, String status, int page, int quantity, String direction) {
        return sessionRepository.findAll();
    }


    public List<Session> findSessionCollision(LocalDate date1, LocalDate date2, LocalTime time1, LocalTime time2) {
        ArrayList<Session> collision = new ArrayList();
        for (Session session: sessionRepository.findSessionCollision(date1, date2)) {
            if ((time1.isAfter(session.getTime()) && time1.)
            || (time1.isBefore(session.getTime()) && session.getEndTime().isAfter(time1)))
        }
        return
    }
}
