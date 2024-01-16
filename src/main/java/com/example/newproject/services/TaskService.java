package com.example.newproject.services;

import com.example.newproject.entity.Task;
import com.example.newproject.repositories.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;
    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    @Transactional(readOnly = true)
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Transactional
    public Task saveTask(Task task) {
        logger.info("Saving task: {}", task.getTitle());
        return taskRepository.save(task);
    }

    @Transactional(readOnly = true)
    public Task findById(Long id) {
        return taskRepository.findById(id).orElse(null);
    }

    public void completeTask(Long id) {
        logger.info("Marking task as completed. Task ID: {}", id);
        Task task = findById(id);
        if (task != null && !task.isCompleted()) {
            task.setCompleted(true);
            taskRepository.save(task);
        } else {
            logger.warn("Task completion requested for non-existing or already completed task. Task ID: {}", id);
        }
    }

    @Transactional
    public void requestApproval(Long id) {
        logger.info("Task Approval. Task ID: {}",id);
        Task task = findById(id);
        if (task != null && task.isCompleted() && !task.isApproved()) {
            // Here, set a flag for requesting approval
            taskRepository.save(task);
        } else {
            logger.warn("Task can not be approved");
        }
    }

    @Transactional
    public void approveTask(Long id) {
        Task task = findById(id);
        logger.info("Task is Approved");
        if (task != null && task.isCompleted() && !task.isApproved()) {
            task.setApproved(true);
            taskRepository.save(task);
        } else {
            logger.warn("Task is approved");
        }
    }
    // Additional methods...
}