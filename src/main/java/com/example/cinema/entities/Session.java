package com.example.cinema.entities;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(indexes = @Index(name = "date_index", columnList = "date"))
public class Session {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long session_id;

    @ManyToOne
    @JoinColumn(name = "film_id")
    private Film film;

    private LocalDate date;
    private LocalTime time;
    private int price;

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    public Session() {
    }

    public Long getSession_id() {
        return session_id;
    }

    public void setSession_id(Long session_id) {
        this.session_id = session_id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
