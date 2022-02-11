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


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;

@Service
public class SortManager {
    @Autowired
    FilmRepository filmRepository;
    @Autowired
    SessionRepository sessionRepository;

    public List<Film> findFilms(String search, String sort, String status, int page, int quantity, String desc) {
        Sort sortPattern;
        sortPattern = getDirection(desc, Sort.by(sort));
        Pageable pageable= PageRequest.of(page-1,quantity,sortPattern);

        if (status.equals("Any"))
            return filmRepository.findAllByTitleEnContainsOrTitleRuContains(search,search,pageable);
        else if (status.equals("at_box_office"))
             return filmRepository.findAllByTitleEnContainsOrTitleRuContainsAndBoxOfficeTrue(search,search, pageable);
        else return  filmRepository.findAllByTitleEnContainsOrTitleRuContainsAndBoxOfficeFalse(search,search, pageable);
    }
    public List<Session> findSession(String search, String sort, String status, int page, int quantity, String direction) {
        Sort sortPattern;
        if (sort.equals("time")) {
            sortPattern = Sort.by("date");
            sortPattern = getDirection(direction, sortPattern);
            sortPattern = sortPattern.and(Sort.by("time"));
            sortPattern = getDirection(direction, sortPattern);
        }
        else {
            sortPattern = Sort.by(sort);
            sortPattern = getDirection(direction, sortPattern);
        }
        Pageable pageable= PageRequest.of(page-1,quantity,sortPattern);
        switch (status){
            case "Any": return sessionRepository.findAllByFilmTitleEnContains(search);
            case "movie_is_passed": return sessionRepository.findAllByFilmTitleEnContainsAndPast(LocalTime.now(), search, pageable);
            default:  return sessionRepository.findAllByFilmTitleEnContainsAndWillBeShown( LocalTime.now(), search, pageable);
        }

    }

    private Sort getDirection(String direction, Sort sortPattern) {
        if (direction.equals("true"))
            sortPattern = sortPattern.descending();
        else sortPattern = sortPattern.ascending();
        return sortPattern;
    }

    public List<Film> findFilmsAtBoxOffice() {
        return  filmRepository.findByBoxOfficeTrueOrderByTitleEn();
    }



    public List<Session> findSessionCollision(LocalDate date1, LocalDate date2, LocalTime time1, LocalTime time2) {
        List<Session> collision = new LinkedList<>();
        for (Session session: sessionRepository.findSessionCollision(date1, date2)) {
            if ((time1.isBefore(session.getTime()) && time2.isAfter(session.getTime())) ||
               (time1.isBefore(session.getEndTime()) && time2.isAfter(session.getEndTime())))
                collision.add(session);
        }
        return collision;
    }
}
