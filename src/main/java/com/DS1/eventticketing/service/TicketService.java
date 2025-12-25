package com.DS1.eventticketing.service;

import com.DS1.eventticketing.model.Event;
import com.DS1.eventticketing.model.Ticket;
import com.DS1.eventticketing.model.User;
import com.DS1.eventticketing.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private EventService eventService;

    @Autowired
    private UserService userService;

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public List<Ticket> getTicketsByUser(Long userId) {
        return ticketRepository.findByUserId(userId);
    }

    public List<Ticket> getTicketsByEvent(Long eventId) {
        return ticketRepository.findByEventId(eventId);
    }

    public Optional<Ticket> getTicketById(Long id) {
        return ticketRepository.findById(id);
    }

    public Ticket createTicket(Long userId, Long eventId, Integer quantity) {
        // Validate user exists
        Optional<User> userOpt = userService.getUserById(userId);
        if (!userOpt.isPresent()) {
            throw new RuntimeException("User not found with id: " + userId);
        }

        // Validate event exists
        Optional<Event> eventOpt = eventService.getEventById(eventId);
        if (!eventOpt.isPresent()) {
            throw new RuntimeException("Event not found with id: " + eventId);
        }

        Event event = eventOpt.get();

        // Validate event is in future
        if (event.getDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Cannot book tickets for past events");
        }

        // Check venue capacity
        Long ticketsSold = ticketRepository.countByEventId(eventId);
        int availableTickets = event.getVenue().getCapacity() - ticketsSold.intValue();

        if (quantity > availableTickets) {
            throw new RuntimeException(
                    String.format("Only %d tickets available. Requested: %d", availableTickets, quantity));
        }

        // Create ticket
        Ticket ticket = new Ticket();
        ticket.setQuantity(quantity);
        ticket.setUser(userOpt.get());
        ticket.setEvent(event);

        return ticketRepository.save(ticket);
    }

    public void deleteTicket(Long id) {
        if (!ticketRepository.existsById(id)) {
            throw new RuntimeException("Ticket not found with id: " + id);
        }
        ticketRepository.deleteById(id);
    }
}