package com.example.airlineticket.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
public class ConfirmationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String email;
    private String password;
    private String token;

    public ConfirmationToken(String email, String password, String token) {
        this.email = email;
        this.password = password;
        this.token = token;
    }

    public ConfirmationToken() {

    }

}
