package com.spring.cinema.model.service;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.spring.cinema.entities.Film;
import com.spring.cinema.entities.Order;
import com.spring.cinema.entities.Session;
import com.spring.cinema.entities.Ticket;
import com.spring.cinema.model.repository.OrderRepository;
import com.spring.cinema.model.repository.TicketRepository;
import com.spring.cinema.model.service.Hall.HallTopology;
import com.spring.cinema.model.service.Hall.Place;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static com.itextpdf.text.pdf.parser.PdfTextExtractor.getTextFromPage;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
class OrderManagerTest {

    @InjectMocks
    OrderManager orderManager;

    @Mock
    TicketRepository ticketRepository;

    @Mock
    OrderRepository orderRepository;

    @Mock
    @Autowired
    HallTopology hallTopology;

    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void generateTickets() {
        List<String> data = Arrays.asList("1_1", "2_2", "3_3", "4_4");
        Order order = new Order();
        order.setOrder_id(1L);
        List<Ticket> tickets = orderManager.generateTickets(data, order);
        int i = 0;
        for (Ticket ticket : tickets) {
            i++;
            assertEquals(i, ticket.getPlace());
            assertEquals(i, ticket.getRow());
            assertEquals(ticket.getOrder().getOrder_id(), 1L);
        }
    }

    @Test
    public void getHall() {
        Session session = new Session();
        session.setSession_id(1L);
        LinkedList<Ticket> tickets = new LinkedList<>();
        tickets.add(new Ticket());
        when(ticketRepository.getHallBySession(1L)).thenReturn(tickets);

        ArrayList<ArrayList<Place>> test = new ArrayList<>();
        when(hallTopology.getCopyTopology(tickets)).thenReturn(test);

        assertThat(orderManager.getHall(session)).isEqualTo(test);
        verify(ticketRepository, times(1)).getHallBySession(1L);
    }

    @Test
    public void getPdf() throws IOException, DocumentException {
        Order order = new Order();

        List<Ticket> tickets = new ArrayList<>();

        Session session = new Session();
        Film film = new Film();
        film.setTitleEn("Title1");
        film.setTitleRu("Title2");
        film.setDuration(Duration.ofHours(1));
        session.setDate(LocalDate.of(1984, 1, 1));
        session.setTime(LocalTime.of(12, 0));
        session.setFilm(film);
        order.setSession(session);

        Ticket ticket = new Ticket(1, 1, order);
        ticket.setTicket_id(1L);


        tickets.add(ticket);

        when(ticketRepository.findTicketsByOrder(order)).thenReturn(tickets);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        orderManager.getPdf(order, outputStream);

        InputStream inStream = new ByteArrayInputStream(outputStream.toByteArray());
        PdfReader pdfReader = new PdfReader(inStream);

        assertEquals(getTextFromPage(pdfReader, 1),
                "1984-01-01 12:00:00\n" +
                        "Title1 Title2\n" +
                        "Row: 1\n" +
                        "Place: 1");

    }

    @Test
    public void submitOrderNotExist() {
        when(orderRepository.findById(0L)).thenReturn(Optional.empty());
        assertThrows(OrderNotExist.class, () -> {
            orderManager.submit(0L);
        });

        when(orderRepository.findById(1L)).thenReturn(Optional.of(new Order().setActive(true)));
        assertThrows(OrderNotExist.class, () -> {
            orderManager.submit(1L);
        });
    }


}



