package com.fintracker.backend.auth_microservice.repository;

import com.fintracker.backend.auth_microservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}