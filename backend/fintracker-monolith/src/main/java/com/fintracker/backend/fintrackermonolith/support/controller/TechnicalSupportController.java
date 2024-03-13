package com.fintracker.backend.fintrackermonolith.support.controller;


import com.fintracker.backend.fintrackermonolith.exchange_rate.controller.response.MainResponse;
import com.fintracker.backend.fintrackermonolith.support.entity.SupportRequestEntity;
import com.fintracker.backend.fintrackermonolith.support.service.support.SupportService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee/support")
@RequiredArgsConstructor
public class TechnicalSupportController {
    private SupportService supportService;

    @Autowired
    public TechnicalSupportController(SupportService supportService) {
        this.supportService = supportService;
    }

    @GetMapping()
    public ResponseEntity<MainResponse<Iterable<SupportRequestEntity>>> getAllSupportRequests (){
        return ResponseEntity.ok().body(MainResponse.success(supportService.getAllSupportRequests()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MainResponse<SupportRequestEntity>> getSupportRequestById (@PathVariable int id){
        SupportRequestEntity entity = supportService.getSupportRequestById(id);
       try{
           return ResponseEntity.ok().body(MainResponse.success(entity));
       }catch (Exception e){
           return ResponseEntity.badRequest().body(MainResponse.error(e.getMessage()));
       }
    }

    @GetMapping("/firstopened")
    public ResponseEntity<MainResponse<SupportRequestEntity>> getFirstOpenedSupportRequest () {
        SupportRequestEntity entity = supportService.getFirstOpenedSupportRequest();
        try {
            return ResponseEntity.ok().body(MainResponse.success(entity));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(MainResponse.error(e.getMessage()));

        }
    }

    @PatchMapping("/close/{id}")
    public ResponseEntity<MainResponse<SupportRequestEntity>> closeSupportRequestById (@PathVariable int id) {
        SupportRequestEntity entity = supportService.closeSupportRequestById(id);
        try {
            return ResponseEntity.ok().body(MainResponse.success(entity));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(MainResponse.error(e.getMessage()));

        }
    }
}
