package com.pedro.api.dto;

import com.pedro.api.model.Task;
import com.pedro.api.model.enums.TaskStatus;
import java.time.Instant;

public class TaskDTO {
        private Long id;
        private String title;
        private String description;
        private TaskStatus status;
        private Long userId;
        private Instant createdAt;
        private Instant updatedAt;
        private Long categoryId;
        private String categoryName;

        public TaskDTO() {}

        public TaskDTO(Task task) {
                this.id = task.getId();
                this.title = task.getTitle();
                this.description = task.getDescription();
                this.status = task.getStatus();
                this.userId = task.getUser().getId();
                this.createdAt = task.getCreatedAt();
                this.updatedAt = task.getUpdatedAt();

                // Verificação de segurança para evitar NullPointerException
                if (task.getCategory() != null) {
                        this.categoryId = task.getCategory().getId();
                        this.categoryName = task.getCategory().getName();
                }
        }

        // Getters para todos os campos (incluindo categoryId e categoryName)
        public Long getId() { return id; }
        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public TaskStatus getStatus() { return status; }
        public Long getUserId() { return userId; }
        public Instant getCreatedAt() { return createdAt; }
        public Instant getUpdatedAt() { return updatedAt; }
        public Long getCategoryId() { return categoryId; }
        public String getCategoryName() { return categoryName; }
}