package com.rfn.to_do_list;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Objects;

public class Main extends Application {
    private final NotificationService notificationService = new NotificationService();

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("HomePage.fxml")));
        primaryStage.setTitle("To-Do List");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();

        notificationService.start();
    }

    @Override
    public void stop() throws Exception {
        notificationService.stop();
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}