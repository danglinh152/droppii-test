package com.danglinh.droppii_test.controller;


import com.danglinh.droppii_test.domain.DTO.request.RequestAddDependency;
import com.danglinh.droppii_test.domain.DTO.request.RequestUpdateTask;
import com.danglinh.droppii_test.domain.DTO.response.ResponseGetDependencies;
import com.danglinh.droppii_test.domain.DTO.response.ResponsePaginationDTO;
import com.danglinh.droppii_test.domain.entity.Task;
import com.danglinh.droppii_test.service.TaskService;
import com.danglinh.droppii_test.util.annotation.ApiMessage;
import com.danglinh.droppii_test.util.error.DroppiiException;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Set;


@RestController
@RequestMapping("/api/v1")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/tasks")
    @ApiMessage("Get All Tasks")
    public ResponseEntity<ResponsePaginationDTO> getAllTask(@Filter Specification<Task> spec, Pageable pageable) {
        return ResponseEntity.ok(taskService.getAllTasks(spec, pageable));
    }

    @GetMapping("/tasks/{id}")
    @ApiMessage("Get Task By Id")
    public ResponseEntity<Task> getTask(@PathVariable Long id) {
        Task task = taskService.getTaskById(id);
        return ResponseEntity.ok(task);
    }

    @GetMapping("/tasks/{id}/dependencies")
    @ApiMessage("Get Dependencies of A Task")
    public ResponseEntity<ResponseGetDependencies> getDependencies(@PathVariable Long id) {
        Task task = taskService.getTaskById(id);

        ResponseGetDependencies responseGetDependencies = new ResponseGetDependencies();
        responseGetDependencies.setTaskId(id);
        Set<Task> taskDep = task.getDependencies();
        boolean dep2 = taskDep.stream().anyMatch(item -> !item.getDependencies().isEmpty());

        responseGetDependencies.setDirectDependencies(task.getDependencies());
        if (dep2) {
            taskDep.forEach(item -> {
                responseGetDependencies.setIndirectDependencies(item.getDependencies());
            });
        }

        return ResponseEntity.ok(responseGetDependencies);
    }

    @PostMapping("/tasks")
    @ApiMessage("Create A Task")
    public ResponseEntity<Task> createTask(@Valid @RequestBody Task task) {
        if (taskService.addTask(task) == null) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @PostMapping("/add-dependency")
    @ApiMessage("Add Dependency For a Task")
    public ResponseEntity<String> addDependency(@RequestBody RequestAddDependency requestAddDependency) throws DroppiiException {
        taskService.addDependency(requestAddDependency.getTaskId(), requestAddDependency.getDependencyId());
        return ResponseEntity.status(HttpStatus.CREATED).body("ok");
    }

//    @DeleteMapping("/add-dependency")
//    @ApiMessage("Remove Dependency For a Task")
//    public ResponseEntity<String> removeDependency(@RequestBody RequestRemoveDependency requestRemoveDependency) throws DroppiiException {
//        taskService.addDependency(requestRemoveDependency.getTaskId(), requestRemoveDependency.getDependencyId());
//        return ResponseEntity.status(HttpStatus.CREATED).body("ok");
//    }

    @PutMapping("/tasks/{id}")
    @ApiMessage("Update a Task")
    public ResponseEntity<Task> updateTask(@Valid @RequestBody RequestUpdateTask requestUpdateTask, @PathVariable Long id) throws DroppiiException {
        return ResponseEntity.ok(taskService.updateTask(requestUpdateTask, id));
    }

    @DeleteMapping("/tasks/{id}")
    @ApiMessage("Delete a Task")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) throws DroppiiException {
        if (taskService.deleteTask(id)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
