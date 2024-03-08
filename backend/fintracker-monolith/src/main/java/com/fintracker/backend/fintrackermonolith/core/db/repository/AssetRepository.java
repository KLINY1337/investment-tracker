package com.fintracker.backend.fintrackermonolith.core.db.repository;

import com.fintracker.backend.fintrackermonolith.core.db.entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {
}