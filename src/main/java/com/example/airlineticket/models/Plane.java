package com.example.airlineticket.models;

import jakarta.persistence.*;

import java.time.Year;

@Entity
public class Plane {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String manufacturer;
    private int numberOfSeat;
    private Year mfgYear;
    private String status;

}
