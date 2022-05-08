package com.spring.cinema.entities;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders", indexes = @Index(name = "order_index", columnList = "session_id"))
public class Order {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long order_id;
    @ManyToOne
    User user;

    @OneToMany(mappedBy = "ticket_id", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> tickets =new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "session_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Session session;
    private LocalDateTime time;
    private boolean active;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return tickets.equals(order.tickets);
    }

    @Override
    public int hashCode() {
        return Objects.hash(order_id, tickets, session, time, active);
    }

    public Order() {
    }

    public Long getOrder_id() {
        return order_id;
    }

    public Order setOrder_id(Long order_id) {
        this.order_id = order_id;
        return this;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public Order setTickets(List<Ticket> comments) {
        this.tickets = comments;
        return this;
    }

    public Session getSession() {
        return session;
    }

    public Order setSession(Session session) {
        this.session = session;
        return this;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public Order setTime(LocalDateTime time) {
        this.time = time;
        return this;
    }

    public boolean isActive() {
        return active;
    }

    public Order setActive(boolean active) {
        this.active = active;
        return this;
    }
    public User getUser() {
        return user;
    }

    public Order setUser(User user) {
        this.user = user;
        return this;
    }
}
