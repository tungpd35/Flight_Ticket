package com.example.airlineticket.repositories;

import com.example.airlineticket.models.Price;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceRepository extends JpaRepository<Price, Long> {
    Price getPriceByDepartureAndArrival(String departure, String Arrival);
}
