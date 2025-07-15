package com.rfn.to_do_list;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FilteredTasksController {
    @FXML private VBox taskContainer;
    @FXML private Label titleLabel;

    public void loadTasksInInterval(LocalDateTime start, LocalDateTime end) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy hh:mm a");
        titleLabel.setText("Tasks from " + start.format(formatter) + " to " + end.format(formatter));

        taskContainer.getChildren().clear();
        List<Task> tasks = DatabaseService.getTasksBetween(start, end);

        if (tasks.isEmpty()) {
            taskContainer.getChildren().add(new Label("No tasks found in this interval."));
        } else {
            for (Task task : tasks) {
                taskContainer.getChildren().add(createTaskPane(task));
            }
        }
    }

    private Node createTaskPane(Task task) {
        BorderPane taskPane = new BorderPane();
        taskPane.setPadding(new Insets(10));
        taskPane.setStyle("-fx-background-color: #e3f2fd; -fx-border-color: #bbdefb;");
        VBox infoBox = new VBox(5);
        Label titleLabel = new Label(task.getTitle());
        titleLabel.setFont(new Font("System Bold", 16));
        Label descLabel = new Label(task.getDescription());
        descLabel.setWrapText(true);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM, yyyy 'at' hh:mm a");
        Label dateTimeLabel = new Label("Due: " + task.getTaskTimestamp().format(formatter));
        infoBox.getChildren().addAll(titleLabel, descLabel, dateTimeLabel);
        taskPane.setCenter(infoBox);
        return taskPane;
    }
}