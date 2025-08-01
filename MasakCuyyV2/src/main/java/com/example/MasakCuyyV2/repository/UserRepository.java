package com.example.MasakCuyyV2.repository;

import com.example.MasakCuyyV2.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username); 
    Optional<User> findByEmail(String email); 
}
