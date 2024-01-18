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
@CrossOrigin(origins = "http://localhost:5137")
public class OvertimeRequestController {

    @Autowired
    private OvertimeRequestService overtimeRequestService;
    private static final Logger logger = LoggerFactory.getLogger(OvertimeRequestController.class);

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<List<OvertimeRequest>> listOvertimeRequests() {
        logger.info("Overtime List");
        return ResponseEntity.ok(overtimeRequestService.getAllOvertimeRequests());
    }

    @PostMapping("/overtime-request")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<OvertimeRequest> createOvertimeRequest(@RequestBody OvertimeRequest overtimeRequest) {
        logger.info("Overtime Requested");
        return ResponseEntity.ok(overtimeRequestService.saveOvertimeRequest(overtimeRequest));
    }

    @PostMapping("/approve/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> approveOvertimeRequest(@PathVariable Long id) {
        logger.info("Approving overtime request with ID: " + id);
        overtimeRequestService.approveOvertimeRequest(id);
        return ResponseEntity.ok().build();
    }


    // Additional methods...
}
