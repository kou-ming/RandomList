package com.example.randomlist;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;

public class EditController {
    @FXML
    private Button bt_random;
    public ArrayList <Song> songlist = HelloController.songslist;

    @FXML
    void random_playlist(MouseEvent event) {

        System.out.println("wow");
        System.out.println(songlist.get(0).getName());

    }

}

