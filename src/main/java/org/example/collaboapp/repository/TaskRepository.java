package org.example.collaboapp.repository;

import org.example.collaboapp.model.Task;
import org.example.collaboapp.model.utils.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("SELECT t FROM Task t WHERE t.projectId = :projectId AND (:status IS NULL OR t.status = :status)")
    Page<Task> findAllByProjectId(int projectId, Status status, Pageable pageable);
}
