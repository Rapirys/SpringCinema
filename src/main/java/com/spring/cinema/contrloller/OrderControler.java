package com.spring.cinema.contrloller;


import com.spring.cinema.entities.Order;
import com.spring.cinema.entities.Session;
import com.spring.cinema.entities.Ticket;
import com.spring.cinema.model.repository.OrderRepository;
import com.spring.cinema.model.repository.SessionRepository;
import com.spring.cinema.model.repository.TicketRepository;
import com.spring.cinema.model.service.Hall.Place;
import com.spring.cinema.model.service.OrderManager;
import com.spring.cinema.model.service.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@PreAuthorize("hasAuthority('USER')")
public class OrderControler {

    @Autowired
    Validator validator;
    @Autowired
    OrderManager orderManager;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    SessionRepository sessionRepository;
    @Autowired
    TicketRepository ticketRepository;

    @GetMapping("/place")
    public String place(Model model, HttpServletRequest request
            , @RequestParam(name = "id") Long id) {
        Optional<Session> o = sessionRepository.findById(id);
        Session session;
        if (o.isPresent())
            session = o.get();
        else return "redirect:/main";
        ArrayList<ArrayList<Place>> topology = orderManager.getHall(session);
        model.addAttribute("mySession", session);
        model.addAttribute("topology", topology);
        return "place";
    }

    @PostMapping(value = "/order")
    public ResponseEntity<?> order(@RequestParam(name = "data") List<String> data,
                                   @RequestParam(name = "session_id") Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long orderId = orderManager.book(data, id, auth.getName());
        if (orderId != null)
            return ResponseEntity.ok().body(orderId);
        else return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(-1);
    }

    @GetMapping("/order")
    public String payment(@RequestParam(name = "id") Long id,
                          Model model) {
        Optional<Order> o = orderRepository.findById(id);
        Order order;
        if (o.isPresent())
            order = o.get();
        else return "redirect:/";
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!order.getUser().getUsername().equals(auth.getName()))
            return "redirect:/";
        if (order.isActive())
            return "orderOld";

        order.setTickets(ticketRepository.findTicketsByOrder(order));
        model.addAttribute("order", order);
        return "order";
    }

    @PostMapping("/order/pay")
    public String pay(@RequestParam(name = "cvv") String cvv,
                      @RequestParam(name = "cardName") String holder,
                      @RequestParam(name = "cardNumber") String cardNumber,
                      @RequestParam(name = "dateM", required = false) Integer dateM,
                      @RequestParam(name = "dateY", required = false) Integer dateY,
                      @RequestParam("order_id") Long order_id,
                      RedirectAttributes redirectAttributes) {
        if (!validator.validCard(cardNumber, cvv, dateM, dateY, holder)) {
            ArrayList<String> messages = new ArrayList<>();
            messages.add("Card_data_error");
            redirectAttributes.addFlashAttribute("message", messages);
            redirectAttributes.addAttribute("id", order_id);
            return "redirect:/order";
        }
        Order order;
        try {
            order = orderManager.submit(order_id);
        } catch (Exception e) {
            return "orderOld";
        }
        redirectAttributes.addAttribute("order", order);
        return "redirect:/download";
    }

    @GetMapping("/download")
    public String download(@RequestParam("order") Order order, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!order.getUser().getUsername().equals(auth.getName()))
            return "redirect:/";
        model.addAttribute("id", order.getOrder_id());
        return "download";
    }

    @GetMapping("/downloadOrder")
    public void getFile(@RequestParam("id") Long id, HttpServletResponse response) {
        Optional<Order> o = orderRepository.findById(id);
        Order order;
        if (o.isPresent())
            order = o.get();
        else return;
        if (!order.isActive())
            return;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!order.getUser().getUsername().equals(auth.getName()))
            return;
        response.setHeader("Content-disposition", "attachment;filename=" + "tickets_" + order.getOrder_id() + ".pdf");
        response.setContentType("application/pdf");
        try {
            orderManager.getPdf(order, response.getOutputStream());
            response.getOutputStream().flush();
        } catch (Exception e) {
            throw new RuntimeException("IOError writing file to output stream");
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/ticket")
    public String getFile(@RequestParam("id") Long id,
                          @RequestParam("salt") Long salt, Model model) {
        Optional<Ticket> ticket = ticketRepository.findById(id);
        if (ticket.isPresent() && ticket.get().getSalt().equals(salt)) {
            model.addAttribute("ticket", ticket.get());
            model.addAttribute("exist", true);
        } else model.addAttribute("exist", false);
        return "exist";
    }

}
