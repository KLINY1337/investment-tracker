package com.fintracker.backend.fintrackermonolith.auth_server.db.repository;

import com.fintracker.backend.fintrackermonolith.auth_server.db.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Transactional
    @Modifying
    @Query("update User u set u.username = ?1, u.email = ?2, u.password = ?3 where u.id = ?4")
    int updateUsernameAndEmailAndPasswordById(String username, String email, String password, Long id);
    @Query("select (count(u) > 0) from User u where u.username = ?1 or u.email = ?2")
    boolean existsByUsernameOrEmail(String username, String email);
    @Query("select u from User u where u.username = ?1 or u.email = ?1")
    Optional<User> findUserByUsernameOrEmail(String username);
}