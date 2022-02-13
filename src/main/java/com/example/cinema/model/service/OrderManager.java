package com.example.cinema.model.service;

import com.example.cinema.entities.Order;
import com.example.cinema.entities.Session;
import com.example.cinema.entities.Ticket;
import com.example.cinema.entities.User;
import com.example.cinema.model.repository.OrderRepository;
import com.example.cinema.model.repository.SessionRepository;
import com.example.cinema.model.repository.TicketRepository;
import com.example.cinema.model.repository.UserRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderManager {
    private final static Logger logger = Logger.getLogger(OrderManager.class);
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    SessionRepository sessionRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TicketRepository ticketRepository;

    @Transactional
    public boolean book(List<String> data,Long session_id, String username){
        User user = userRepository.findByUsername(username);
        try{
            Order order= new Order();
            Session session= sessionRepository.findById(session_id).orElseThrow(SessionNotExist::new);
            order.setUser(user);
            order.setSession(session);
            order.setActive(false);
            order.setTime(LocalDateTime.now());
            orderRepository.deleteBySessionAndActiveFalseAndTimeBefore(session,LocalDateTime.now().minusMinutes(15));
            order=orderRepository.save(order);
            List<Ticket> tickets=generateTickets(data, order);
            ticketRepository.saveAll(tickets);
            logger.debug("User with id: "+user.getId()+ " booked seats for session id: "+session_id+" order id: "+ order.getOrder_id());
        }catch (SessionNotExist e){
            logger.error("User with id: "+user.getId()+ " tried to book seats for session id: "+session_id+" bat session noe Exist.");
        }
        catch (Exception e){
            logger.info("User with id: "+user.getId()+ " tried to book seats for session id: "+session_id+" and failed.");
            return false;
        }
        return true;
    }


    public List<Ticket> generateTickets(List<String> data, Order order){
        List<Ticket> tickets = new ArrayList();
        for (String s:data){
            String[] t=s.split("_");
            Ticket ticket= new Ticket(Integer.parseInt(t[0]),Integer.parseInt(t[1]), order);
            tickets.add(ticket);
        }
        return tickets;
    }
}

class SessionNotExist extends Exception{
    public SessionNotExist(String message){
        super(message);
    }
    public SessionNotExist(){
    }
}