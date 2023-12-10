package com.example.airlineticket.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Agency {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String phoneNumber;
    private String address;

    @OneToOne
    private User user;
    @OneToMany
    private List<Customer> customers;
}
