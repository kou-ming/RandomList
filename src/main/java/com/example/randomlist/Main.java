package com.example.randomlist;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application{

    public static Stage  primaryStage;
    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("file_Scene.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1285, 648);
        stage.setTitle("RandomList");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();

        
    }
}
