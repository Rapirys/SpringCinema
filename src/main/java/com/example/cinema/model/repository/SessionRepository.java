package com.example.cinema.model.repository;


import com.example.cinema.entities.Film;
import com.example.cinema.entities.Session;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


@Repository
public interface SessionRepository extends CrudRepository<Session, Long> {


    public List<Session> findAll();

    @Query("SELECT s FROM Session s inner join Film f on s.film=f where (s.date between ?1 and ?2)")
    List<Session> findSessionCollision(LocalDate date1, LocalDate date2);
}
