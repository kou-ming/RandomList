package com.example.randomlist;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Create_Controller {


    @FXML
    private TextField new_ListName;

    public ObservableList<List_Info> List = FileController.ALL_List;
    @FXML
    void create_List(MouseEvent event) {
        System.out.println(new_ListName.getText());

        File file = new File("src\\main\\java\\SongList_File\\File_name.txt");
        List.add(new List_Info((new_ListName.getText() + ".txt")));
        saveSystem(file, List);
    }

    void saveSystem(File file, ObservableList<List_Info> List){
        try {
            PrintWriter printWriter = new PrintWriter(file);
            for(int i = 0 ; i < List.size() ; i++) {
                printWriter.write(List.get(i).getName() + "\n");
            }
            printWriter.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}