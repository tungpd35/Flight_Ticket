package com.example.airlineticket.models;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
public class DataFlight {
    private String flight_date;
    private String flight_status;
    private Departure departure;
    private Arrival arrival;
    private Flight flight;
}
