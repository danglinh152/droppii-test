package com.danglinh.droppii_test.domain.DTO.request;


public class RequestAddDependency {
    private Long taskId;
    private Long dependencyId;

    public RequestAddDependency() {
    }

    public RequestAddDependency(Long dependencyId, Long taskId) {
        this.dependencyId = dependencyId;
        this.taskId = taskId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getDependencyId() {
        return dependencyId;
    }

    public void setDependencyId(Long dependencyId) {
        this.dependencyId = dependencyId;
    }
}
