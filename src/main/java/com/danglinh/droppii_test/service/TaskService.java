package com.danglinh.droppii_test.service;

import com.danglinh.droppii_test.domain.DTO.request.UpdatedTask;
import com.danglinh.droppii_test.domain.DTO.response.Meta;
import com.danglinh.droppii_test.domain.DTO.response.ResponsePaginationDTO;
import com.danglinh.droppii_test.domain.entity.Task;
import com.danglinh.droppii_test.repository.TaskRepository;
import com.danglinh.droppii_test.util.error.DroppiiException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Instant;
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

    @Cacheable(value = "tasks")
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

    @Cacheable(value = "tasks", key = "#id")
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

    @CacheEvict(value = "tasks", allEntries = true)
    public void addDependency(Long taskId, Long dependencyId) throws DroppiiException {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new DroppiiException("Task not found"));
        Task dependency = taskRepository.findById(dependencyId).orElseThrow(() -> new DroppiiException("Dependency not found"));

        if (detectCircularDependency(task, dependency)) {
            throw new DroppiiException("Circular dependency detected");
        }

        task.getDependencies().add(dependency);
        taskRepository.save(task);
    }

    @CacheEvict(value = "tasks", allEntries = true)
    public void removeDependency(Long taskId, Long dependencyId) throws DroppiiException {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new DroppiiException("Task not found"));
        Set<Task> taskDep = task.getDependencies();
        taskDep.remove(taskRepository.findById(dependencyId).orElseThrow(() -> new DroppiiException("Dependency not found")));

        task.setDependencies(taskDep);
        taskRepository.save(task);
    }

    @CachePut(value = "tasks", key = "#id")
    public Task updateTask(UpdatedTask requestUpdateTask, Long id) throws DroppiiException {
        Task task = taskRepository.findById(id).orElseThrow(() -> new DroppiiException("Task not found"));
        if (requestUpdateTask != null) {
            task.setTitle(requestUpdateTask.getTitle());
            task.setDescription(requestUpdateTask.getDescription());
            task.setDueDate(requestUpdateTask.getDueDate());
            task.setPriority(requestUpdateTask.getPriority());
            task.setCompleted(requestUpdateTask.isCompleted());

            // Keep current dependencies if none provided
            if (!requestUpdateTask.getDependencies().isEmpty()) {
                task.setDependencies(requestUpdateTask.getDependencies());
            }
            return taskRepository.save(task);
        } else {
            throw new DroppiiException("Request Update Task is null?");
        }
    }

    @CacheEvict(value = "tasks", key = "#id")
    public Task completeTask(Long id) throws DroppiiException {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new DroppiiException("Không tìm thấy task với ID " + id));

        Set<Task> taskDep = task.getDependencies();

        // Check if all dependencies are completed
        boolean isCompleteAll = taskDep.stream().allMatch(Task::isCompleted);

        // Check if the task is overdue
        Instant now = Instant.now();
        boolean isOverDue = now.isAfter(task.getDueDate());

        if (isCompleteAll && !isOverDue) {
            task.setCompleted(true);
            return taskRepository.save(task); // Save and return updated task
        } else if (!isCompleteAll && isOverDue) {
            throw new DroppiiException("Task không thể hoàn thành vì đã quá hạn và có các dependency chưa hoàn thành.");
        } else if (!isCompleteAll) {
            throw new DroppiiException("Task không thể hoàn thành vì không phải tất cả các dependency đã hoàn thành.");
        } else {
            throw new DroppiiException("Task không thể hoàn thành vì đã quá hạn.");
        }
    }

    @CachePut(value = "tasks", key = "#task.id")
    public Task addTask(Task task) {
        return taskRepository.save(task);
    }

    @CacheEvict(value = "tasks", key = "#id")
    public boolean deleteTask(Long id) throws DroppiiException {
        // Find the task by ID
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new DroppiiException("Task not found"));

        // Check for dependencies
        List<Task> dependentTasks = taskRepository.findByDependenciesContaining(task);
        if (!dependentTasks.isEmpty()) {
            throw new DroppiiException("Cannot delete task. It has dependencies in other tasks.");
        }
        taskRepository.delete(task);
        return true;
    }
}
