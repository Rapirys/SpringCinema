package com.spring.cinema.model.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.PdfWriter;
import com.spring.cinema.entities.Order;
import com.spring.cinema.entities.Session;
import com.spring.cinema.entities.Ticket;
import com.spring.cinema.entities.User;
import com.spring.cinema.model.repository.OrderRepository;
import com.spring.cinema.model.repository.SessionRepository;
import com.spring.cinema.model.repository.TicketRepository;
import com.spring.cinema.model.repository.UserRepository;
import com.spring.cinema.model.service.Hall.HallTopology;
import com.spring.cinema.model.service.Hall.Place;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


/**
 * Handles all actions related to booking, orders, and payment.
 */
@Service
public class OrderManager {
    static int timeOfOrderMinute = 15;
    private final static Logger logger = Logger.getLogger(OrderManager.class);
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    SessionRepository sessionRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    HallTopology hallTopology;

    /**
     * Creates a new order, and books seats, the booking will be invalid after 15 minutes.
     *
     * @param data       List of places for tickets in the form :"row_place"
     * @param session_id id of session;
     * @param username   username for which the order is registered
     * @return id of new order;
     */
    @Transactional
    public Long book(List<String> data, Long session_id, String username){
        User user = userRepository.findByUsername(username);
        Long orderId = null;
        try {
            Session session = sessionRepository.findById(session_id).orElseThrow(SessionNotExist::new);
            Order order = new Order();
            order.setUser(user).setSession(session).setActive(false).setTime(LocalDateTime.now());
            orderRepository.deleteBySessionAndActiveFalseAndTimeBefore(session, LocalDateTime.now().minusMinutes(timeOfOrderMinute));
            order = orderRepository.save(order);
            List<Ticket> tickets = generateTickets(data, order);
            ticketRepository.saveAll(tickets);
            logger.debug("User with id: " + user.getId() + " booked seats for session id: " + session_id + " order id: " + order.getOrder_id());
            orderId = order.getOrder_id();
        } catch (SessionNotExist e) {
            logger.error("User with id: " + user.getId() + " tried to book seats for session id: " + session_id + " bat session noe Exist.");
        } catch (Exception e) {
            logger.info("User with id: " + user.getId() + " tried to book seats for session id: " + session_id + " and failed.");
        }
        return orderId;
    }


    /**
     * @param data  List of places for tickets in the form :"row_place"
     * @param order Order entity to which tickets are added.
     * @return List of ticket which can be saved in DB.
     */
    public List<Ticket> generateTickets(List<String> data, Order order) {
        List<Ticket> tickets = new ArrayList();
        for (String s : data) {
            String[] t = s.split("_");
            Ticket ticket = new Ticket(Integer.parseInt(t[0]), Integer.parseInt(t[1]), order);
            tickets.add(ticket);
        }
        return tickets;
    }

    /**
     * @return hall topology with occupied seats
     */
    public ArrayList<ArrayList<Place>> getHall(Session session) {
        List<Ticket> tickets = ticketRepository.getHallBySession(session.getSession_id());
        return hallTopology.getCopyTopology(tickets);
    }

    @Transactional
    public Order submit(Long order_id) throws Exception {
        Order order = orderRepository.findById(order_id).orElseThrow(OrderNotExist::new);
        if (!order.isActive())
            order.setActive(true);
        else throw new OrderNotExist();
        order.getSession().incOccupancy(ticketRepository.countTicketByOrder(order));
        orderRepository.save(order);
        logger.trace("Order id: " + order_id + "  has been paid.");
        return order;
    }

    /**
     * Used to get a list of order tickets in PDF.
     *
     * @param order        Entity of order for which pdf will be generated
     * @param outputStream Stream with pdf written to it
     */
    public void getPdf(Order order, OutputStream outputStream) throws DocumentException {
        List<Ticket> tickets = ticketRepository.findTicketsByOrder(order);
        Document document = new Document();
        PdfWriter.getInstance(document, outputStream);
        document.open();

        for (Ticket ticket : tickets) {
            Font font = FontFactory.getFont(FontFactory.COURIER, 20, BaseColor.BLACK);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            Paragraph paragraph = new Paragraph(ticket.getSession().getLocalDateTime().format(formatter), font);
            document.add(paragraph);
            paragraph = new Paragraph(ticket.getSession().getFilm().getTitleEn() + " " + ticket.getSession().getFilm().getTitleRu(), font);
            document.add(paragraph);
            paragraph = new Paragraph("Row: " + ticket.getRow(), font);
            document.add(paragraph);
            paragraph = new Paragraph("Place: " + ticket.getPlace(), font);
            document.add(paragraph);
            BarcodeQRCode qrCode = new BarcodeQRCode("http://localhost:8080/ticket?id=" + ticket.getTicket_id() + "&salt=" + ticket.getSalt(), 400, 400, null);
            Image codeQrImage = qrCode.getImage();
            codeQrImage.scaleAbsolute(400, 400);
            document.add(codeQrImage);
            document.newPage();
        }
        document.close();
    }
}

class SessionNotExist extends Exception {
    public SessionNotExist(String message) {
        super(message);
    }

    public SessionNotExist() {
    }
}

class OrderNotExist extends Exception {
    public OrderNotExist(String message) {
        super(message);
    }

    public OrderNotExist() {
    }
}