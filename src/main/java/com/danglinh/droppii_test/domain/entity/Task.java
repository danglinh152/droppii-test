package com.danglinh.droppii_test.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;


import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Task")
public class Task implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long id;

    @Column(name = "title", nullable = false)
    @NotBlank(message = "title không được để trống!")
    private String title;

    @Column(name = "description", columnDefinition = "LONGTEXT", nullable = false)
    @NotBlank(message = "description không được để trống!")
    private String description;

    @Column(name = "due_date", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss a", timezone = "GMT+7")
    private Instant dueDate;

    @Column(name = "priority", nullable = false)
    private int priority;

    @Column(name = "completed", nullable = false)
    private boolean completed = false;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "task_dependencies",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "dependency_id")
    )
    private Set<Task> dependencies = new HashSet<>();


    public Task() {
    }

    public Task(Long id, String title, String description, Instant dueDate, int priority, boolean completed, Set<Task> dependencies) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
        this.completed = completed;
        this.dependencies = dependencies;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotBlank(message = "title không được để trống!") String getTitle() {
        return title;
    }

    public void setTitle(@NotBlank(message = "title không được để trống!") String title) {
        this.title = title;
    }

    public @NotBlank(message = "description không được để trống!") String getDescription() {
        return description;
    }

    public void setDescription(@NotBlank(message = "description không được để trống!") String description) {
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
