package com.example.psp6blackjack;

import com.example.psp6blackjack.game.BlackJack;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

public class BlackJackApp extends Application {
    private Stage primaryStage;
    private Pane rootLayout;
    @Override
    public void start(Stage primaryStage) throws IOException {
        this.primaryStage=primaryStage;
        this.primaryStage.setTitle("Black Jack");
        showWindow();
    }

    public void showWindow() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(BlackJackApp.class.getResource("scenes/MainScene.fxml"));
            rootLayout = loader.load();
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            InputStream iconStream = getClass().getResourceAsStream("icon/Icon.jfif");
            Image image = new Image(iconStream);
            primaryStage.getIcons().add(image);
            BlackJackController controller = loader.getController();
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        launch();
    }
}