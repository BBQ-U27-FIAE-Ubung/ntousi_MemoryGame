package de.bbq.memorygame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Φόρτωση αρχικού FXML (primary.fxml)
        Parent root = FXMLLoader.load(getClass().getResource("/de/bbq/memorygame/primary.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Memory Game - Menu");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
