package com.danglinh.droppii_test.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity // Khai báo rằng lớp Task là entity
@Table(name = "Task") // Lớp Task thuộc table Task trong cơ sở dữ liệu
public class Task {

    @Id // Đánh dấu thuộc tính id là Id của bảng Task
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Tự động phát sinh giá trị cho thuộc tính id với các giá trị tăng dần
    @Column(name = "task_id") // Thuộc tính id thuộc cột task_id trong cơ sở dữ liệu
    private Long id;

    @Column(name = "title", nullable = false)
    // Thuộc tính title thuộc cột title trong cơ sở dữ liệu và cột title sẽ là NOT NULL
    @NotBlank(message = "title không được để trống!")
    private String title;

    @Column(name = "description", columnDefinition = "LONGTEXT", nullable = false)
    // Thuộc tính description thuộc cột description trong cơ sở dữ liệu và cột này sẽ là NOT NULL với kiểu dữ liệu LONGTEXT
    @NotBlank(message = "description không được để trống!")
    private String description;

    @Column(name = "due_date", nullable = false)
    // Thuộc tính due_date thuộc cột due_date trong cơ sở dữ liệu và cột này sẽ là NOT NULL
    private Instant dueDate;

    @Column(name = "priority", nullable = false)
    // Thuộc tính priority thuộc cột priority trong cơ sở dữ liệu và cột này sẽ là NOT NULL
    private int priority;

    @Column(name = "completed", nullable = false)
    // Thuộc tính completed thuộc cột completed trong cơ sở dữ liệu và cột này sẽ là NOT NULL
    private boolean completed = false; // Mặc định completed có giá trị là FALSE

    @ManyToMany
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
