package com.fintracker.backend.fintrackermonolith.service.support;

import com.fintracker.backend.fintrackermonolith.controller.request.SupportRequest;
import com.fintracker.backend.fintrackermonolith.entity.SupportRequestEntity;
import com.fintracker.backend.fintrackermonolith.enumeration.SupportRequestStatus;
import com.fintracker.backend.fintrackermonolith.exception.support.SupportRequestNotFoundException;
import com.fintracker.backend.fintrackermonolith.repository.SupportRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SupportService {

    private SupportRequestRepository supportRepository;

    @Autowired
    public SupportService(SupportRequestRepository supportRepository) {
        this.supportRepository = supportRepository;
    }

    public SupportRequestEntity createSupportRequest(SupportRequest supportRequest) {
        SupportRequestEntity supportRequestEntity = SupportRequestEntity.builder()
                .name(supportRequest.getName())
                .email(supportRequest.getEmail())
                .status(SupportRequestStatus.OPEN)
                .createdAt(new Date())
                .description(supportRequest.getDescription()).build();

        return supportRepository.save(supportRequestEntity);
    }

    public void updateSupportRequest(int id, SupportRequestStatus newStatus) {
        SupportRequestEntity supportRequestEntity = supportRepository.findById(id).orElseThrow(SupportRequestNotFoundException::new);
        supportRequestEntity.setStatus(SupportRequestStatus.CLOSED);
        supportRepository.save(supportRequestEntity);
    }

    public boolean closeSupportRequest(int id) {
        updateSupportRequest(id, SupportRequestStatus.CLOSED);
        return true;
    }

    public Iterable<SupportRequestEntity> getAllSupportRequests() {
        return supportRepository.findAll();
    }

    public SupportRequestEntity getSupportRequestById(int id) {
        return supportRepository.findById(id).orElseThrow(SupportRequestNotFoundException::new);
    }

    public SupportRequestEntity getFirstOpenedSupportRequest() {
        try{
            SupportRequestEntity supportRequestEntity =  supportRepository.findFirstByStatusOrderByCreatedAtAsc(SupportRequestStatus.OPEN);
            supportRequestEntity.setStatus(SupportRequestStatus.IN_PROGRESS);
            return supportRepository.save(supportRequestEntity);
        }catch(Exception e){
            return null;
        }
    }

    public SupportRequestEntity closeSupportRequestById(int id) {
        SupportRequestEntity supportRequestEntity = supportRepository.findById(id).orElseThrow(SupportRequestNotFoundException::new);
        supportRequestEntity.setStatus(SupportRequestStatus.CLOSED);
        return supportRepository.save(supportRequestEntity);
    }
}
