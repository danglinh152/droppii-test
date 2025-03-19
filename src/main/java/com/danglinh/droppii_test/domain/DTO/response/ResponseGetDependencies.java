package com.danglinh.droppii_test.domain.DTO.response;

import com.danglinh.droppii_test.domain.entity.Task;


import java.util.HashSet;
import java.util.Set;


public class ResponseGetDependencies {
    private Long taskId;
    private Set<Task>  directDependencies = new HashSet<>();
    private Set<Task>  indirectDependencies = new HashSet<>();

    public ResponseGetDependencies() {
    }

    public ResponseGetDependencies(Long taskId, Set<Task> directDependencies, Set<Task> indirectDependencies) {
        this.taskId = taskId;
        this.directDependencies = directDependencies;
        this.indirectDependencies = indirectDependencies;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Set<Task> getDirectDependencies() {
        return directDependencies;
    }

    public void setDirectDependencies(Set<Task> directDependencies) {
        this.directDependencies = directDependencies;
    }

    public Set<Task> getIndirectDependencies() {
        return indirectDependencies;
    }

    public void setIndirectDependencies(Set<Task> indirectDependencies) {
        this.indirectDependencies = indirectDependencies;
    }
}
