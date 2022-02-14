package com.example.cinema.model.service.Hall;

public class Place implements Cloneable{
    public int row, place;
    public Character type;

    public Place(int row, int place, Character type) {
        this.type = type;
        if (type == '#') {
            this.row = row;
            this.place = place;
        }
    }
    public Place(Place place) {
        this.type = place.type;
        if (type == '#') {
            this.row = place.row;
            this.place = place.place;
        }
    }
    public void setType(Character type) {
        this.type = type;
    }

}
