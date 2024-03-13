package com.fintracker.backend.fintrackermonolith.gigachat.db.repositiory;

import com.fintracker.backend.fintrackermonolith.gigachat.db.entity.GigachatToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GigachatTokenRepository extends JpaRepository<GigachatToken, String> {

    @Query("SELECT t FROM GigachatToken t WHERE t.expires_at > CURRENT_TIMESTAMP")
    String getLastValidToken();
}
