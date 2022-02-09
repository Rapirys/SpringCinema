package com.example.cinema.entities;

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
    private Session session;
    private LocalDateTime time;
    private boolean active;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return active == order.active && order_id.equals(order.order_id) && tickets.equals(order.tickets) && session.equals(order.session) && time.equals(order.time);
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

    public void setOrder_id(Long order_id) {
        this.order_id = order_id;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> comments) {
        this.tickets = comments;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
