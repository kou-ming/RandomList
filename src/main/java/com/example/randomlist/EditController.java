package com.example.randomlist;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class EditController {
    @FXML
    private Button bt_random;

    @FXML
    private Button chscene_main;

    public ArrayList<Song> songlist = HelloController.songslist;
    ObservableList<Song> Songlist = HelloController.List;
//    Songlist = HelloController.List;

    @FXML
    void random_playlist(MouseEvent event) {

        System.out.println("wow");
        Songlist.remove(0);
        System.out.println(Songlist.get(0).getName());

    }

    @FXML
    void chscene_main(ActionEvent event)  throws IOException {
        System.out.println("change_to_mainScene");
        Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}

