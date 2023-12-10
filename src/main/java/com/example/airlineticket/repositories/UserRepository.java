package com.example.airlineticket.repositories;

import com.example.airlineticket.models.Role;
import com.example.airlineticket.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByEmail(String email);
    User findUserById(Long id);
    List<User> findAllByRole(Role role);
}
