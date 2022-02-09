package com.example.cinema.model.repository;

import com.example.cinema.entities.Film;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface FilmRepository extends CrudRepository<Film, Long> {

    public List<Film> findAllByTitleEnContainsOrTitleRuContainsAndBoxOfficeTrue(String search_ru, String search_en,Pageable pageable);
    public List<Film> findAllByTitleEnContainsOrTitleRuContainsAndBoxOfficeFalse(String search_ru, String search_en,Pageable pageable);
    public List<Film> findAllByTitleEnContainsOrTitleRuContains(String titleEn, String titleRu);


    @Transactional
    @Modifying
    @Query("update Film f set f.boxOffice=?2 where f.film_id = ?1")
    void update_status(Long id, boolean b);
}
