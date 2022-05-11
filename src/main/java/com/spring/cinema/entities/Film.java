package com.spring.cinema.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.Duration;
import java.util.Objects;

@Entity
public class Film {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long film_id;
    private String titleRu;
    private String titleEn;
    private Duration duration;
    private boolean boxOffice = true;
    //Path to img=(title_en).jpg


    public String getTitleLocale(String locale) {
        return (locale.equals("en")) ? getTitleEn() : getTitleRu();
    }

    public String getFormatDuration() {
        return String.format("%02d", duration.toHours()) + ':' + String.format("%02d", duration.toMinutesPart());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Film)) return false;
        Film films = (Film) o;
        return film_id.equals(films.film_id) && titleRu.equals(films.titleRu) && titleEn.equals(films.titleEn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(film_id, titleRu, titleEn, duration);
    }

    public Film() {
    }

    public boolean isBoxOffice() {
        return boxOffice;
    }

    public void setBoxOffice(boolean box_office) {
        this.boxOffice = box_office;
    }

    public Long getFilm_id() {
        return film_id;
    }

    public void setFilm_id(Long film_id) {
        this.film_id = film_id;
    }

    public String getTitleRu() {
        return titleRu;
    }

    public void setTitleRu(String title_ru) {
        this.titleRu = title_ru;
    }

    public String getTitleEn() {
        return titleEn;
    }

    public void setTitleEn(String title_en) {
        this.titleEn = title_en;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }
}
