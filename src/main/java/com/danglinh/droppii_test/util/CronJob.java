package com.danglinh.droppii_test.util;

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

    @Scheduled(cron = "0 0 0 * * MON") // Every Monday at midnight
    public void printMonday() {
        System.out.println("Monday");
    }

    @Scheduled(cron = "0 0 0 * * TUE") // Every Tuesday at midnight
    public void printTuesday() {
        System.out.println("Tuesday");
    }

    @Scheduled(cron = "0 0 0 * * WED") // Every Wednesday at midnight
    public void printWednesday() {
        System.out.println("Wednesday");
    }

    @Scheduled(cron = "0 0 0 * * THU") // Every Thursday at midnight
    public void printThursday() {
        System.out.println("Thursday");
    }

    @Scheduled(cron = "0 0 0 * * FRI") // Every Friday at midnight
    public void printFriday() {
        System.out.println("Friday");
    }

    @Scheduled(cron = "0 0 0 * * SAT") // Every Saturday at midnight
    public void printSaturday() {
        System.out.println("Saturday");
    }

    @Scheduled(cron = "0 0 0 * * SUN") // Every Sunday at midnight
    public void printSunday() {
        System.out.println("Sunday");
    }


    @Scheduled(cron = "0 0 0 * * *") // Mỗi ngày vào lúc khởi đầu ngày mới :v
    public void notifyOverdueTasks() {
        List<Task> overdueTasks = taskRepository.findAllByDueDateBeforeAndCompletedFalse(Instant.now());

        for (Task task : overdueTasks) {
            System.out.println("Công việc quá hạn: " + task.getTitle() + ", Hạn chót: " + task.getDueDate());
        }
    }
}
