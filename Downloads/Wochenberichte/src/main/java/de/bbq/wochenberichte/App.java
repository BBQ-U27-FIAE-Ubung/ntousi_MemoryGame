package de.bbq.wochenberichte;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.stage.Screen;

/**
 * JavaFX App
 */
public class App extends Application {

    public static Scene scene;
    public static double hoeheScene;
    public static PrimaryController primary;

    @Override
    public void start(Stage stage) throws IOException {
        double seitenverhaeltnis = 1.414;
        hoeheScene = Screen.getPrimary().getVisualBounds().getHeight() - 50;
        double breite = hoeheScene / seitenverhaeltnis;
        
        stage.setResizable(false);
        
        scene = new Scene(loadFXML("primary"), breite, hoeheScene);
        stage.setScene(scene);
        stage.setTitle("IHK Wochenberichte");
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        Parent p = fxmlLoader.load();
        if (fxml.equals("primary")) {
            primary = fxmlLoader.getController();
        }
        return p;
    }

    public static void main(String[] args) {
        launch();
    }

}