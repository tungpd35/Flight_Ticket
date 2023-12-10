package com.example.airlineticket.models;

import jakarta.persistence.*;

@Entity
public class Baggage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private Long weight;
    @OneToOne
    private Customer customer;
}
