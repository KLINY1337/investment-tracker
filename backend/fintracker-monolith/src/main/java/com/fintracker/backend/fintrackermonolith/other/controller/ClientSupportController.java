package com.fintracker.backend.fintrackermonolith.other.controller;

import com.fintracker.backend.fintrackermonolith.other.controller.request.SupportRequest;
import com.fintracker.backend.fintrackermonolith.exchange_rate.controller.response.MainResponse;
import com.fintracker.backend.fintrackermonolith.other.service.support.SupportService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/support")
@RequiredArgsConstructor
public class ClientSupportController {

    private SupportService supportService;

    @Autowired
    public ClientSupportController (SupportService supportService){
        this.supportService = supportService;
    }

    @PostMapping
    public ResponseEntity<MainResponse<SupportRequest>> placeSupportRequest(@RequestBody SupportRequest supportRequest) {
        ResponseEntity<MainResponse<SupportRequest>> response = null;
        try{
            supportService.createSupportRequest(supportRequest);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(MainResponse.error(e.getMessage()));
        }finally{
            response  = ResponseEntity.ok(MainResponse.<SupportRequest>builder().data(supportRequest).success(true).message("200").build());
        }
        return response;

    }

}
