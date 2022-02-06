package com.example.cinema.config.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.Duration;
import java.util.Objects;

@Entity
public class Film {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int film_id;
    private String title_ru;
    private String title_en;
    private Duration duration;
    //Path to img=(title_en).jpg


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Film)) return false;
        Film films = (Film) o;
        return film_id == films.film_id && title_ru.equals(films.title_ru) && title_en.equals(films.title_en) && duration.equals(films.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(film_id, title_ru, title_en, duration);
    }

    public Film() {
    }

    public int getFilm_id() {
        return film_id;
    }

    public void setFilm_id(int film_id) {
        this.film_id = film_id;
    }

    public String getTitle_ru() {
        return title_ru;
    }

    public void setTitle_ru(String title_ru) {
        this.title_ru = title_ru;
    }

    public String getTitle_en() {
        return title_en;
    }

    public void setTitle_en(String title_en) {
        this.title_en = title_en;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }
}
