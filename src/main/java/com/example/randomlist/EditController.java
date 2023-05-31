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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.*;

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
    private RadioButton Editor3;

    @FXML
    private RadioButton Editor4;

    @FXML
    private Button bt_random;

    @FXML
    private Button bt_pick10;

    @FXML
    private Button bt_chscene_main;

    @FXML
    private Button bt_save_changes;

    @FXML
    private Button bt_add_song;

    @FXML
    private Button bt_edit_song;

    @FXML
    private Button bt_delete_song;

    @FXML
    private Button bt_add_label;

    @FXML
    private Button bt_delete_label;

    @FXML
    private Button bt_song_preference;

    @FXML
    private Button bt_song_preference1;

    @FXML
    private Button bt_song_preference2;

    @FXML
    private Button bt_song_preference3;


    @FXML
    private RadioButton bt_only_random;

    @FXML
    private RadioButton bt_random_and_preference;

    @FXML
    private RadioButton bt_random_and_owner;

    @FXML
    private RadioButton bt_random_and_time;

    @FXML
    private Button bt_open_close_labels;

    @FXML
    private Button bt_open_close_adder;


    @FXML
    private VBox song_buttons;

    @FXML
    private ScrollPane song_add_label_buttons_pane;

    @FXML
    private VBox song_add_label_buttons;

    @FXML
    private ScrollPane song_delete_label_buttons_pane;

    @FXML
    private ScrollPane labels_pane;

    @FXML
    private VBox labels;

    @FXML
    private ScrollPane adder_pane;

    @FXML
    private VBox adder;

    @FXML
    private VBox song_delete_label_buttons;

    @FXML
    private VBox song_preference_buttons;

    @FXML
    private TextField txt_song_amount;

    @FXML
    private TextField txt_hour;

    @FXML
    private TextField txt_minute;

    @FXML
    private TextField txt_second;


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
    public ArrayList<Song> ori_songlist = new ArrayList<>();

    private String editor;

    private Song songinfo;

    private int table_x, table_y;

    private Song song_selected;



    public Map<String, String> User_to_Youtube = FileController.User_to_Youtube;
    private void nonrepeat_random_sublist(int sublist_size){
        ObservableList<Song> temp = FXCollections.observableArrayList();
        Random random = new Random();
        int num = songlist.size();

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
            if (temp.size() == sublist_size) {
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

    private void show_song_detail(){
        Song_Info.clear();
        Song_Info.appendText("歌名：" + songinfo.getName() + "\n");
        Song_Info.appendText("歌曲連結：" + songinfo.getLink() + "\n");
        Song_Info.appendText("頻道名稱：" + songinfo.getChannel() + "\n");
        Song_Info.appendText("歌曲長度：" + songinfo.getDuration() + "\n");
        Song_Info.appendText("添加者：" + songinfo.getOwner() + "\n");
        Song_Info.appendText("偏好：" + songinfo.getPreference() + "\n");
        Song_Info.appendText("標籤：");
        for(int i = 0 ; i < songinfo.getLabelsize() ; i++){
            Song_Info.appendText( songinfo.getLabel(i) + "  ");
        }
    }

    private void count_list_time(){
        int total_second = 0;
        for (int i = 0; i < songlist.size(); i++) {
            String temp_ = songlist.get(i).getDuration();
            temp_ = temp_.replaceAll(" ","");
            String [] time = temp_.split(":");
            if (time.length == 2){
                int minute_ = Integer.parseInt(time[0]);
                int second_ = Integer.parseInt(time[1]);
                total_second += minute_ * 60 + second_;
            }
            //System.out.println(songlist.get(i).getLink());
        }

        int hour = total_second / 3600;
        int minute = (total_second % 3600) / 60;
        int second = total_second % 60;
        System.out.println(hour + " " + minute + " " + second);
        txt_hour.setText(String.valueOf(hour));
        txt_minute.setText(String.valueOf(minute));
        txt_second.setText(String.valueOf(second));
    }

    @FXML
    void random_playlist(MouseEvent event) {
        int song_amount = songlist.size();
        nonrepeat_random_sublist(song_amount);
    }

    @FXML
    void chscene_main(ActionEvent event) throws IOException {
        songlist.clear();
        for(Song song : ori_songlist){
            songlist.add(song);
        }
        System.out.println("change");
        Parent root = FXMLLoader.load(getClass().getResource("file_Scene.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void save_changes(MouseEvent event) throws IOException {
        File file = new File(FileController.now_list_path);
        try {
            PrintWriter printWriter = new PrintWriter(file);
            printWriter.write("<Name>	<Channel>	<Duration>	<URL>\n");
            for(int i = 0; i < songlist.size() ; i++){
                printWriter.write(songlist.get(i).getName() + "\t" + songlist.get(i).getChannel());
                if(!songlist.get(i).getOwner().equals("")){
                    printWriter.write(User_to_Youtube.get(songlist.get(i).getOwner()));
                }
                printWriter.write("\t" + songlist.get(i).getDuration() + "\t" + songlist.get(i).getLink() + "\t" + songlist.get(i).getPreference());

                for(int j = 0 ; j < songlist.get(i).getLabelsize() ; j++){
                    printWriter.write("\t" + songlist.get(i).getLabel(j));
                }
                printWriter.write("\n");
            }
            printWriter.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

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
        if (bt_only_random.isSelected()){
            int song_amount = Integer.parseInt(txt_song_amount.getText());
            nonrepeat_random_sublist(song_amount);
        }
        else if (bt_random_and_preference.isSelected()) {
            Random random = new Random();
            ObservableList<Song> temp_final = FXCollections.observableArrayList();
            ObservableList<Song> temp_1 = FXCollections.observableArrayList();
            ObservableList<Song> temp_2 = FXCollections.observableArrayList();
            ObservableList<Song> temp_3 = FXCollections.observableArrayList();
            int song_amount = Integer.parseInt(txt_song_amount.getText());
            int x, y;
            //計算有幾輪的十首歌
            if (song_amount % 10 == 0){
                x = song_amount / 10;
                y = 10;
            }
            else{
                x = song_amount / 10 + 1;
                y = song_amount % 10;
            }

            //將不同偏好值的歌分類
            for (int i = 0; i < songlist.size(); i++) {
                if (songlist.get(i).getPreference() == 1){
                    temp_1.add(songlist.get(i));
                }
                else if (songlist.get(i).getPreference() == 2){
                    temp_2.add(songlist.get(i));
                }
                else if (songlist.get(i).getPreference() == 3){
                    temp_3.add(songlist.get(i));
                }
            }

            for (int i = 0; i < x; i++) {
                ObservableList<Song> temp = FXCollections.observableArrayList();
                int range = 10;
                if (i == x - 1){
                    range = y;
                }

                //根據不同偏好值調整控制歌曲數量和優先度
                for (int j = 0; j < range; j++) {
                    if (j >= 8){
                        temp.add(temp_1.get(0));
                        temp_1.remove(0);
                    }
                    else if (j >= 5) {
                        if (temp_2.size() != 0) {
                            temp.add(temp_2.get(0));
                            temp_2.remove(0);
                        }
                        else {
                            temp.add(temp_1.get(0));
                            temp_1.remove(0);
                        }
                    }
                    else if (j >= 0) {
                        if (temp_3.size() != 0){
                            temp.add(temp_3.get(0));
                            temp_3.remove(0);
                        }
                        else if (temp_2.size() != 0) {
                            temp.add(temp_2.get(0));
                            temp_2.remove(0);
                        }
                        else {
                            temp.add(temp_1.get(0));
                            temp_1.remove(0);
                        }
                    }
                }

                //隨機排序不重複歌曲
                int count = 0;
                int num = temp.size();
                while (true){
                    boolean repeat = false;
                    int rand_num = random.nextInt(num);
                    for (int k = 0; k < temp_final.size(); k++) {
                        if (temp_final.get(k).equals(temp.get(rand_num))) {
                            repeat = true;
                            break;
                        }
                    }
                    if (!repeat) {
                        count++;
                        temp_final.add(temp.get(rand_num));
                    }
                    if (count == range) {
                        break;
                    }
                }
            }

            //清理原歌單，將修改過後的歌單內容放進原歌單中
            int temp_final_size = temp_final.size();
            songlist.clear();
            for(int i = 0 ; i < temp_final_size ; i++){
                songlist.add(temp_final.get(i));
            }
        }
        else if (bt_random_and_owner.isSelected()) {
            int song_amount = Integer.parseInt(txt_song_amount.getText()), count = 0, song_added = 0;
            ArrayList<Song> temp1 = new ArrayList<>();
            ArrayList<Song> temp2 = new ArrayList<>();
            ArrayList<Song> temp3 = new ArrayList<>();
            ArrayList<Song> temp4 = new ArrayList<>();
            ArrayList<Song> temp_final = new ArrayList<>();

            nonrepeat_random_sublist(songlist.size());
            for (int i = 0; i < songlist.size(); i++) {
                if (songlist.get(i).getOwner().equals("薛耀智")){
                    temp1.add(songlist.get(i));
                }
                else if (songlist.get(i).getOwner().equals("許高銘")){
                    temp2.add(songlist.get(i));
                }
                else if (songlist.get(i).getOwner().equals("王文和")){
                    temp3.add(songlist.get(i));
                }
                else if (songlist.get(i).getOwner().equals("鍾君逸")){
                    temp4.add(songlist.get(i));
                }
            }

            while (true){
                if (count % 4 == 0 && temp1.size() != 0){
                    temp_final.add(temp1.get(0));
                    temp1.remove(0);
                    song_added ++;
                }
                else if (count % 4 == 1 && temp2.size() != 0){
                    temp_final.add(temp2.get(0));
                    temp2.remove(0);
                    song_added ++;
                }
                else if (count % 4 == 2 && temp3.size() != 0){
                    temp_final.add(temp3.get(0));
                    temp3.remove(0);
                    song_added ++;
                }
                else if (count % 4 == 3 && temp4.size() != 0){
                    temp_final.add(temp4.get(0));
                    temp4.remove(0);
                    song_added ++;
                }

                count ++;

                if (song_added == song_amount){
                    break;
                }
            }

            songlist.clear();
            for (int i = 0; i < temp_final.size(); i++) {
                songlist.add(temp_final.get(i));
                System.out.println(temp_final.get(i).getOwner());
            }
            nonrepeat_random_sublist(song_amount);
        }
        else if (bt_random_and_time.isSelected()) {
            int hour = Integer.parseInt(txt_hour.getText());
            int minute = Integer.parseInt(txt_minute.getText());
            int second = Integer.parseInt(txt_second.getText());
            int total_second = hour * 3600 + minute * 60 + second;
            int count_second = 0;

            ArrayList<Song> temp = new ArrayList<>();
            Random random = new Random();
            int num = songlist.size();

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

                    String temp_ = songlist.get(rand_num).getDuration();
                    temp_ = temp_.replaceAll(" ","");
                    String [] time = temp_.split(":");
                    if (time.length == 2){
                        int minute_ = Integer.parseInt(time[0]);
                        int second_ = Integer.parseInt(time[1]);
                        count_second += minute_ * 60 + second_;
                    }
                }

                if (count_second >= total_second){
                    System.out.println(count_second);
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

        //設定拉桿長度最大值和文字顯示
        sld_song_amount.setMax(songlist.size());
        txt_song_amount.setText(String.valueOf(songlist.size()));

        //設定歌單總時間長度
        count_list_time();

        for (int i = 0; i < songlist.size(); i++) {
            System.out.println(songlist.get(i).getLink());
        }
    }

    @FXML
    void only_random(MouseEvent event) {

    }

    @FXML
    void random_and_preference(MouseEvent event) {

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

    private double mouse_x, mouse_y;
    @FXML
    void get_Info(MouseEvent event) {
        //顯示已選取的歌曲的資訊
        songinfo = SongTableView.getSelectionModel().getSelectedItem(); //取得歌曲資訊
        song_selected = SongTableView.getSelectionModel().getSelectedItem(); //取得歌曲資訊
        System.out.println(songinfo.getName());
        show_song_detail();
        song_buttons.setVisible(false);
        song_preference_buttons.setVisible(false);
        song_add_label_buttons_pane.setVisible(false);
        song_delete_label_buttons_pane.setVisible(false);

        //右鍵顯示按鈕列
        if (event.getButton() == MouseButton.SECONDARY) {
            System.out.println(song_selected.getName());
            mouse_x = event.getX();
            mouse_y = event.getY();

            song_buttons.setTranslateX(mouse_x + table_x);
            song_buttons.setTranslateY(mouse_y + table_y);
            song_buttons.setVisible(true);
            song_preference_buttons.setVisible(false);
            song_add_label_buttons_pane.setVisible(false);
            song_delete_label_buttons_pane.setVisible(false);

            //create_delete_label_buttons();
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
            songlist.add(0, song);

            txt_add_song_name.setText("");
            txt_add_song_link.setText("");
            txt_add_song_artist.setText("");
            txt_add_song_length.setText("");
        }

        sld_song_amount.setMax(songlist.size());
        txt_song_amount.setText(String.valueOf(songlist.size()));
        count_list_time();
    }

    //左鍵點擊按鈕編輯已選取的歌曲資訊
    @FXML
    void edit_song(MouseEvent event) {
        if (!txt_add_song_name.getText().equals("")){
            String song_name = txt_add_song_name.getText();
            song_selected.setName(song_name);
        }
        if (!txt_add_song_link.getText().equals("")){
            String song_link = txt_add_song_link.getText();
            song_selected.setLink(song_link);
        }
        if (!txt_add_song_artist.getText().equals("")){
            String song_artist = txt_add_song_artist.getText();
            song_selected.setChannel(song_artist);
        }
        if (!txt_add_song_length.getText().equals("")){
            String song_length = txt_add_song_length.getText();
            song_selected.setDuration(song_length);
        }

        count_list_time();
        show_song_detail();
        txt_add_song_name.setText("");
        txt_add_song_link.setText("");
        txt_add_song_artist.setText("");
        txt_add_song_length.setText("");
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
            txt_song_amount.setText(String.valueOf(songlist.size()));
            count_list_time();
            song_buttons.setVisible(false);
            song_preference_buttons.setVisible(false);
            song_add_label_buttons_pane.setVisible(false);
        }
    }

    @FXML
    void enter_bt_add_label(MouseEvent event) {
        song_add_label_buttons_pane.setTranslateX(mouse_x + table_x);
        song_add_label_buttons_pane.setTranslateY(mouse_y + table_y);

        song_add_label_buttons_pane.setVisible(true);
        song_delete_label_buttons_pane.setVisible(false);

    }

    @FXML
    void enter_bt_delete_label(MouseEvent event) {
        song_delete_label_buttons_pane.setTranslateX(mouse_x + table_x);
        song_delete_label_buttons_pane.setTranslateY(mouse_y + table_y);

        if (song_selected.SongLabels.size() != 0){
            song_delete_label_buttons_pane.setVisible(true);
        }

        song_delete_label_buttons.getChildren().clear();
        for (int i = 0; i < song_selected.SongLabels.size(); i++) {
            System.out.println(song_selected.SongLabels.get(i));

            Button button = new Button(song_selected.getLabel(i));
            button.setPrefSize(110, 36);

            button.setOnAction(e -> {
                for (int j = 0; j < song_selected.SongLabels.size(); j++) {
                    if (song_selected.getLabel(j).equals(button.getText())) {
                        song_selected.SongLabels.remove(j);
                        break;
                    }
                }
                if (song_selected.SongLabels.size() == 0) {
                    song_delete_label_buttons_pane.setVisible(false);
                }

                song_delete_label_buttons.getChildren().remove(button);
                show_song_detail();
            });

            song_delete_label_buttons.getChildren().add(button);
        }
        song_add_label_buttons_pane.setVisible(false);
        song_preference_buttons.setVisible(false);
    }

    //游標進入按鈕展開歌曲偏好按鈕列
    @FXML
    void song_preference(MouseEvent event) {
        //if (event.getButton() == MouseButton.PRIMARY){
            song_preference_buttons.setTranslateX(mouse_x + table_x);
            song_preference_buttons.setTranslateY(mouse_y + table_y);
            song_preference_buttons.setVisible(true);
            song_delete_label_buttons_pane.setVisible(false);

        //}
    }

    @FXML
    void hide_song_preference(MouseEvent event) {
        song_preference_buttons.setVisible(false);
    }

    @FXML
    void song_preference1_selected(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            song_selected.setPreference(1);
            song_preference_buttons.setVisible(false);
            show_song_detail();
        }
    }

    @FXML
    void song_preference2_selected(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            song_selected.setPreference(2);
            song_preference_buttons.setVisible(false);
            show_song_detail();
        }
    }

    @FXML
    void song_preference3_selected(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            song_selected.setPreference(3);
            song_preference_buttons.setVisible(false);
            show_song_detail();
        }
    }

    @FXML
    void bt_editor1(MouseEvent event) {
        editor = Editor1.getText();
    }

    @FXML
    void bt_editor2(MouseEvent event) {
        editor = Editor2.getText();
    }

    @FXML
    void bt_editor3(MouseEvent event) {
        editor = Editor3.getText();
    }

    @FXML
    void bt_editor4(MouseEvent event) {
        editor = Editor4.getText();
    }

    @FXML
    void main_plane_clicked(MouseEvent event) {
        song_buttons.setVisible(false);
        song_preference_buttons.setVisible(false);
        song_add_label_buttons_pane.setVisible(false);
        song_delete_label_buttons_pane.setVisible(false);
    }

    @FXML
    void open_close_labels(MouseEvent event) {
        if (labels_pane.isVisible()){
            labels_pane.setVisible(false);
        }
        else {
            labels_pane.setVisible(true);
        }
    }

    @FXML
    void open_close_adder(MouseEvent event) {
        if (adder_pane.isVisible()){
            adder_pane.setVisible(false);
        }
        else{
            adder_pane.setVisible(true);
        }
    }

    private ArrayList<String> labels_selected = new ArrayList<>();

    private ArrayList<String> adder_selected = new ArrayList<>();

    private ArrayList<Song> full_songlist = new ArrayList<>();

    public void initialize(URL url, ResourceBundle resourceBundle) {
        for(Song song : songlist){
            ori_songlist.add(song.clone());
        }
        for (int i = 0; i < songlist.size(); i++) {
            full_songlist.add(songlist.get(i));
        }
        editor = FileController.editor;
        if (editor.equals("薛耀智")){
            Editor1.setSelected(true);
        }
        else if (editor.equals("許高銘")){
            Editor2.setSelected(true);
        }

        for (int i = 0; i < songlist.size(); i++) {
            if (songlist.get(i).getOwner().equals("")){
                songlist.get(i).setOwner(editor);
            }
        }

        //初始化表格
        SongName.setSortable(false);
        SongName.setCellValueFactory(new PropertyValueFactory<>("name"));
        SongName.setText(FileController.listname);
        SongTableView.setItems(songlist);
        table_x = (int) SongTableView.getLayoutX();
        table_y = (int) SongTableView.getLayoutY();

        //初始化文字顯示和拉桿
        txt_song_amount.setText(String.valueOf(songlist.size()));
        sld_song_amount.setMax(songlist.size());
        sld_song_amount.setValue(songlist.size());

        //初始化時間
        count_list_time();

        for (int i = 0; i < FileController.Labels.size(); i++) {
            //讀取所有的標籤並建立按鈕
            Button button = new Button(FileController.Labels.get(i));
            button.setPrefSize(110, 36);

            button.setOnAction(e -> {
                boolean repeat = false;
                for (int j = 0; j < song_selected.SongLabels.size(); j++) {
                    if (song_selected.getLabel(j).equals(button.getText())){
                        repeat = true;
                        break;
                    }
                }

                if (!repeat){
                    song_selected.addLabel(button.getText());
                    show_song_detail();
                }
            });

            song_add_label_buttons.getChildren().add(button);

            //建立所有標籤的選取按鈕
            CheckBox checkBox = new CheckBox(FileController.Labels.get(i));
            checkBox.setPrefSize(150,36);

            checkBox.setOnAction(e -> {
                labels_selected.clear();
                for (javafx.scene.Node node : labels.getChildren()){
                    if (node instanceof CheckBox) {
                        CheckBox current_check_box = (CheckBox) node;
                        if (current_check_box.isSelected()){
                            labels_selected.add(current_check_box.getText());
                        }
                    }
                }

                ArrayList<Song> temp = new ArrayList<>();
                if (labels_selected.size() == 0){
                    for (int j = 0; j < full_songlist.size(); j++) {
                        temp.add(full_songlist.get(j));
                    }
                }
                else{
                    for (int j = 0; j < full_songlist.size(); j++) {
                        for (int k = 0; k < labels_selected.size(); k++) {
                            if (full_songlist.get(j).check_label(labels_selected.get(k))){
                                temp.add(full_songlist.get(j));
                                break;
                            }
                        }
                    }
                }

                songlist.clear();
                for (int j = 0; j < temp.size(); j++) {
                    songlist.add(temp.get(j));
                }

                txt_song_amount.setText(String.valueOf(songlist.size()));
                sld_song_amount.setMax(songlist.size());
                sld_song_amount.setValue(songlist.size());
                count_list_time();
            });
            labels.getChildren().add(checkBox);
        }

        for (int i = 0; i < 4; i++) {
            CheckBox checkBox = new CheckBox();
            checkBox.setPrefSize(150,36);
            checkBox.setSelected(true);
            if (i == 0){
                checkBox.setText(Editor1.getText());
            }
            else if (i == 1) {
                checkBox.setText(Editor2.getText());
            }
            else if (i == 2) {
                checkBox.setText(Editor3.getText());
            }
            else if (i == 3) {
                checkBox.setText(Editor4.getText());
            }

            checkBox.setOnAction(e -> {
                adder_selected.clear();
                for (javafx.scene.Node node : adder.getChildren()){
                    if (node instanceof CheckBox) {
                        CheckBox current_check_box = (CheckBox) node;
                        if (current_check_box.isSelected()){
                            adder_selected.add(current_check_box.getText());
                        }
                    }
                }

                ArrayList<Song> temp = new ArrayList<>();
                if (adder_selected.size() == 4){
                    for (int j = 0; j < full_songlist.size(); j++) {
                        temp.add(full_songlist.get(j));
                    }
                }
                else{
                    for (int j = 0; j < full_songlist.size(); j++) {
                        for (int k = 0; k < adder_selected.size(); k++) {
                            if (full_songlist.get(j).getOwner().equals(adder_selected.get(k))){
                                temp.add(full_songlist.get(j));
                                break;
                            }
                        }
                    }
                }

                songlist.clear();
                for (int j = 0; j < temp.size(); j++) {
                    songlist.add(temp.get(j));
                }

                txt_song_amount.setText(String.valueOf(songlist.size()));
                sld_song_amount.setMax(songlist.size());
                sld_song_amount.setValue(songlist.size());
                count_list_time();
            });

            adder.getChildren().add(checkBox);
        }
    }
}