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

    @Scheduled(cron = "0 0 0 * * MON") // at 00h00 at Monday day
    public void printMonday() {
        System.out.println("Monday");
    }

    @Scheduled(cron = "0 0 0 * * TUE") // at 00h00 at Tuesday day
    public void printTuesday() {
        System.out.println("Tuesday");
    }

    @Scheduled(cron = "0 0 0 * * WED") // at 00h00 at Wednesday day
    public void printWednesday() {
        System.out.println("Wednesday");
    }

    @Scheduled(cron = "0 0 0 * * THU") // at 00h00 at Thursday day
    public void printThursday() {
        System.out.println("Thursday");
    }

    @Scheduled(cron = "0 0 0 * * FRI") // at 00h00 at Friday day
    public void printFriday() {
        System.out.println("Friday");
    }

    @Scheduled(cron = "0 0 0 * * SAT") // at 00h00 at Saturday day
    public void printSaturday() {
        System.out.println("Saturday");
    }

    @Scheduled(cron = "0 0 0 * * SUN") // at 00h00 at Sunday day
    public void printSunday() {
        System.out.println("Sunday");
    }


    @Scheduled(cron = "0 0 0 * * *") // at 00h00 everydayyyyy
    public void notifyOverdueTasks() {
        List<Task> overdueTasks = taskRepository.findAllByDueDateBeforeAndCompletedFalse(Instant.now());

        for (Task task : overdueTasks) {
            System.out.println("Công việc quá hạn: " + task.getTitle() + ", Hạn chót: " + task.getDueDate());
        }
    }
}
