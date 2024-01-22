package com.example.newproject.repositories;

import com.example.newproject.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
    boolean existsByTicketId(String ticketId);
}

