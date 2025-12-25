package com.DS1.eventticketing.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "venues")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Venue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Venue name is required")
    private String name;

    @NotBlank(message = "Address is required")
    private String address;

    @Positive(message = "Capacity must be greater than 0")
    private int capacity;

    @OneToMany(mappedBy = "venue", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"venue"})
    @ToString.Exclude
    private List<Event> events;
}