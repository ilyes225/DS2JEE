package com.DS1.eventticketing.repository;

import com.DS1.eventticketing.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByDateAfter(LocalDate date);
    List<Event> findByVenueId(Long venueId);
}