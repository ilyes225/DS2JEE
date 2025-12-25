package com.DS1.eventticketing.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Event name is required")
    private String name;

    @NotNull(message = "Event date is required")
    @Future(message = "Event date must be in the future")
    private LocalDate date;

    @NotNull(message = "Ticket price is required")
    @Positive(message = "Price must be positive")
    private Double price;

    @ManyToOne
    @JoinColumn(name = "venue_id")
    @NotNull(message = "Venue is required")
    @JsonIgnoreProperties({ "events" })
    private Venue venue;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({ "event", "user" })
    @ToString.Exclude
    private List<Ticket> tickets;
}