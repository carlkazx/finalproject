package com.example.newproject.services;

import com.example.newproject.entity.Task;
import com.example.newproject.entity.User;
import com.example.newproject.repositories.TaskRepository;
import com.example.newproject.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    @Transactional(readOnly = true)
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Transactional
    public Task saveOrUpdateTask(Task task) {
        if (task.getId() != null) {
            Optional<Task> existingTaskOpt = taskRepository.findById(task.getId());
            if (existingTaskOpt.isPresent()) {
                Task existingTask = existingTaskOpt.get();
                logger.info("Updating existing task with ID: {}", task.getId());
                existingTask.updateFromOtherTask(task);
                return taskRepository.save(existingTask);
            } else {
                logger.warn("Task with ID: {} not found for update", task.getId());
                return null;
            }
        } else {
            logger.info("Creating new task");
            return taskRepository.save(task);
        }
    }

    public Task updateStatus(Long taskId, String status) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));
        task.setStatus(status); // Assuming Task has a setStatus method
        return taskRepository.save(task);
    }


    public void completeTask(Long id) {
        taskRepository.findById(id).ifPresent(task -> {
            if (!task.isCompleted()) {
                task.setCompleted(true);
                taskRepository.save(task);
            } else {
                logger.warn("Task completion requested for non-existing or already completed task. Task ID: {}", id);
            }
        });
    }


    @Transactional
    public void requestApproval(Long id) {
        logger.info("Task Approval. Task ID: {}", id);
        taskRepository.findById(id).ifPresent(task -> {
            if (task.isApproved()) {
                task.setApproved(true);
                taskRepository.save(task);
            } else {
                logger.warn("Task can't be approved. Task ID: {}", id);
            }
        });
    }

    @Transactional
    public void revertTask(Long id) {
        taskRepository.findById(id).ifPresent(task -> {
            if (task != null && task.isApproved()) {
                task.setCompleted(false);
                task.setApproved(false);
                taskRepository.save(task);
                logger.info("Reverted task to incomplete and unapproved state. Task ID: {}", id);
            } else {
                logger.warn("Task can't be Reverted. Task ID: {}", id);
            }
        });
    }

    @Transactional
    public Task createTaskFromGoogleSheet(String ticketId, String name, String staffId, String description, String timestamp) {
        Task task = new Task();
        task.setTicketId(ticketId);
        task.setName(name); // Assuming you have a 'name' field in your Task entity
        task.setStaffId(staffId); // Assuming you have a 'staffId' field in your Task entity
        task.setDescription(description); // Assuming you have a 'details' field in your Task entity
        task.setTimestamp(timestamp);
        // Set other default values or handle them accordingly

        return taskRepository.save(task);
    }

    public boolean existsByTicketId(String ticketId) {
        return taskRepository.existsByTicketId(ticketId);
    }

    @Transactional
    public Task createAndAssignTask(Task task, Integer userId) {
        User assignedUser = userRepository.findById(userId).orElse(null);
        if (assignedUser == null) {
            logger.warn("Cannot assign task. User not found with ID: {}", userId);
            return null; // Or handle differently
        }

        task.setAssignedTo(assignedUser);
        logger.info("Creating and assigning task to user ID: {}", userId);
        return taskRepository.save(task);
    }

    @Transactional
    public Task assignUser(Long taskId, Integer userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with ID: " + taskId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

        task.setAssignedTo(user);
        logger.info("Task ID {} assigned to User ID {}", taskId, userId);
        return taskRepository.save(task);
    }

    // In TaskService.java




    @Transactional(readOnly = true)
    public Optional<Task> findById(Long id) {
        return taskRepository.findById(id);
    }

}
