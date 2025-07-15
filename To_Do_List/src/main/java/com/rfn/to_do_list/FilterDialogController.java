package com.rfn.to_do_list;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class FilterDialogController {
    @FXML private DatePicker startDatePicker;
    @FXML private TextField startTimeField;
    @FXML private DatePicker endDatePicker;
    @FXML private TextField endTimeField;

    @FXML
    private void handleSearch() {
        try {
            LocalDate startDate = startDatePicker.getValue();
            LocalTime startTime = LocalTime.parse(startTimeField.getText());
            LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime);

            LocalDate endDate = endDatePicker.getValue();
            LocalTime endTime = LocalTime.parse(endTimeField.getText());
            LocalDateTime endDateTime = LocalDateTime.of(endDate, endTime);

            if (startDate == null || endDate == null) {
                showAlert("Input Error", "Please select both start and end dates.");
                return;
            }

            // নতুন উইন্ডোতে ফলাফল দেখানোর কোড
            showFilteredTasksWindow(startDateTime, endDateTime);

        } catch (DateTimeParseException e) {
            showAlert("Invalid Time", "Please enter time in HH:mm format (e.g., 09:30).");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "An unexpected error occurred.");
        }
    }

    private void showFilteredTasksWindow(LocalDateTime start, LocalDateTime end) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FilteredTasksView.fxml"));
        Parent root = loader.load();

        // ফলাফল দেখানোর কন্ট্রোলারে start ও end সময় পাঠানো হচ্ছে
        FilteredTasksController controller = loader.getController();
        controller.loadTasksInInterval(start, end);

        Stage stage = new Stage();
        stage.setTitle("Filtered Tasks");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}