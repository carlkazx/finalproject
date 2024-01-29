package com.example.newproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
public class Task {


    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Getter
    private String ticketId; // New field for TICKETID from Google Sheets


    @Setter
    @Getter
    private String description;

    @Setter
    @Getter
    private LocalDateTime dueDate;


    @Setter
    @Getter
    private String title;

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    private boolean isCompleted = false;

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    private boolean isApproved = false;

    @Setter @Getter
    private String status;


    @Setter @Getter
    private String name; // Corresponds to the 'Name' column in Google Sheets

    @Setter @Getter
    private String staffId; // Corresponds to the 'Staff ID' column in Google Sheets

    @Setter @Getter
    private String details; // Corresponds to the 'Details' column in Google Sheets

    @Setter @Getter
    private String timestamp; // Corresponds to the 'Details' column in Google Sheets
    public void updateFromOtherTask(Task other) {
        this.setTitle(other.getTitle());
        this.setDescription(other.getDescription());
        this.setDueDate(other.getDueDate());
        this.setTicketId(other.getTicketId());
        this.setName(other.getName());
        this.setStaffId(other.getStaffId());
        this.setStatus(other.getStatus());
        // Update other properties as necessary
    }

    @Setter
    @Getter
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assigned_user_id")
    private User assignedTo;



    // Constructors, getters and setters
}
