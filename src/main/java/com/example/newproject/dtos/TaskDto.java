package com.example.newproject.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class TaskDto {

    private String title;

    private String description;

    private String status;


    private LocalDateTime dueDate;


    private Integer assignedUserId;

}