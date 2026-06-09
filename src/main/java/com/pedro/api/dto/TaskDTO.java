package com.pedro.api.dto;

import com.pedro.api.model.Task;
import com.pedro.api.model.enums.TaskStatus;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

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

        // NOVOS CAMPOS PARA AS TAGS
        private Set<Long> tagIds = new HashSet<>();
        private Set<String> tagNames = new HashSet<>();

        public TaskDTO() {}

        public TaskDTO(Task task) {
                this.id = task.getId();
                this.title = task.getTitle();
                this.description = task.getDescription();
                this.status = task.getStatus();
                this.userId = task.getUser().getId();
                this.createdAt = task.getCreatedAt();
                this.updatedAt = task.getUpdatedAt();

                if (task.getCategory() != null) {
                        this.categoryId = task.getCategory().getId();
                        this.categoryName = task.getCategory().getName();
                }

                // POPULAR AS TAGS NA RESPOSTA DA API
                if (task.getTags() != null) {
                        task.getTags().forEach(tag -> {
                                this.tagIds.add(tag.getId());
                                this.tagNames.add(tag.getName());
                        });
                }
        }

        public Long getId() { return id; }
        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public TaskStatus getStatus() { return status; }
        public Long getUserId() { return userId; }
        public Instant getCreatedAt() { return createdAt; }
        public Instant getUpdatedAt() { return updatedAt; }
        public Long getCategoryId() { return categoryId; }
        public String getCategoryName() { return categoryName; }

        // GETTERS PARA OS NOVOS CAMPOS
        public Set<Long> getTagIds() { return tagIds; }
        public Set<String> getTagNames() { return tagNames; }
}