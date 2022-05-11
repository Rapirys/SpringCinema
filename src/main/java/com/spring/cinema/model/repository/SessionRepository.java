package com.spring.cinema.model.repository;


import com.spring.cinema.entities.Film;
import com.spring.cinema.entities.Session;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


@Repository
public interface SessionRepository extends CrudRepository<Session, Long> {


    List<Session> findAllByFilmTitleEnContains(String search, Pageable pageable);

    @Query("SELECT s FROM Session s where ((s.date>current_date) OR (s.date=current_date AND s.time>=?1))" +
            " AND s.film.titleEn LIKE %?2%")
    List<Session> findAllByFilmTitleEnContainsAndWillBeShown(LocalTime current_time, String search, Pageable pageable);

    @Query("SELECT s FROM Session s where ((s.date>current_date ) OR (s.date=current_date AND s.time>=?3))" +
            " AND s.film=?1 AND s.date=?2 AND s.occupancy<?4")
    List<Session> findAllBetween(Film film, LocalDate date1, LocalTime current, int occupancy, Sort sort);

    @Query("SELECT s FROM Session s where ((s.date<current_date) OR (s.date=current_date AND s.time<?1))" +
            " AND s.film.titleEn LIKE %?2%")
    List<Session> findAllByFilmTitleEnContainsAndPast(LocalTime current_time, String search, Pageable pageable);


    @Query("SELECT s FROM Session s inner join Film f on s.film=f where (s.date between ?1 and ?2)")
    List<Session> findSessionCollision(LocalDate date1, LocalDate date2);
}
