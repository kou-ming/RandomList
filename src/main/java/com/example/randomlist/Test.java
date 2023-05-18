package com.example.randomlist;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.*;

import java.io.IOException;

public class Test extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("file_Scene.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 810, 540);
        stage.setTitle("Test What you what");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
        int num;
        ArrayList<Integer> temp = new ArrayList<>();
        Random random = new Random();
        Scanner input = new Scanner(System.in);
        num = input.nextInt();

        while (true) {
            boolean repeat = false;
            int rand_num = random.nextInt(num);
            for (int i = 0; i < temp.size(); i++) {
                if (temp.get(i) == rand_num) {
                    repeat = true;
                    break;
                }
            }
            if (!repeat) {
                temp.add(rand_num);
            }
            if (temp.size() == num) {
                break;
            }
        }

        for (int i = 0; i < temp.size(); i++) {
            System.out.println(temp.get(i));
        }
    }
}
