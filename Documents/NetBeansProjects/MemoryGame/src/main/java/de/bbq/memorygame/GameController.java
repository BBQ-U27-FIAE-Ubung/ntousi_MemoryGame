package de.bbq.memorygame;

import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

import java.io.IOException;
import java.util.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameController {

    @FXML
    private GridPane gridPane;

    @FXML
    private Button backButton;

    @FXML
    private Label timerLabel;
    
    @FXML
    private Label timeLabel;


    private int rows;
    private int cols;
    private long startTime;
    private int matchesFound = 0;
    private Card firstCard = null;
    private boolean lock = false;
    private Timer timer;

    public void setGridSize(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        initializeGrid();
    }

    private void initializeGrid() {
        gridPane.getChildren().clear();
        List<Card> cards = new ArrayList<>();
        Set<String> usedImages = new HashSet<>();

        String[] imageNames = {
            "dog.png", "cat.png", "apple.png", "car.png", "cloud.png",
            "moon.png", "tree.png", "fish.png", "bird.png", "ball.png",
            "book.png", "chair.png", "duck.png", "elephant.png", 
            "flower.png", "guitar.png", "hat.png", "ice-cream.png"
        };

        int totalCards = rows * cols;
        int numPairs = totalCards / 2;
        Random rand = new Random();

        for (int i = 0; i < numPairs; i++) {
            String imageName;
            do {
                imageName = imageNames[rand.nextInt(imageNames.length)];
            } while (usedImages.contains(imageName));
            usedImages.add(imageName);

            Image image = new Image(getClass().getResourceAsStream("/images/" + imageName));
            cards.add(new Card(image));
            cards.add(new Card(image));
        }

        Collections.shuffle(cards);

        int index = 0;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Card card = cards.get(index++);
                card.setOnAction(e -> handleCardClick(card));
                gridPane.add(card, col, row);
            }
        }

        startTime = System.currentTimeMillis();
        startTimer();
        matchesFound = 0;
    }

    private void handleCardClick(Card clickedCard) {
        if (clickedCard.isFlipped() || lock) return;

        clickedCard.show();
        scaleUpCard(clickedCard);

        if (firstCard == null) {
            firstCard = clickedCard;
        } else {
            lock = true;
            Card secondCard = clickedCard;

            new Thread(() -> {
                try {
                    Thread.sleep(800);
                } catch (InterruptedException ignored) {
                }
                Platform.runLater(() -> {
                    if (firstCard.getImage().equals(secondCard.getImage())) {
                        matchesFound++;
                        showPairFoundEffect(firstCard, secondCard);
                        checkGameOver();
                    } else {
                        rotateCardBack(firstCard);
                        rotateCardBack(secondCard);
                    }
                    firstCard = null;
                    lock = false;
                });
            }).start();
        }
    }

    private void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    long now = System.currentTimeMillis();
                    long elapsedSeconds = (now - startTime) / 1000;

                    long minutes = elapsedSeconds / 60;
                    long seconds = elapsedSeconds % 60;
                    String timeFormatted = String.format("%02d:%02d", minutes, seconds);
                    timerLabel.setText(timeFormatted);
                });
            }
        }, 0, 1000);
    }

    private void checkGameOver() {
        int totalPairs = (rows * cols) / 2;
        if (matchesFound == totalPairs) {
            long endTime = System.currentTimeMillis();
            long elapsedSeconds = (endTime - startTime) / 1000;
            timer.cancel();
            gameOverEffect();

            long minutes = elapsedSeconds / 60;
            long seconds = elapsedSeconds % 60;
            String finalTime = String.format("%02d:%02d", minutes, seconds);

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Game Over");
            alert.setHeaderText("Congratulations!");
            alert.setContentText("You completed the game in " + finalTime + ".");
            alert.showAndWait();
        }
    }

    private void scaleUpCard(Card card) {
        ScaleTransition scale = new ScaleTransition(Duration.millis(200), card);
        scale.setToX(1.2);
        scale.setToY(1.2);
        scale.play();
    }

    private void scaleDownCard(Card card) {
        ScaleTransition scale = new ScaleTransition(Duration.millis(200), card);
        scale.setToX(1.0);
        scale.setToY(1.0);
        scale.play();
    }

    private void rotateCardBack(Card card) {
        ScaleTransition rotate = new ScaleTransition(Duration.millis(400), card);
        rotate.setOnFinished(e -> card.hide());
        rotate.play();
    }

    private void showPairFoundEffect(Card firstCard, Card secondCard) {
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.millis(100), e -> {
                firstCard.setStyle("-fx-border-color: green; -fx-border-width: 5px;");
                secondCard.setStyle("-fx-border-color: green; -fx-border-width: 5px;");
            }),
            new KeyFrame(Duration.millis(200), e -> {
                firstCard.setStyle("-fx-border-color: transparent;");
                secondCard.setStyle("-fx-border-color: transparent;");
            })
        );
        timeline.setCycleCount(2);
        timeline.play();
    }

    private void gameOverEffect() {
        ScaleTransition scale = new ScaleTransition(Duration.millis(800), gridPane);
        scale.setByX(1.2);
        scale.setByY(1.2);
        scale.setCycleCount(2);
        scale.setAutoReverse(true);
        scale.play();
    }

    @FXML
    private void handleBackToMenu() {
        if (timer != null) timer.cancel();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/de/bbq/memorygame/primary.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Memory Game");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
