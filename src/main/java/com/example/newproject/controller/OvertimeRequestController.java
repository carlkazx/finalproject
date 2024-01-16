package com.example.newproject.controller;

import com.example.newproject.entity.OvertimeRequest;
import com.example.newproject.services.OvertimeRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/overtime")
public class OvertimeRequestController {

    @Autowired
    private OvertimeRequestService overtimeRequestService;
    private static final Logger log = LoggerFactory.getLogger(OvertimeRequestController.class);

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<List<OvertimeRequest>> listOvertimeRequests() {
        return ResponseEntity.ok(overtimeRequestService.getAllOvertimeRequests());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<OvertimeRequest> createOvertimeRequest(@RequestBody OvertimeRequest overtimeRequest) {
        return ResponseEntity.ok(overtimeRequestService.saveOvertimeRequest(overtimeRequest));
    }

    @PostMapping("/approve/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> approveOvertimeRequest(@PathVariable Long id) {
        log.info("Approving overtime request with ID: " + id);
        overtimeRequestService.approveOvertimeRequest(id);
        return ResponseEntity.ok().build();
    }


    // Additional methods...
}
