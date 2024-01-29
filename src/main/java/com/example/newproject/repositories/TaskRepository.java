package com.example.newproject.repositories;

import com.example.newproject.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    boolean existsByTicketId(String ticketId);


}

