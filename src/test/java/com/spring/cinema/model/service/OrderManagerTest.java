package com.spring.cinema.model.service;

import com.spring.cinema.entities.Order;
import com.spring.cinema.entities.Ticket;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class OrderManagerTest {
    @Autowired
    OrderManager orderManager;

    @Test
    void generateTickets() {
        List<String> data= Arrays.asList("1_1","2_2","3_3","4_4");
        Order order =new Order();
        order.setOrder_id(1L);
        List<Ticket> tickets =orderManager.generateTickets(data,order);
        int i=0;
        for (Ticket ticket:tickets){
            i++;
            assertEquals(i, ticket.getPlace());
            assertEquals(i, ticket.getRow());
            assertEquals(ticket.getOrder().getOrder_id(),1L);
        }
    }

}