package com.rfn.to_do_list;

import java.time.LocalDateTime;

public class Task {
    private final int taskId;
    private final String title;
    private final String description;
    private final LocalDateTime taskTimestamp;
    private boolean completed;

    public Task(int taskId, String title, String description, LocalDateTime taskTimestamp) {
        this.taskId = taskId;
        this.title = title;
        this.description = description;
        this.taskTimestamp = taskTimestamp;
        this.completed = false;
    }

    // --- Getters ---
    public int getTaskId() { return taskId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDateTime getTaskTimestamp() { return taskTimestamp; }



    public boolean isCompleted() {
        return completed;
    }


    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}