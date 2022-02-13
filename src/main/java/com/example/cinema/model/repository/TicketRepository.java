package com.example.cinema.model.repository;

import com.example.cinema.entities.Order;
import com.example.cinema.entities.Session;
import com.example.cinema.entities.Ticket;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Repository
public interface TicketRepository extends CrudRepository<Ticket, Long> {

}