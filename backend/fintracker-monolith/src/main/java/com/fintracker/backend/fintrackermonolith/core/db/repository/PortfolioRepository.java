package com.fintracker.backend.fintrackermonolith.core.db.repository;

import com.fintracker.backend.fintrackermonolith.auth_server.db.entity.User;
import com.fintracker.backend.fintrackermonolith.core.db.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    @Query("select count(p) from Portfolio p where p.user = ?1")
    long countByUser(User user);
    @Query("select p.id from Portfolio p where p.user = ?1")
    List<Long> findAllIdsByUser(User user);

    @Query("select (count(p) > 0) from Portfolio p where p.user = ?1 and p.name = ?2")
    boolean existsByUserAndName(User user, String name);
}