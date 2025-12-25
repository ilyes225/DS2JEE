package com.DS1.eventticketing.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "At least one ticket must be booked")
    private Integer quantity;

    private LocalDateTime bookingDate = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull(message = "User is required")
    @JsonIgnoreProperties({"tickets"})
    private User user;

    @ManyToOne
    @JoinColumn(name = "event_id")
    @NotNull(message = "Event is required")
    @JsonIgnoreProperties({"tickets", "venue"})
    private Event event;
}