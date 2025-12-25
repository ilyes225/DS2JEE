package com.DS1.eventticketing.repository;

import com.DS1.eventticketing.model.Venue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VenueRepository extends JpaRepository<Venue, Long> {
}