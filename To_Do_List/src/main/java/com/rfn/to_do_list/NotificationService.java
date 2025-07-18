package com.rfn.to_do_list;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NotificationService {
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    private final Set<Integer> notified2Min = new HashSet<>();
    private final Set<Integer> alarmedTaskIds = new HashSet<>();

    public void start() {
        scheduler.scheduleAtFixedRate(this::checkTasks, 0, 1, TimeUnit.SECONDS);
    }

    private void checkTasks() {
        Platform.runLater(() -> {
            LocalDateTime now = LocalDateTime.now();
            for (Task task : DatabaseService.getAllTasks()) {
                int taskId = task.getTaskId();
                LocalDateTime taskTime = task.getTaskTimestamp();

                if (taskTime == null || task.isCompleted()) {
                    continue;
                }

                if (alarmedTaskIds.contains(taskId)) {
                    continue;
                }

                if (!taskTime.isAfter(now)) {
                    alarmedTaskIds.add(taskId);

                    showAlarmAlert("Task Due: " + task.getTitle(), "The task '" + task.getTitle() + "' is due now!");

                } else {
                    if (!notified2Min.contains(taskId)) {
                        long secondsUntilTask = Duration.between(now, taskTime).toSeconds();
                        if (secondsUntilTask <= 120) {
                            notified2Min.add(taskId);
                            showInfoAlert("Reminder: 2 Minutes", "Task '" + task.getTitle() + "' is due in about 2 minutes.");
                        }
                    }
                }
            }
        });
    }

    private void showInfoAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.show();
    }

    private void showAlarmAlert(String title, String content) {
        SoundUtil.playAlarm();

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText("ALARM!");
        alert.setContentText(content);

        alert.showAndWait();

        SoundUtil.stopAlarm();
    }

    public void stop() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }
}