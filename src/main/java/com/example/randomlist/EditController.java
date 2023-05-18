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
    //public ArrayList <Song> songlist = HelloController.songslist;
    public ObservableList<Song> songlist = HelloController.List;

    @FXML
    void random_playlist(MouseEvent event) {

        System.out.println("wow");
        for (int i = 0; i < songlist.size(); i++) {
            System.out.println(songlist.get(i).getName());
        }

        System.out.println();
        System.out.println("我是分隔線");
        System.out.println();

        //ArrayList <Song> temp = new ArrayList<Song>();
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
            System.out.println(temp.get(i).getName());
        }
//        songlist.remove(0);
        for(int i = 0 ;i < songlist.size() ; i++){
            songlist.remove(0);
            songlist.add(temp.get(i));
        }
//        songlist = temp;
//        System.out.println(songlist.get(0).getName());
//        songlist.remove(0);
    }

    @FXML
    void chscene_main(ActionEvent event) throws IOException {
        System.out.println("change");
        Parent root = FXMLLoader.load(getClass().getResource("file_Scene.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}