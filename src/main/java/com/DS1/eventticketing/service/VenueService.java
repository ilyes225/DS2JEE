package com.DS1.eventticketing.service;

import com.DS1.eventticketing.model.Venue;
import com.DS1.eventticketing.repository.VenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VenueService {
    @Autowired
    private VenueRepository venueRepository;

    public List<Venue> getAllVenues() {
        return venueRepository.findAll();
    }

    public Optional<Venue> getVenueById(Long id) {
        return venueRepository.findById(id);
    }

    public Venue createVenue(Venue venue) {
        return venueRepository.save(venue);
    }

    public Venue updateVenue(Long id, Venue newVenueData) {
        Optional<Venue> venueOpt = venueRepository.findById(id);
        if (!venueOpt.isPresent()) {
            throw new RuntimeException("Venue not found with id: " + id);
        }
        
        Venue venue = venueOpt.get();
        
        venue.setName(newVenueData.getName());
        venue.setAddress(newVenueData.getAddress());
        venue.setCapacity(newVenueData.getCapacity());
        
        return venueRepository.save(venue);
    }

    public void deleteVenue(Long id) {
        Optional<Venue> venueOpt = venueRepository.findById(id);
        if (!venueOpt.isPresent()) {
            throw new RuntimeException("Venue not found with id: " + id);
        }
        
        Venue venue = venueOpt.get();
        
        // Check if venue has events
        if (venue.getEvents() != null && !venue.getEvents().isEmpty()) {
            throw new RuntimeException("Cannot delete venue with scheduled events");
        }
        
        venueRepository.deleteById(id);
    }
}