package com.rfn.to_do_list;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CompletedTasksController {
    @FXML
    private VBox taskContainer;

    private final List<CheckBox> taskCheckBoxes = new ArrayList<>();
    private final List<Task> currentCompletedTasks = new ArrayList<>();

    @FXML
    public void initialize() {
        loadCompletedTasks();
    }

    private void loadCompletedTasks() {
        taskContainer.getChildren().clear();
        taskCheckBoxes.clear();
        currentCompletedTasks.clear();

        currentCompletedTasks.addAll(DatabaseService.getCompletedTasks());
        for (Task task : currentCompletedTasks) {
            taskContainer.getChildren().add(createTaskPane(task));
        }
    }

    private Node createTaskPane(Task task) {
        BorderPane taskPane = new BorderPane();
        taskPane.setPadding(new Insets(10));
        taskPane.setStyle("-fx-background-color: #e8f5e9; -fx-border-color: #c8e6c9;");

        VBox infoBox = new VBox(5);
        Label titleLabel = new Label(task.getTitle());
        titleLabel.setFont(new Font("System Bold", 16));
        Label descLabel = new Label(task.getDescription());
        descLabel.setWrapText(true);

        String formattedDateTime = "Original due date not available";
        if (task.getTaskTimestamp() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM, yyyy 'at' hh:mm a");
            formattedDateTime = "Originally due: " + task.getTaskTimestamp().format(formatter);
        }
        Label dateTimeLabel = new Label(formattedDateTime);
        infoBox.getChildren().addAll(titleLabel, descLabel, dateTimeLabel);

        CheckBox taskCheck = new CheckBox();
        taskCheckBoxes.add(taskCheck);

        taskPane.setCenter(infoBox);
        taskPane.setRight(taskCheck);
        return taskPane;
    }

    @FXML
    private void handlePermanentDelete() {
        List<Task> tasksToDelete = new ArrayList<>();
        for (int i = 0; i < taskCheckBoxes.size(); i++) {
            if (taskCheckBoxes.get(i).isSelected()) {
                tasksToDelete.add(currentCompletedTasks.get(i));
            }
        }

        if (tasksToDelete.isEmpty()) {
            showAlert("No Selection", "Please select tasks for permanent deletion.");
            return;
        }

        try {
            DatabaseService.permanentlyDeleteCompletedTasks(tasksToDelete);
            loadCompletedTasks(); 
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to permanently delete tasks.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
