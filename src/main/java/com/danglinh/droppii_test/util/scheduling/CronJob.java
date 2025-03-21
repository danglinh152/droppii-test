package com.danglinh.droppii_test.util.scheduling;

import com.danglinh.droppii_test.domain.entity.Task;
import com.danglinh.droppii_test.repository.TaskRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class CronJob {
    private final TaskRepository taskRepository;

    public CronJob(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    //    Tasks due within 24 hours:
    @Scheduled(cron = "0 0 0 * * *") // at 00h00 everydayyyyy
    public void notifyDueTasksInNext24Hours() {
        Instant now = Instant.now();
        Instant nextDay = now.plusSeconds(86400); // 24 hours later

        List<Task> dueTasks = taskRepository.findAllByDueDateBetweenAndCompletedFalse(now, nextDay);

        for (Task task : dueTasks) {
            System.out.println("Task due within 24 hours: " + task.getTitle() + ", Due date: " + task.getDueDate());
        }
    }


    //    Overdue Tasks:
    @Scheduled(cron = "0 0 0 * * *") // at 00h00 everydayyyyy
    public void notifyOverdueTasks() {
        List<Task> overdueTasks = taskRepository.findAllByDueDateBeforeAndCompletedFalse(Instant.now());

        for (Task task : overdueTasks) {
            System.out.println("Công việc quá hạn: " + task.getTitle() + ", Hạn chót: " + task.getDueDate());
        }
    }
}
