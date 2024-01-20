package com.example.newproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
public class OvertimeRequest {

    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Setter
    @Getter
    private LocalDateTime startTime;

    @Setter
    @Getter
    private LocalDateTime endTime;

    @Setter
    @Getter
    private String reason;

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    private boolean isApproved = false;

    @Getter
    @Setter
    private String approvalStatus = "pending"; // Default to "pending"


    // Constructors, Getters, Setters
}
