package com.example.newproject.controller;

import com.example.newproject.entity.Task;
import com.example.newproject.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        logger.info("Creating new task with title: {}", task.getTitle());
        return ResponseEntity.ok(taskService.saveTask(task));
    }



    @PostMapping("/complete/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<?> completeTask(@PathVariable Long id) {
        taskService.completeTask(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/requestApproval/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<?> requestApproval(@PathVariable Long id) {
        taskService.requestApproval(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/approve/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> approveTask(@PathVariable Long id) {
        taskService.approveTask(id);
        return ResponseEntity.ok().build();
    }
    // Additional endpoints...
}