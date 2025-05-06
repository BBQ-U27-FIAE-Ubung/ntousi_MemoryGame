package de.bbq.memorygame;

import java.io.IOException;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

public class PrimaryController {

    @FXML
    private void handleBeginner(ActionEvent event) {
        try {
            loadGame(4, 4, event);
        } catch (Exception e) {
        }
    }

    @FXML
    private void handleAdvanced(ActionEvent event) {
        try {
            loadGame(6, 6, event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleExpert(ActionEvent event) {
        try {
            loadGame(8, 8, event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadGame(int rows, int cols, ActionEvent event) {
        javafx.scene.Node sourceNode = (javafx.scene.Node) event.getSource();
        Parent currentRoot = sourceNode.getScene().getRoot();

        FadeTransition fadeOut = new FadeTransition(Duration.millis(400), currentRoot);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        fadeOut.setOnFinished(ev -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/de/bbq/memorygame/game-view.fxml"));
                Parent gameRoot = loader.load();

                GameController controller = loader.getController();
                controller.setGridSize(rows, cols);

                FadeTransition fadeIn = new FadeTransition(Duration.millis(400), gameRoot);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.play();

                Scene scene = new Scene(gameRoot);
                Stage stage = (Stage) sourceNode.getScene().getWindow();
                stage.setScene(scene);
                stage.setTitle("Memory Game");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        fadeOut.play();
    }
}
