package com.example.randomlist;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
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

    public ObservableList<List_Info> ALL_List = FileController.ALL_List;  //儲存本地歌單
    public Map<String, String> User_Map = FileController.User_Map;
    public ObservableList<Song> second_songlist = FXCollections.observableArrayList();  //引入的歌單

    public Map<String, String> User_to_Youtube = FileController.User_to_Youtube;
    public ArrayList<Song> ori_songlist = new ArrayList<>();
    Song temp_song = null;

    int now_choose_index = -1;    //原歌單選取的歌
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
            for(int i = 0 ; i < first_songlist.size() ; i++){
                if(first_songlist.get(i).getLink().equals(songinfo.getLink())){
                    now_choose_index = i;
                }
            }
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
    void open_localList(MouseEvent event) throws IOException {
        System.out.println("call a popupScene");

        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initOwner(Main. primaryStage);

        Pane popupRoot = FXMLLoader.load(getClass().getResource("new_list_popupScene.fxml"));


        VBox vbox = new VBox();
        for(int i = 0 ; i < ALL_List.size() ; i++){
            create_listInfo(ALL_List.get(i).getName(), vbox, popupStage);
        }

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefSize(142, 253);
        scrollPane.setTranslateX(170);
        scrollPane.setTranslateY(20);

        Label label = new Label("本\n地\n歌\n單");
        label.setLayoutX(45);
        label.setLayoutY(20);
        label.setFont(new Font(50));
        popupRoot.getChildren().add(label);
        popupRoot.getChildren().add(scrollPane);
        scrollPane.setContent(vbox);
        Scene popupScene = new Scene(popupRoot, 400, 300);
        popupStage.setScene(popupScene);
        popupStage.showAndWait();
    }

    //建立歌單預覽按鈕
    void create_listInfo(String list_name, VBox vBox, Stage popupStage) throws IOException {
        list_name = list_name.replace(".txt", "");
        Button songlist_name = new Button(list_name);
        songlist_name.getStyleClass().add("list_button");

        songlist_name.setPrefSize(125, 50);

        songlist_name.setOnAction(event -> {
            second_songlist.clear();
            String path = "src\\main\\java\\SongList_File\\" + songlist_name.getText() + ".txt";
            readFile(path);
            //closeWindow(popupStage);
        });
        System.out.println(songlist_name.getText());
        vBox.getChildren().add(songlist_name);
    }

    void closeWindow(Stage stage) {
        // 关闭窗口
        stage.close();
    }
    @FXML
    void add_All(MouseEvent event) throws IOException {
        if(!editor.equals("")){
            for(int i = 0; i < second_songlist.size() ; i++){
                check_and_add(second_songlist.get(i), editor);
            }
        }
        else{
            no_editor();
        }
    }

    @FXML
    void add_Some(MouseEvent event) throws IOException {
        if(!editor.equals("")){
            check_and_add(temp_song, editor);
        }
        else{
            no_editor();
        }
    }


    void no_editor() throws IOException {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initOwner(Main.primaryStage);

        Pane popupRoot = FXMLLoader.load(getClass().getResource("new_list_popupScene.fxml"));


        Label label1 = new Label("警告！");
        label1.setLayoutX(130);
        label1.setLayoutY(60);
        label1.setFont(new Font(45));
        label1.getStyleClass().add("error_label");
        Label label2 = new Label("你未選取添加者");
        label2.setLayoutX(35);
        label2.setLayoutY(130);
        label2.setFont(new Font(45));
        label2.getStyleClass().add("error_label");
        popupRoot.getChildren().addAll(label1, label2);
        Scene popupScene = new Scene(popupRoot, 400, 300);
        popupStage.setScene(popupScene);
        popupStage.showAndWait();
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
        if(now_choose_index >= 0){
            first_songlist.remove(first_songlist.get(now_choose_index));
            if (now_choose_index == 0 && first_songlist.size() >= 1){
                now_choose_index = 0;
            }
            else if (now_choose_index == 0 && first_songlist.size() == 0){
                now_choose_index = -1;
            }
            else{
                now_choose_index -= 1;
            }
        }

    }

    @FXML
    void save_changes(MouseEvent event) throws IOException {
        File file = new File(FileController.now_list_path);
        try {
            PrintWriter printWriter = new PrintWriter(file);
            printWriter.write("<Name>	<Channel>	<Duration>	<URL>\n");
            for(int i = 0; i < first_songlist.size() ; i++){
                printWriter.write(first_songlist.get(i).getName() + "\t" + first_songlist.get(i).getChannel());
                if(!first_songlist.get(i).getOwner().equals("")){
                    printWriter.write(User_to_Youtube.get(first_songlist.get(i).getOwner()));
                }
                printWriter.write("\t" + first_songlist.get(i).getDuration() + "\t" + first_songlist.get(i).getLink() + "\t" + first_songlist.get(i).getPreference());

                for(int j = 0 ; j < first_songlist.get(i).getLabelsize() ; j++){
                    printWriter.write("\t" + first_songlist.get(i).getLabel(j));
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

    @FXML
    void chscene_main(MouseEvent event) throws IOException {
        first_songlist.clear();
        for(Song song : ori_songlist){
            first_songlist.add(song);
        }
        System.out.println("change");
        Parent root = FXMLLoader.load(getClass().getResource("file_Scene.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    //讀取第二個歌單的檔案
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
                            Song song = new Song(row[0], row[1].replace(ownerlname, ""),row[2].replaceAll(" ", ""),row[3]);
                            song.setOwner(User_Map.get(ownerlname));
                            second_songlist.removeAll(song);
                            second_songlist.add(song);
                        }
                        else{
                            second_songlist.add(new Song(row[0], row[1],row[2].replaceAll(" ", ""),row[3]));
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
        Song_Info.appendText("歌名：" + songinfo.getName() + "\n");
        Song_Info.appendText("歌曲連結：" + songinfo.getLink() + "\n");
        Song_Info.appendText("頻道名稱：" + songinfo.getChannel() + "\n");
        Song_Info.appendText("歌曲長度：" + songinfo.getDuration() + "\n");
        Song_Info.appendText("添加者：" + songinfo.getOwner() + "\n");
        Song_Info.appendText("偏好：" + songinfo.getPreference() + "\n");
        Song_Info.appendText("標籤：");
        for(int i = 0 ; i < songinfo.getLabelsize() ; i++){
            Song_Info.appendText(" " + songinfo.getLabel(i));
        }
    }
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fileChooser.setInitialDirectory(new File("C:\\Users\\user\\Downloads"));

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
        for(Song song: first_songlist){
            ori_songlist.add(song.clone());
        }

    }
}