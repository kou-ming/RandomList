package com.example.randomlist;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class EditController {
    @FXML
    private Button bt_random;

    @FXML
    private Button bt_pick10;

    @FXML
    private Button chscene_main;

    //public ArrayList <Song> songlist = HelloController.songslist;
    public ObservableList<Song> songlist = HelloController.List;

    @FXML
    void random_playlist(MouseEvent event) {
        ObservableList<Song> temp = FXCollections.observableArrayList();
        int num = songlist.size();
        Random random = new Random();
        Scanner input = new Scanner(System.in);

        while (true) {
            boolean repeat = false;
            int rand_num = random.nextInt(num);
            for (int i = 0; i < temp.size(); i++) {
                if (temp.get(i).equals(songlist.get(rand_num))) {
                    repeat = true;
                    break;
                }
            }
            if (!repeat) {
                temp.add(songlist.get(rand_num));
            }
            if (temp.size() == num) {
                break;
            }
        }

        for (int i = 0; i < temp.size(); i++) {
            //System.out.println(temp.get(i).getName());
        }

        int size = temp.size();
        songlist.clear();
        for(int i = 0 ;i < size ; i++){
            songlist.add(temp.get(i));
        }
    }

    @FXML
    void chscene_main(ActionEvent event) throws IOException {
        //System.out.println("change");
        Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void pick10_playlist(MouseEvent event) {
        ObservableList<Song> temp = FXCollections.observableArrayList();
        int num = songlist.size();
        Random random = new Random();
        Scanner input = new Scanner(System.in);

        while (true) {
            System.out.println("wow");
            boolean repeat = false;
            int rand_num = random.nextInt(num);
            for (int i = 0; i < temp.size(); i++) {
                if (temp.get(i).equals(songlist.get(rand_num))) {
                    repeat = true;
                    break;
                }
            }
            if (!repeat) {
                temp.add(songlist.get(rand_num));
            }
            if (temp.size() == 10) {
                break;
            }
        }

        int temp_size = temp.size();
        songlist.clear();
        for(int i = 0 ; i < temp_size ; i++){
            songlist.add(temp.get(i));
        }
    }
}