package com.pedro.api.projections;

public interface TaskProjection {
    Long getId();
    String getTitle();
    String getDescription();
    String getStatus();
}