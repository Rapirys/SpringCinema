package com.example.cinema.model.repository;

import com.example.cinema.entities.Order;
import com.example.cinema.entities.Session;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {

    @Transactional
    @Modifying
    void deleteBySessionAndActiveFalseAndTimeBefore(Session session, LocalDateTime localDateTime);
}
