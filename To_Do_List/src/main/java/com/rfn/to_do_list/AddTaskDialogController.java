package com.rfn.to_do_list;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class AddTaskDialogController {
    @FXML private TextField titleField;
    @FXML private TextArea descriptionArea;
    @FXML private DatePicker datePicker;
    @FXML private TextField timeField;
    @FXML private ComboBox<String> ampmComboBox; // নতুন ComboBox

    private Task taskToEdit;

    // এই মেথডটি ডায়ালগ খোলার সাথে সাথে ComboBox-এ AM/PM যোগ করবে
    @FXML
    public void initialize() {
        ampmComboBox.setItems(FXCollections.observableArrayList("AM", "PM"));
        ampmComboBox.getSelectionModel().selectFirst(); // ডিফল্ট হিসেবে AM সিলেক্ট থাকবে
    }

    public void setTaskToEdit(Task task) {
        this.taskToEdit = task;
        titleField.setText(task.getTitle());
        descriptionArea.setText(task.getDescription());
        if (task.getTaskTimestamp() != null) {
            LocalDateTime taskDateTime = task.getTaskTimestamp();
            datePicker.setValue(taskDateTime.toLocalDate());

            // 12-ঘণ্টার ফরম্যাটে সময় দেখানো
            int hour = taskDateTime.getHour();
            String ampm = (hour < 12) ? "AM" : "PM";
            if (hour > 12) {
                hour -= 12;
            } else if (hour == 0) {
                hour = 12;
            }
            timeField.setText(String.format("%02d:%02d", hour, taskDateTime.getMinute()));
            ampmComboBox.setValue(ampm);
        }
    }

    @FXML
    private void handleSave() {
        String title = titleField.getText();
        LocalDate date = datePicker.getValue();
        String timeStr = timeField.getText();
        String ampm = ampmComboBox.getValue();

        if (title == null || title.trim().isEmpty() || date == null || timeStr == null || timeStr.trim().isEmpty()) {
            showAlert("Invalid Input", "Title, Date, and Time are required.");
            return;
        }

        try {
            // "02:30" স্ট্রিং থেকে ঘণ্টা ও মিনিট আলাদা করা
            String[] timeParts = timeStr.split(":");
            int hour = Integer.parseInt(timeParts[0]);
            int minute = Integer.parseInt(timeParts[1]);

            // AM/PM অনুযায়ী 24-ঘণ্টার ফরম্যাটে রূপান্তর
            if (ampm.equals("PM") && hour != 12) {
                hour += 12;
            } else if (ampm.equals("AM") && hour == 12) {
                hour = 0; // 12 AM মানে 00:00
            }

            LocalTime time = LocalTime.of(hour, minute);
            LocalDateTime dateTime = LocalDateTime.of(date, time);

            if (taskToEdit == null) {
                DatabaseService.insertTask(title, descriptionArea.getText(), dateTime);
            } else {
                DatabaseService.updateTask(taskToEdit.getTaskId(), title, descriptionArea.getText(), dateTime);
            }
            closeWindow();

        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            showAlert("Invalid Time Format", "Please enter time in hh:mm format (e.g., 02:30).");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to save the task.");
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        ((Stage) titleField.getScene().getWindow()).close();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}