package com.rfn.to_do_list;

import java.time.LocalDateTime;

public class Task {
    private final int taskId;
    private final String title;
    private final String description;
    private final LocalDateTime taskTimestamp;
    private boolean completed; // 🟢 ADDED: Field to track completion status

    public Task(int taskId, String title, String description, LocalDateTime taskTimestamp) {
        this.taskId = taskId;
        this.title = title;
        this.description = description;
        this.taskTimestamp = taskTimestamp;
        this.completed = false; // By default, a new task is not completed
    }

    // --- Getters ---
    public int getTaskId() { return taskId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDateTime getTaskTimestamp() { return taskTimestamp; }


    /**
     * 🟢 ADDED: Checks if the task is completed.
     * @return true if completed, false otherwise.
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * 🟢 ADDED: Sets the completion status of the task.
     * @param completed The new completion status.
     */
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}