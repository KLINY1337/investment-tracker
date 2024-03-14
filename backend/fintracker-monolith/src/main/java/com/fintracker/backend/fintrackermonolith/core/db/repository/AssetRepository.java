package com.fintracker.backend.fintrackermonolith.core.db.repository;

import com.fintracker.backend.fintrackermonolith.core.db.entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {
    @Query("select (count(a) > 0) from Asset a where a.symbol = ?1 and a.assetTypeName = ?2")
    boolean existsBySymbolAndAssetTypeName(String symbol, String asset_type_name);
}