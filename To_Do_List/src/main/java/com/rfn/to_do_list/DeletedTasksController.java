package com.rfn.to_do_list;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DeletedTasksController {
    @FXML
    private VBox taskContainer;

    private final List<CheckBox> taskCheckBoxes = new ArrayList<>();
    private final List<Task> currentDeletedTasks = new ArrayList<>();

    @FXML
    public void initialize() {
        loadDeletedTasks();
    }

    private void loadDeletedTasks() {
        taskContainer.getChildren().clear();
        taskCheckBoxes.clear();
        currentDeletedTasks.clear();
        currentDeletedTasks.addAll(DatabaseService.getDeletedTasks());
        for (Task task : currentDeletedTasks) {
            taskContainer.getChildren().add(createTaskPane(task));
        }
    }

    private Node createTaskPane(Task task) {
        BorderPane taskPane = new BorderPane();
        taskPane.setStyle("-fx-background-color: #ffebee; -fx-border-color: #ffcdd2;");
        taskPane.setPadding(new Insets(10));
        VBox infoBox = new VBox(5);
        Label titleLabel = new Label(task.getTitle());
        titleLabel.setFont(new Font("System Bold", 16));
        infoBox.getChildren().add(titleLabel);
        CheckBox taskCheck = new CheckBox();
        taskCheckBoxes.add(taskCheck);
        taskPane.setCenter(infoBox);
        taskPane.setRight(taskCheck);
        return taskPane;
    }

    private List<Task> getSelectedTasks() {
        List<Task> selectedTasks = new ArrayList<>();
        for (int i = 0; i < taskCheckBoxes.size(); i++) {
            if (taskCheckBoxes.get(i).isSelected()) {
                selectedTasks.add(currentDeletedTasks.get(i));
            }
        }
        return selectedTasks;
    }

    @FXML
    void handleRestoreTasks() {
        List<Task> tasksToRestore = getSelectedTasks();
        if (tasksToRestore.isEmpty()) {
            showAlert("No Selection", "Please select tasks to restore.");
            return;
        }
        try {
            DatabaseService.restoreTasks(tasksToRestore);
            loadDeletedTasks();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to restore tasks.");
        }
    }

    @FXML
    void handlePermanentDelete() {
        List<Task> tasksToDelete = getSelectedTasks();
        if (tasksToDelete.isEmpty()) {
            showAlert("No Selection", "Please select tasks for permanent deletion.");
            return;
        }
        try {
            DatabaseService.permanentlyDeleteTasks(tasksToDelete);
            loadDeletedTasks();
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