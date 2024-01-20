package com.example.newproject.services;

import com.example.newproject.entity.OvertimeRequest;
import com.example.newproject.repositories.OvertimeRequestRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




@Service
public class OvertimeRequestService {

    @Autowired
    private OvertimeRequestRepository overtimeRequestRepository;

    private static final Logger log = LoggerFactory.getLogger(OvertimeRequestService.class);

    public OvertimeRequest saveOvertimeRequest(OvertimeRequest overtimeRequest) {
        return overtimeRequestRepository.save(overtimeRequest);
    }

    public List<OvertimeRequest> getAllOvertimeRequests() {
        return overtimeRequestRepository.findAll();
    }

    public OvertimeRequest findById(Long id) {
        return overtimeRequestRepository.findById(id).orElse(null);
    }


    @Transactional
    public void approveOvertimeRequest(Long id) {
        OvertimeRequest overtimeRequest = findById(id);
        if (overtimeRequest != null) {
            log.info("Found overtime request. Current approval status: " + overtimeRequest.isApproved());
            overtimeRequest.setApproved(true);
            overtimeRequestRepository.save(overtimeRequest);
            log.info("Updated approval status: " + overtimeRequest.isApproved());
        } else {
            log.warn("Overtime request not found for ID: " + id);
        }
    }

    public OvertimeRequest getOvertimeRequestById(Long id) {
        return overtimeRequestRepository.findById(id).orElse(null);
    }

    // Additional methods like update, delete, findByUserId, etc.
}