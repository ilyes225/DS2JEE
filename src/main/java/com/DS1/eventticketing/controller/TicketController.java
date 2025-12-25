package com.DS1.eventticketing.controller;

import com.DS1.eventticketing.model.Ticket;
import com.DS1.eventticketing.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    @Autowired
    private TicketService ticketService;

    @GetMapping
    public ResponseEntity<List<Ticket>> getAllTickets() {
        return ResponseEntity.ok(ticketService.getAllTickets());
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Ticket>> getTicketsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(ticketService.getTicketsByUser(userId));
    }
    
    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<Ticket>> getTicketsByEvent(@PathVariable Long eventId) {
        return ResponseEntity.ok(ticketService.getTicketsByEvent(eventId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ticket> getTicket(@PathVariable Long id) {
        return ticketService.getAllTickets().stream()
            .filter(t -> t.getId().equals(id))
            .findFirst()
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Ticket> createTicket(
            @RequestParam Long userId,
            @RequestParam Long eventId,
            @RequestParam Integer quantity) {
        try {
            Ticket ticket = ticketService.createTicket(userId, eventId, quantity);
            return ResponseEntity.status(HttpStatus.CREATED).body(ticket);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        try {
            ticketService.deleteTicket(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}