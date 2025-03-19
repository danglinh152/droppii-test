package com.danglinh.droppii_test.service;

import com.danglinh.droppii_test.domain.DTO.request.RequestUpdateTask;
import com.danglinh.droppii_test.domain.DTO.response.Meta;
import com.danglinh.droppii_test.domain.DTO.response.ResponsePaginationDTO;
import com.danglinh.droppii_test.domain.entity.Task;
import com.danglinh.droppii_test.repository.TaskRepository;
import com.danglinh.droppii_test.util.error.DroppiiException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }


    public ResponsePaginationDTO getAllTasks(Specification<Task> spec, Pageable pageable) {
        Page<Task> pageTasks = taskRepository.findAll(spec, pageable);
        Meta meta = new Meta();
        meta.setCurrentPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setTotal(pageTasks.getTotalElements());
        meta.setTotalPages(pageTasks.getTotalPages());

        ResponsePaginationDTO responsePaginationDTO = new ResponsePaginationDTO();
        responsePaginationDTO.setMeta(meta);
        responsePaginationDTO.setData(pageTasks.getContent());

        return responsePaginationDTO;
    }

    public Task getTaskById(Long id) {
        Optional<Task> task = taskRepository.findById(id);
        return task.orElse(null);
    }

    private boolean detectCircularDependency(Task task, Task dependency) {
        Set<Task> visited = new HashSet<>();
        return hasCycle(task, dependency, visited);
    }

    private boolean hasCycle(Task task, Task dependency, Set<Task> visited) {
        if (visited.contains(task)) {
            return true;
        }
        visited.add(task);

        if (!task.getDependencies().isEmpty()) {
            for (Task dep : task.getDependencies()) {
                if (hasCycle(dep, dependency, visited)) {
                    return true;
                }
            }
        } else {
            for (Task dep : dependency.getDependencies()) {
                if (hasCycle(dep, task, visited)) {
                    return true;
                }
            }
        }
        visited.remove(task);
        return false;
    }


    public void addDependency(Long taskId, Long dependencyId) throws DroppiiException {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new DroppiiException("Task not found"));
        Task dependency = taskRepository.findById(dependencyId).orElseThrow(() -> new DroppiiException("Dependency not found"));

        if (detectCircularDependency(task, dependency)) {
            throw new DroppiiException("Circular dependency detected");
        }

        task.getDependencies().add(dependency);
        taskRepository.save(task);
    }

    public Task updateTask(RequestUpdateTask requestUpdateTask, Long id) throws DroppiiException {
        Task task = taskRepository.findById(id).orElseThrow(() -> new DroppiiException("Task not found"));
        task.setTitle(requestUpdateTask.getTitle());
        task.setDescription(requestUpdateTask.getDescription());
        task.setDueDate(requestUpdateTask.getDueDate());
        task.setPriority(requestUpdateTask.getPriority());
        task.setCompleted(requestUpdateTask.isCompleted());

        if (!requestUpdateTask.getDependencies().isEmpty()) {
            task.setDependencies(requestUpdateTask.getDependencies());
        }

        return taskRepository.save(task);
    }


    public Task addTask(Task task) {
        return taskRepository.save(task);
    }

    public boolean deleteTask(Long id) {
        // Find the task by ID
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        // Check for dependencies
        List<Task> dependentTasks = taskRepository.findByDependenciesContaining(task);
        if (!dependentTasks.isEmpty()) {
            throw new RuntimeException("Cannot delete task. It has dependencies in other tasks.");
        }

        // Proceed to delete the task
        taskRepository.delete(task);
        return true;
    }

}
