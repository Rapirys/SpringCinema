package com.example.cinema.entities;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Objects;
import java.util.Random;

@Entity
@Table(name = "tickets", indexes = @Index(name = "ticket_index", columnList = "order_id"),
       uniqueConstraints = { @UniqueConstraint( columnNames = { "place", "row","session_id"} )})
public class Ticket {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long ticket_id;


    @ManyToOne
    @JoinColumn(name = "order_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "session_id")
    private Session session;
    private int  place;
    private int row;
    private Long salt;

    public Ticket(int row, int place, Order order) {
        this.place = place;
        this.row = row;
        this.order=order;
        this.session=order.getSession();
        this.salt=new Random().nextLong();
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

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }


    public Ticket() {
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Long getSalt() {
        return salt;
    }

    public void setSalt(Long salt) {
        this.salt = salt;
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
