package com.fintracker.backend.auth_microservice.authentication.repository;

import com.fintracker.backend.auth_microservice.authentication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select (count(u) > 0) from User u where u.username = ?1 or u.email = ?2")
    boolean existsByUsernameOrEmail(String username, String email);
    @Query("select u from User u where u.username = ?1 or u.email = ?1")
    Optional<User> findUserByUsernameOrEmail(String username);
}