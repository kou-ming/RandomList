package com.example.randomlist;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

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
    }
}