package com.example.newproject.controller;

import com.example.newproject.dtos.AssignUserDTO;
import com.example.newproject.dtos.TaskDto;
import com.example.newproject.entity.Task;
import com.example.newproject.entity.User;
import com.example.newproject.services.TaskService;
import com.example.newproject.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<Task>> getAllTasks() {
        logger.info("Getting all tasks");
        List<Task> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Optional<Task> task = taskService.findById(id);
        if (task.isPresent()) {
            return ResponseEntity.ok(task.get());
        } else {
            logger.error("Task with id {} not found.", id);
            return ResponseEntity.notFound().build();
        }
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> updateTask(@PathVariable Long id, @RequestBody TaskDto taskDto) {
        Optional<Task> existingTaskOptional = taskService.findById(id);

        if (!existingTaskOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Task existingTask = existingTaskOptional.get();

        // Map fields from the DTO to the existing task entity
        existingTask.setTitle(taskDto.getTitle());
        existingTask.setDescription(taskDto.getDescription());
        existingTask.setDueDate(taskDto.getDueDate());
        existingTask.setStatus(taskDto.getStatus());
        // ... other property mappings ...

        // Save the updated task
        Task updatedTask = taskService.saveOrUpdateTask(existingTask);
        return ResponseEntity.ok(updatedTask);
    }


    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        logger.info("Creating new task with title: {}", task.getTitle());
        return ResponseEntity.ok(taskService.saveOrUpdateTask(task));
    }

    @PostMapping("/complete/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<?> completeTask(@PathVariable Long id) {
        taskService.completeTask(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{taskId}/status")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<Task> updateTaskStatus(@PathVariable Long taskId, @RequestBody String status) {
        try {
            Task updatedTask = taskService.updateStatus(taskId, status);
            return new ResponseEntity<>(updatedTask, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            logger.error("Task not found", e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error updating task status", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
        taskService.requestApproval(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/assign")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Task> createAndAssignTask(@RequestBody TaskDto taskDto) {
        User assignedUser = userService.findById(taskDto.getAssignedUserId());
        if (assignedUser == null) {
            logger.warn("Assigned user not found. User ID: {}", taskDto.getAssignedUserId());
            return ResponseEntity.badRequest().build();
        }

        Task task = new Task();
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setDueDate(taskDto.getDueDate());
        task.setAssignedTo(assignedUser);
        task.setCompleted(false);  // The task is not yet completed
        task.setApproved(false);   // The task is not yet approved

        Task savedTask = taskService.saveOrUpdateTask(task);
        logger.info("Admin created and assigned a task to user ID: {}", assignedUser.getId());
        return ResponseEntity.ok(savedTask);
    }

    @PostMapping("/{taskId}/assign")
    public ResponseEntity<Task> assignUserToTask(@PathVariable Long taskId, @RequestBody AssignUserDTO assignUserDTO) {
        Task updatedTask = taskService.assignUser(taskId, assignUserDTO.getUserId());
        return ResponseEntity.ok(updatedTask);
    }



    @PostMapping("/revert/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> revertTask(@PathVariable Long id) {
        taskService.revertTask(id);
        return ResponseEntity.ok().build();
    }


    // Additional endpoints...
}
