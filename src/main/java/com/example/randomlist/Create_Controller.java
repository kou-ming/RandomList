package com.example.randomlist;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class Create_Controller implements Initializable {

    @FXML
    private ToggleGroup Editor;

    @FXML
    private RadioButton Editor1;

    @FXML
    private RadioButton Editor2;


    //第一份歌單(編輯中)
    @FXML
    private TableView<Song> SongTableView;

    @FXML
    private TableColumn<Song, String> ListName;

    @FXML
    private TableColumn<Song, String> WriterName;

    @FXML
    private TableColumn<Song, Integer> Pref;


    //第二份歌單(引入的)
    @FXML
    private TableView<Song> Other_SongTableView;

    @FXML
    private TableColumn<Song, String> Other_ListName;

    @FXML
    private TableColumn<Song, String> Other_WriterName;

    @FXML
    private TableColumn<Song, Integer> Other_Pref;


    @FXML
    private TextArea Song_Info;

    @FXML
    private Label songInfo_label;

    public String now_list_path = FileController.now_list_path;



    public ObservableList<Song> first_songlist = FileController.List;  //原歌單

    public Map<String, String> User_Map = FileController.User_Map;
    public ObservableList<Song> second_songlist = FXCollections.observableArrayList();  //引入的歌單

    Song temp_song = null;
    private String editor = "";
    FileChooser fileChooser = new FileChooser();    //建立檔案選擇器


    @FXML
    void open_outsideList(MouseEvent event) {
        second_songlist.clear();
        String path = "c:";
        System.out.println("openList");
        File file = fileChooser.showOpenDialog(new Stage());    //可以在頁面選擇檔案位置
        if (file != null) {
            path = file.getAbsolutePath();  //若檔案不為空則重設路徑
            readFile(path);
        }

    }

    @FXML
    void add_outsideList(MouseEvent event) {
        System.out.println("addList");
        String path = "c:";
        File file = fileChooser.showOpenDialog(new Stage());    //可以在頁面選擇檔案位置
        if (file != null) {
            path = file.getAbsolutePath();  //若檔案不為空則重設路徑
            readFile(path);
        }
    }

    @FXML
    void bt_editor1(MouseEvent event) {
        editor = "薛耀智";
    }

    @FXML
    void bt_editor2(MouseEvent event) {
        editor = "許高銘";
    }

    @FXML
    void clearAll(MouseEvent event) {
        first_songlist.clear();
    }

    @FXML
    void get_Info(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            Song songinfo = SongTableView.getSelectionModel().getSelectedItem(); //取得歌曲資訊
            display_songinfo(songinfo);
            Song_Info.getStyleClass().remove("text-area_v1");
            Song_Info.getStyleClass().add("text-area_v1");
            Song_Info.getStyleClass().remove("text-area_v2");
            songInfo_label.getStyleClass().remove("songInfo_label_v1");
            songInfo_label.getStyleClass().add("songInfo_label_v1");
            songInfo_label.getStyleClass().remove("songInfo_label_v2");
        }
    }
    @FXML
    void get_Info_v2(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            Song songinfo = Other_SongTableView.getSelectionModel().getSelectedItem(); //取得歌曲資訊
            temp_song = songinfo;
            display_songinfo(songinfo);
            Song_Info.getStyleClass().remove("text-area_v2");
            Song_Info.getStyleClass().add("text-area_v2");
            Song_Info.getStyleClass().remove("text-area_v1");
            songInfo_label.getStyleClass().remove("songInfo_label_v2");
            songInfo_label.getStyleClass().add("songInfo_label_v2");
            songInfo_label.getStyleClass().remove("songInfo_label_v1");
        }
    }

    @FXML
    void open_localList(MouseEvent event) {

    }

    @FXML
    void add_All(MouseEvent event) {
        if(!editor.equals("")){
            for(int i = 0; i < second_songlist.size() ; i++){
                check_and_add(second_songlist.get(i), editor);
            }
        }
    }

    @FXML
    void add_Some(MouseEvent event) {
        if(!editor.equals("")){
            check_and_add(temp_song, editor);
        }
    }

    void check_and_add(Song song, String now_editor){
        boolean flag = true;
        if(song != null){
            for(int i  = 0 ; i < first_songlist.size() ; i++){
                if (first_songlist.get(i).getLink().equals(song.getLink())){
                    flag = false;
                    break;
                }
            }
            if(flag){
                Song new_song = song.clone();
                if(new_song.getOwner().equals("")){
                    new_song.setOwner(now_editor);
                }
                first_songlist.add(new_song);
            }
        }
    }
    @FXML
    void del(MouseEvent event) {

    }


    //讀取檔案
    void readFile(String path){
        try{
            BufferedReader reader = null;
            String line = "";
            int index_line = -1; //目前讀到第幾列
            try{
                reader = new BufferedReader(new FileReader(path));

                //一次讀一列
                while((line = reader.readLine()) != null){

                    String[] row = line.split("\t");
                    if(index_line >= 0){
                        String ownerlname = check_owner(row[1]);
                        if(!ownerlname.equals("")){
                            Song song = new Song(row[0], row[1].replace(ownerlname, ""),row[2],row[3]);
                            song.setOwner(User_Map.get(ownerlname));
                            second_songlist.removeAll(song);
                            second_songlist.add(song);
                        }
                        else{
                            second_songlist.add(new Song(row[0], row[1],row[2],row[3]));
                        }
                    }

                    if(row.length >= 5){
                        second_songlist.get(index_line).setPreference(Integer.parseInt(row[4]));
                        for (int i = 5 ; i < row.length ; i++){
                            second_songlist.get(index_line).addLabel(row[i]);
                        }
                    }

                    index_line += 1;
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally{
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    String check_owner(String name){
        for(String user : User_Map.keySet()){
            if(name.matches("(.*)" + user)){
                return user;
            }
        }
        return "";
    }

    void display_songinfo(Song songinfo){
        Song_Info.clear();
        Song_Info.appendText("歌名: " + songinfo.getName() + "\n");
        Song_Info.appendText("歌曲連結: " + songinfo.getLink() + "\n");
        Song_Info.appendText("頻道名稱: " + songinfo.getChannel() + "\n");
        Song_Info.appendText("歌曲長度: " + songinfo.getDuration() + "\n");
        Song_Info.appendText("添加者: " + songinfo.getOwner() + "\n");
        Song_Info.appendText("偏好: " + songinfo.getPreference() + "\n");
        Song_Info.appendText("標籤：");
        for(int i = 0 ; i < songinfo.getLabelsize() ; i++){
            Song_Info.appendText(" " + songinfo.getLabel(i));
        }
    }
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //初始化表格
        ListName.setSortable(false);
        ListName.setCellValueFactory(new PropertyValueFactory<>("name"));
        WriterName.setCellValueFactory(new PropertyValueFactory<>("owner"));
        WriterName.setCellFactory(new Callback<TableColumn<Song, String>, TableCell<Song, String>>() {
            @Override
            public TableCell<Song, String> call(TableColumn<Song, String> param) {
                TableCell<Song, String> cell = new TableCell<Song, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(item);
                        }
                    }
                };
                cell.setAlignment(Pos.CENTER); // 设置单元格内容居中
                return cell;
            }
        });

        Pref.setCellValueFactory(new PropertyValueFactory<>("preference"));
        Pref.setCellFactory(new Callback<TableColumn<Song, Integer>, TableCell<Song, Integer>>() {
            @Override
            public TableCell<Song, Integer> call(TableColumn<Song, Integer> param) {
                TableCell<Song, Integer> cell = new TableCell<Song, Integer>() {
                    @Override
                    protected void updateItem(Integer item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(String.valueOf(item));
                        }
                    }
                };
                cell.setAlignment(Pos.CENTER); // 设置单元格内容居中
                return cell;
            }
        });

        ListName.setText(FileController.listname);
        SongTableView.setItems(first_songlist);

        //初始化引入歌單的表格
        Other_ListName.setSortable(false);
        Other_ListName.setCellValueFactory(new PropertyValueFactory<>("name"));
        Other_WriterName.setCellValueFactory(new PropertyValueFactory<>("owner"));
        Other_WriterName.setCellFactory(new Callback<TableColumn<Song, String>, TableCell<Song, String>>() {
            @Override
            public TableCell<Song, String> call(TableColumn<Song, String> param) {
                TableCell<Song, String> cell = new TableCell<Song, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(item);
                        }
                    }
                };
                cell.setAlignment(Pos.CENTER); // 设置单元格内容居中
                return cell;
            }
        });

        Other_Pref.setCellValueFactory(new PropertyValueFactory<>("preference"));
        Other_Pref.setCellFactory(new Callback<TableColumn<Song, Integer>, TableCell<Song, Integer>>() {
            @Override
            public TableCell<Song, Integer> call(TableColumn<Song, Integer> param) {
                TableCell<Song, Integer> cell = new TableCell<Song, Integer>() {
                    @Override
                    protected void updateItem(Integer item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(String.valueOf(item));
                        }
                    }
                };
                cell.setAlignment(Pos.CENTER); // 设置单元格内容居中
                return cell;
            }
        });

        Other_SongTableView.setItems(second_songlist);
    }
}