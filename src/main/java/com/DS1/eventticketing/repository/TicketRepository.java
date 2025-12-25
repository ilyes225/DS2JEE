package com.DS1.eventticketing.repository;

import com.DS1.eventticketing.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByUserId(Long userId);
    List<Ticket> findByEventId(Long eventId);
    Long countByEventId(Long eventId);
}