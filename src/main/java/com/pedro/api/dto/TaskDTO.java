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

        public TaskDTO() {
        }

        // Construtor que transforma a Entidade em DTO (igual ao seu UserDTO)
        public TaskDTO(Task task) {
                this.id = task.getId();
                this.title = task.getTitle();
                this.description = task.getDescription();
                this.status = task.getStatus();
                this.userId = task.getUser().getId(); // Pegamos apenas o ID do dono
                this.createdAt = task.getCreatedAt();
                this.updatedAt = task.getUpdatedAt();
        }

        public Long getId() {
                return id;
        }

        public String getTitle() {
                return title;
        }

        public String getDescription() {
                return description;
        }

        public TaskStatus getStatus() {
                return status;
        }

        public Long getUserId() {
                return userId;
        }

        public Instant getCreatedAt() {
                return createdAt;
        }

        public Instant getUpdatedAt() {
                return updatedAt;
        }
}