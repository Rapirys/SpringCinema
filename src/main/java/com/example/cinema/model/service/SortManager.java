package com.example.cinema.model.service;

import com.example.cinema.entities.Film;
import com.example.cinema.model.repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SortManager {
    @Autowired
    FilmRepository filmRepository;


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

    public Film save(Film newFilm) {
        return filmRepository.save(newFilm);
    }

    public void delete(Long id) {
        filmRepository.deleteById(id);
    }
    public void swap_status(Long id, boolean current) {
        filmRepository.update_status(id,!current);
    }
}
