package com.fintracker.backend.fintrackermonolith.other.repository;

import com.fintracker.backend.fintrackermonolith.other.entity.SupportRequestEntity;
import com.fintracker.backend.fintrackermonolith.other.enumeration.SupportRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupportRequestRepository extends JpaRepository<SupportRequestEntity, Integer> {

    SupportRequestEntity findFirstByStatusOrderByCreatedAtAsc(SupportRequestStatus supportRequestStatus);

}
