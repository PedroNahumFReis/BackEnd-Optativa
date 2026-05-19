package com.pedro.api.repository;

import com.pedro.api.model.Task;
import com.pedro.api.projections.TaskProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query(nativeQuery = true,
            value = """
                    SELECT DISTINCT t.id, t.title, t.description, t.status
                    FROM tb_task t
                    WHERE (:categoriesID IS NULL OR t.category_id IN (:categoriesID))
                      AND (LOWER(t.title) LIKE LOWER(CONCAT('%', :title, '%')))
                    """,
            countName = """
                    SELECT COUNT(*)
                    FROM (
                        SELECT DISTINCT t.id, t.title, t.description, t.status
                        FROM tb_task t
                        WHERE (:categoriesID IS NULL OR t.category_id IN (:categoriesID))
                          AND (LOWER(t.title) LIKE LOWER(CONCAT('%', :title, '%')))
                    ) as tb_result
                    """)
    Page<TaskProjection> searchTasks(List<Long> categoriesID, String title, Pageable pageable);
}