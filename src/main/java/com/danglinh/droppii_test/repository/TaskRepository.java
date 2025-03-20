package com.danglinh.droppii_test.repository;

import com.danglinh.droppii_test.domain.entity.Task;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
    List<Task> findByDependenciesContaining(Task dependency);

    List<Task> findAllByDueDateBeforeAndCompletedFalse(Instant now);
}
