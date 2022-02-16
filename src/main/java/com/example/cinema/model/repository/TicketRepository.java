package com.example.cinema.model.repository;


import com.example.cinema.entities.Order;
import com.example.cinema.entities.Ticket;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


import java.util.List;


@Repository
public interface TicketRepository extends CrudRepository<Ticket, Long> {
    @Query(value = "  SELECT * FROM findHall(?1)  ORDER BY \"row\", place", nativeQuery = true)
    List<Ticket> getHallBySession(Long id);
    List<Ticket> findTicketsByOrder(Order order);
    int countTicketByOrder(Order order);

}