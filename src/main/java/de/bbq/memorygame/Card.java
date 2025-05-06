package de.bbq.memorygame;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.animation.FadeTransition;
import javafx.util.Duration;

public class Card extends Button {
    private final Image frontImage;
    private final ImageView frontView;
    private final Image backImage;
    private final ImageView backView;
    private boolean flipped = false;

    public Card(Image frontImage) {
        this.frontImage = frontImage;
        this.frontView = new ImageView(frontImage);
        this.backImage = new Image(getClass().getResourceAsStream("/images/back.png"));
        this.backView = new ImageView(backImage);

        frontView.setFitWidth(70);
        frontView.setFitHeight(70);
        backView.setFitWidth(70);
        backView.setFitHeight(70);

        setGraphic(backView); // ξεκινάει κλειστή
        setStyle("-fx-background-color: transparent;"); // χωρίς κουμπί-εμφάνιση
    }

    public void show() {
        if (flipped) return;
        flipped = true;

        // Εφέ fade για εμφάνιση
        FadeTransition ft = new FadeTransition(Duration.millis(200), this);
        ft.setOnFinished(e -> setGraphic(frontView));
        ft.play();
    }

    public void hide() {
        if (!flipped) return;
        flipped = false;

        // Εφέ fade για απόκρυψη
        FadeTransition ft = new FadeTransition(Duration.millis(200), this);
        ft.setOnFinished(e -> setGraphic(backView));
        ft.play();
    }

    public boolean isFlipped() {
        return flipped;
    }

    public Image getImage() {
        return frontImage;
    }
}
