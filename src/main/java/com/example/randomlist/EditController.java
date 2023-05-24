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
import javafx.scene.input.*;
import javafx.event.ActionEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Scanner;

public class EditController implements Initializable{

    @FXML
    private AnchorPane main_pane;

    @FXML
    private ToggleGroup Editor;

    @FXML
    private RadioButton Editor1;

    @FXML
    private RadioButton Editor2;

    @FXML
    private Button bt_random;

    @FXML
    private Button bt_pick10;

    @FXML
    private Button chscene_main;

    @FXML
    private Button bt_delete_song;

    @FXML
    private Button bt_add_tag;

    @FXML
    private Button bt_song_preference;

    @FXML
    private Button bt_song_preference1;

    @FXML
    private Button bt_song_preference2;

    @FXML
    private Button bt_song_preference3;

    @FXML
    private VBox song_buttons;

    @FXML
    private VBox song_preference_buttons;

    @FXML
    private TextField txt_song_amount;

    @FXML
    private TextField txt_add_song_link;

    @FXML
    private TextField txt_add_song_name;

    @FXML
    private TextField txt_add_song_artist;

    @FXML
    private TextField txt_add_song_length;

    @FXML
    private Slider sld_song_amount;

    @FXML
    private TableColumn<Song, String> SongName;

    @FXML
    private TableView<Song> SongTableView;

    @FXML
    private TextArea Song_Info;

    public ObservableList<Song> songlist = FileController.List;

    private String editor = "薛耀智";

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

    //創建子歌單
    @FXML
    void create_subplaylist(MouseEvent event) {
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

        //設定拉桿長度最大值為歌單長度
        sld_song_amount.setMax(songlist.size());
    }

    //拉桿的值配合文字的值改變
    @FXML
    void sld_song_amount_change(KeyEvent event) {
        System.out.println("wow");
        int song_amount = Integer.parseInt(txt_song_amount.getText());
        sld_song_amount.setValue(song_amount);
    }

    //文字的值配合拉桿的值改變(滑鼠點擊)
    @FXML
    void txt_song_amount_change_mouse(MouseEvent event) {
        System.out.println("hello");
        int song_amount = (int) sld_song_amount.getValue();
        txt_song_amount.setText(String.valueOf(song_amount));
    }

    //文字的值配合拉桿的值改變(左右鍵點擊)
    @FXML
    void txt_song_amount_change_key(KeyEvent event) {
        System.out.println("hello");
        int song_amount = (int) sld_song_amount.getValue();
        txt_song_amount.setText(String.valueOf(song_amount));
    }

    private Song song_selected;
    private double mouse_x, mouse_y;
    @FXML
    void get_Info(MouseEvent event) {
        //左鍵顯示已選取的歌曲的資訊
        if (event.getButton() == MouseButton.PRIMARY) {
            Song songinfo = SongTableView.getSelectionModel().getSelectedItem(); //取得歌曲資訊
            Song_Info.clear();
            Song_Info.appendText(songinfo.getName() + "\n");
            Song_Info.appendText(songinfo.getChannel() + "\n");
            Song_Info.appendText(songinfo.getDuration() + "\n");
            Song_Info.appendText(songinfo.getLink() + "\n");
            Song_Info.appendText("標籤：");
            for(int i = 0 ; i < songinfo.getLabelsize() ; i++){
                Song_Info.appendText(" " + songinfo.getLabel(i));
            }
            song_buttons.setVisible(false);
            song_preference_buttons.setVisible(false);
       }
        //右鍵顯示按鈕列
        else if (event.getButton() == MouseButton.SECONDARY) {
            song_selected = SongTableView.getSelectionModel().getSelectedItem(); //取得歌曲資訊
            mouse_x = event.getX();
            mouse_y = event.getY();
            song_buttons.setTranslateX(mouse_x + 570);
            song_buttons.setTranslateY(mouse_y + 50);
            song_buttons.setVisible(true);
            song_preference_buttons.setVisible(false);
        }
    }

    //左鍵點擊按鈕加入歌曲
    @FXML
    void add_song(MouseEvent event) {
        if (!txt_add_song_name.getText().equals("") && !txt_add_song_link.getText().equals("")){
            String song_name = txt_add_song_name.getText();
            String song_link = txt_add_song_link.getText();
            String song_artist = txt_add_song_artist.getText();
            String song_length = txt_add_song_length.getText();

            //songlist.add(new Song(song_name, song_artist, song_length, song_link));
            Song song = new Song(song_name, song_artist, song_length, song_link);
            song.setOwner(editor);
            System.out.println(editor);
            songlist.add(song);

            txt_add_song_name.setText("");
            txt_add_song_link.setText("");
            txt_add_song_artist.setText("");
            txt_add_song_length.setText("");
        }
    }

    //左鍵點擊按鈕刪除以選取的歌曲
    @FXML
    void delete_song(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            for (int i = 0; i < songlist.size(); i++) {
                if (songlist.get(i).equals(song_selected)) {
                    songlist.remove(i);
                    break;
                }
            }
            sld_song_amount.setMax(songlist.size());
            int song_amount = (int) sld_song_amount.getValue();
            txt_song_amount.setText(String.valueOf(song_amount));
            song_buttons.setVisible(false);
            song_preference_buttons.setVisible(false);
        }
    }

    //左鍵點擊按鈕展開歌曲偏好按鈕列
    @FXML
    void song_preference(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY){
            song_preference_buttons.setTranslateX(mouse_x + 570);
            song_preference_buttons.setTranslateY(mouse_y + 50);
            song_preference_buttons.setVisible(true);
        }
    }


    @FXML
    void song_preference1_selected(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            song_selected.setPreference(1);
            song_preference_buttons.setVisible(false);

            for (int i = 0; i < songlist.size(); i++) {
                System.out.println(songlist.get(i).getPreference());
            }
        }
    }

    @FXML
    void song_preference2_selected(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            song_selected.setPreference(2);
            song_preference_buttons.setVisible(false);

            for (int i = 0; i < songlist.size(); i++) {
                System.out.println(songlist.get(i).getPreference());
            }
        }
    }

    @FXML
    void song_preference3_selected(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            song_selected.setPreference(3);
            song_preference_buttons.setVisible(false);

            for (int i = 0; i < songlist.size(); i++) {
                System.out.println(songlist.get(i).getPreference());
            }
        }
    }

    @FXML
    void bt_editor1(MouseEvent event) {
        editor = "薛耀智";
        System.out.println(editor);
    }

    @FXML
    void bt_editor2(MouseEvent event) {
        editor = "許高銘";
        System.out.println(editor);
    }

    @FXML
    void main_plane_clicked(MouseEvent event) {
        song_buttons.setVisible(false);
        song_preference_buttons.setVisible(false);
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        //初始化表格
        SongName.setSortable(false);
        SongName.setCellValueFactory(new PropertyValueFactory<>("name"));
        SongTableView.setItems(songlist);

        sld_song_amount.setMax(songlist.size());
    }
}