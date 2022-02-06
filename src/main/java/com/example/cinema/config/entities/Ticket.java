package com.example.cinema.config.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "tickets", indexes = @Index(name = "ticket_index", columnList = "order_id"))
public class Ticket {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long ticket_id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    private int  place;
    private int row;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ticket)) return false;
        Ticket ticket = (Ticket) o;
        return ticket_id.equals(ticket.ticket_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticket_id, place, row);
    }

    public Ticket() {
    }

    public Long getTicket_id() {
        return ticket_id;
    }

    public void setTicket_id(Long ticket_id) {
        this.ticket_id = ticket_id;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }
}
