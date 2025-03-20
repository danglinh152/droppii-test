package com.danglinh.droppii_test.domain.DTO.request;

import com.danglinh.droppii_test.domain.entity.Task;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;


public class UpdatedTask {
    private String title;
    private String description;
    private Instant dueDate;
    private int priority;
    private boolean completed;
    private Set<Task> dependencies = new HashSet<>();

    public UpdatedTask() {
    }

    public UpdatedTask(String title, String description, Instant dueDate, int priority, boolean completed, Set<Task> dependencies) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
        this.completed = completed;
        this.dependencies = dependencies;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getDueDate() {
        return dueDate;
    }

    public void setDueDate(Instant dueDate) {
        this.dueDate = dueDate;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Set<Task> getDependencies() {
        return dependencies;
    }

    public void setDependencies(Set<Task> dependencies) {
        this.dependencies = dependencies;
    }
}
