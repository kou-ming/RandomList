package com.example.randomlist;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.*;

import java.io.IOException;

public class Test extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("file_Scene.fxml"));
        Pane rootpane = fxmlLoader.load();
        Button button = new Button("Click me");
        button.setLayoutX(100);
        button.setLayoutY(100);

        rootpane.getChildren().add(button);
        Scene scene = new Scene(rootpane, 810, 540);
        stage.setTitle("Test What you what");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
