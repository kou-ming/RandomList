package com.example.randomlist;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyEvent;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Scanner;

public class EditController implements Initializable{
    @FXML
    private Button bt_random;

    @FXML
    private Button bt_pick10;

    @FXML
    private Button chscene_main;

    @FXML
    private TextField txt_song_amount;

    @FXML
    private Slider sld_song_amount;

    @FXML
    private TableColumn<Song, String> SongName;

    @FXML
    private TableView<Song> SongTableView;

    @FXML
    private TextArea Song_Info;

    public ObservableList<Song> songlist = FileController.List;


    @FXML
    void random_playlist(MouseEvent event) {
        ObservableList<Song> temp = FXCollections.observableArrayList();
        int num = songlist.size();
        Random random = new Random();
        Scanner input = new Scanner(System.in);

        //隨機排序不重複歌曲
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

        //清理原歌單，將修改過後的歌單內容放進原歌單中
        int size = temp.size();
        songlist.clear();
        for(int i = 0 ;i < size ; i++){
            songlist.add(temp.get(i));
        }
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

    @FXML
    void pick10_playlist(MouseEvent event) {
        ObservableList<Song> temp = FXCollections.observableArrayList();
        int num = songlist.size();
        int song_amount = Integer.parseInt(txt_song_amount.getText());
        System.out.println(song_amount);
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
            if (temp.size() == song_amount) {
                break;
            }
        }

        int temp_size = temp.size();
        songlist.clear();
        for(int i = 0 ; i < temp_size ; i++){
            songlist.add(temp.get(i));
        }
    }

    @FXML
    void sld_song_amount_change(KeyEvent event) {
        System.out.println("wow");
        int song_amount = Integer.parseInt(txt_song_amount.getText());
        sld_song_amount.setValue(song_amount);
    }

    @FXML
    void txt_song_amount_change_mouse(MouseEvent event) {
        System.out.println("hello");
        int song_amount = (int) sld_song_amount.getValue();
        txt_song_amount.setText(String.valueOf(song_amount));
    }

    @FXML
    void txt_song_amount_change_key(KeyEvent event) {
        System.out.println("hello");
        int song_amount = (int) sld_song_amount.getValue();
        txt_song_amount.setText(String.valueOf(song_amount));
    }

    @FXML
    void get_Info(MouseEvent event) {
        Song songinfo = SongTableView.getSelectionModel().getSelectedItem();    //取得哥死資訊
        Song_Info.clear();
        Song_Info.appendText(songinfo.getName() + "\n");
        Song_Info.appendText(songinfo.getChannel() + "\n");
        Song_Info.appendText(songinfo.getDuration() + "\n");
        Song_Info.appendText(songinfo.getLink() + "\n");
        Song_Info.appendText("標籤：");
        for(int i = 0 ; i < songinfo.getLabelsize() ; i++){
            Song_Info.appendText(" " + songinfo.getLabel(i));
        }
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        //初始化表格
        SongName.setSortable(false);
        SongName.setCellValueFactory(new PropertyValueFactory<>("name"));
        SongTableView.setItems(songlist);
    }
}