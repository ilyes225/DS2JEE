package com.DS1.eventticketing.service;

import com.DS1.eventticketing.model.Event;
import com.DS1.eventticketing.model.Venue;
import com.DS1.eventticketing.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EventService {
    @Autowired
    private EventRepository eventRepository;
    
    @Autowired
    private VenueService venueService;

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }
    
    public List<Event> getUpcomingEvents() {
        return eventRepository.findByDateAfter(LocalDate.now());
    }

    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
    }

    public Event createEvent(Event event) {
        // Validate venue exists
        if (event.getVenue() == null || event.getVenue().getId() == null) {
            throw new RuntimeException("Venue is required");
        }
        
        Optional<Venue> venueOpt = venueService.getVenueById(event.getVenue().getId());
        if (!venueOpt.isPresent()) {
            throw new RuntimeException("Venue not found with id: " + event.getVenue().getId());
        }
        
        event.setVenue(venueOpt.get());
        
        // Validate date is in future
        if (event.getDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Event date must be in the future");
        }
        
        return eventRepository.save(event);
    }

    public Event updateEvent(Long id, Event updatedEvent) {
        Optional<Event> eventOpt = eventRepository.findById(id);
        if (!eventOpt.isPresent()) {
            throw new RuntimeException("Event not found with id: " + id);
        }
        
        Event event = eventOpt.get();
        
        // Update venue if provided
        if (updatedEvent.getVenue() != null && updatedEvent.getVenue().getId() != null) {
            Optional<Venue> venueOpt = venueService.getVenueById(updatedEvent.getVenue().getId());
            if (!venueOpt.isPresent()) {
                throw new RuntimeException("Venue not found");
            }
            event.setVenue(venueOpt.get());
        }
        
        event.setName(updatedEvent.getName());
        event.setDate(updatedEvent.getDate());
        event.setPrice(updatedEvent.getPrice());
        
        return eventRepository.save(event);
    }

    public void deleteEvent(Long id) {
        Optional<Event> eventOpt = eventRepository.findById(id);
        if (!eventOpt.isPresent()) {
            throw new RuntimeException("Event not found with id: " + id);
        }
        
        Event event = eventOpt.get();
        
        // Check if event has tickets
        if (event.getTickets() != null && !event.getTickets().isEmpty()) {
            throw new RuntimeException("Cannot delete event with purchased tickets");
        }
        
        eventRepository.deleteById(id);
    }
}