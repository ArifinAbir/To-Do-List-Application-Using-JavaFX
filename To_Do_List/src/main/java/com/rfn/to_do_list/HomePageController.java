package com.rfn.to_do_list;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class HomePageController {
    @FXML private VBox taskContainer;
    @FXML private HBox normalModeButtons;
    @FXML private HBox deleteModeButtons;

    private final List<CheckBox> deleteCheckBoxes = new ArrayList<>();
    private final List<Task> currentTasks = new ArrayList<>();

    @FXML
    public void initialize() {
        loadTasks();
    }

    private void loadTasks() {
        taskContainer.getChildren().clear();
        deleteCheckBoxes.clear();
        currentTasks.clear();
        currentTasks.addAll(DatabaseService.getAllTasks());
        for (Task task : currentTasks) {
            taskContainer.getChildren().add(createTaskPane(task));
        }
    }


    private Node createTaskPane(Task task) {
        BorderPane taskPane = new BorderPane();
        taskPane.setPadding(new Insets(10));
        taskPane.setStyle("-fx-border-color: #cccccc; -fx-border-width: 1; -fx-border-radius: 5;");

        VBox infoBox = new VBox(5);
        Label titleLabel = new Label(task.getTitle());
        titleLabel.setFont(new Font("System Bold", 16));
        Label descLabel = new Label(task.getDescription());
        descLabel.setWrapText(true);
        String formattedDateTime = "No due date";
        if (task.getTaskTimestamp() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM, yyyy 'at' hh:mm a");
            formattedDateTime = "Due: " + task.getTaskTimestamp().format(formatter);
        }
        Label dateTimeLabel = new Label(formattedDateTime);
        infoBox.getChildren().addAll(titleLabel, descLabel, dateTimeLabel);

        VBox actionBox = new VBox(10);
        actionBox.setPadding(new Insets(0, 0, 0, 10));

        Button doneButton = new Button("Done");
        doneButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-pref-width: 60;");
        doneButton.setOnAction(e -> handleDoneTask(task));

        Button editButton = new Button("Edit");
        editButton.setStyle("-fx-background-color: #FFC107; -fx-text-fill: white; -fx-pref-width: 60;");
        editButton.setOnAction(e -> handleEditTask(task));

        CheckBox deleteCheck = new CheckBox();
        deleteCheck.setVisible(false);
        deleteCheck.setManaged(false);
        deleteCheckBoxes.add(deleteCheck);

        actionBox.getChildren().addAll(doneButton, editButton, deleteCheck);

        taskPane.setCenter(infoBox);
        taskPane.setRight(actionBox);
        return taskPane;
    }

    @FXML
    void handleAddButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddTaskDialog.fxml"));
            Parent root = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add New Task");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(taskContainer.getScene().getWindow());
            dialogStage.setScene(new Scene(root));
            dialogStage.showAndWait();
            loadTasks();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleDoneTask(Task task) {
        try {
            DatabaseService.moveTaskToCompleted(task);
            loadTasks();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to mark task as completed.");
        }
    }

    @FXML
    void handleEnterDeleteMode() {
        toggleDeleteMode(true);
    }

    @FXML
    void handleCancelDeleteMode() {
        toggleDeleteMode(false);
    }

    @FXML
    void handleConfirmDelete() {
        List<Task> tasksToDelete = new ArrayList<>();
        for (int i = 0; i < deleteCheckBoxes.size(); i++) {
            if (deleteCheckBoxes.get(i).isSelected()) {
                tasksToDelete.add(currentTasks.get(i));
            }
        }
        if (tasksToDelete.isEmpty()) {
            showAlert("No Selection", "Please select tasks to delete.");
            return;
        }
        try {
            DatabaseService.moveTasksToDeleted(tasksToDelete);
            toggleDeleteMode(false);
            loadTasks();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to delete tasks.");
        }
    }

    private void toggleDeleteMode(boolean enabled) {
        normalModeButtons.setVisible(!enabled);
        normalModeButtons.setManaged(!enabled);
        deleteModeButtons.setVisible(enabled);
        deleteModeButtons.setManaged(enabled);
        for (CheckBox cb : deleteCheckBoxes) {
            cb.setVisible(enabled);
            cb.setManaged(enabled);
            cb.setSelected(false);
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    void handleViewCompletedTasks() {
        openWindow("CompletedTasksView.fxml", "Completed Tasks");
    }

    @FXML
    void handleViewDeletedTasks() {
        openWindow("DeletedTasksView.fxml", "Deleted Tasks");
    }

    // HomePageController.java ফাইলের শেষে এই নতুন মেথডটি যোগ করুন
    @FXML
    void handleFilterByDate() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FilterDialog.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Filter Tasks");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(taskContainer.getScene().getWindow());
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openWindow(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(taskContainer.getScene().getWindow());
            stage.setScene(new Scene(root));
            stage.showAndWait();
            loadTasks();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleEditTask(Task task) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddTaskDialog.fxml"));
            Parent root = loader.load();

            AddTaskDialogController controller = loader.getController();
            controller.setTaskToEdit(task);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Task");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(taskContainer.getScene().getWindow());
            dialogStage.setScene(new Scene(root));
            dialogStage.showAndWait();

            loadTasks();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}