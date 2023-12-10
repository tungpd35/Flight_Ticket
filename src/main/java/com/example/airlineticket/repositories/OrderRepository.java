package com.example.airlineticket.repositories;

import com.example.airlineticket.models.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

@Repository
public interface OrderRepository extends JpaRepository<OrderDetail, Long> {
    OrderDetail findByTxnRef(String txnRef);
}
