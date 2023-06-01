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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.io.*;
import java.net.URL;
import java.util.*;

public class FileController implements Initializable {

    @FXML
    private VBox SongList_view;

    @FXML
    private TableView<Song> SongTableView;

    @FXML
    private TableColumn<Song, String> ListName;

    @FXML
    private TableColumn<Song, String> WriterName;

    @FXML
    private TableColumn<Song, Integer> Pref;

    @FXML
    private TextArea Song_Info;

    @FXML
    private VBox list_buttons;

    @FXML
    private ScrollPane List_Pane;

    @FXML
    private AnchorPane ALL_List_Pane;

    FileChooser fileChooser = new FileChooser();    //建立檔案選擇器
    public String path = "C:";  //預設檔案路徑

    static public String now_list_path = "c:";  //目前正在編輯的歌單的檔案未置;
    public String File_path = "c:";  //Song_List_File 這個資料夾的主要路徑
    static public ObservableList<Song> List = FXCollections.observableArrayList();  //儲存歌曲的List
    static public ObservableList<List_Info> ALL_List = FXCollections.observableArrayList();  //儲存本地歌單

    static public ObservableList<String> Labels = FXCollections.observableArrayList();  //儲存標籤
    public String[] Youtubename = {"George","Inventor 發明家 697","Wang wen-ho","evan"};
    public String[] Username = {"薛耀智", "許高銘", "王文和", "鍾君逸"};

    static public Map<String, String> User_Map = new HashMap<>();
    static public Map<String, String> User_to_Youtube = new HashMap<>();

    private String now_choose_list = "";

    private double scroll_value = 0;

    private int now_list_amount = 0;


    static public String listname = "未選取歌單";



    //輸出歌單
    @FXML
    void exportList(MouseEvent event) {
        System.out.println("exportList");
        File file = fileChooser.showSaveDialog(new Stage());
        saveSystem(file, List);
    }

    //點擊表格時顯示詳細資訊
    @FXML
    void get_Info(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            Song songinfo = SongTableView.getSelectionModel().getSelectedItem(); //取得歌曲資訊
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
    }

    //建立新歌單
    @FXML
    void call_newlistPopup(MouseEvent event) throws IOException {
        System.out.println("call a popupScene");
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initOwner(Main.primaryStage);

        Label label = new Label("空白歌單名稱為：");
        label.setLayoutX(76);
        label.setLayoutY(60);
        label.setFont(new Font(32));

        Pane popupRoot = FXMLLoader.load(getClass().getResource("new_list_popupScene.fxml"));
        TextField textfield = new TextField();
        textfield.setPrefWidth(200);
        VBox.setMargin(textfield, new Insets(100, 0, 0, 0)); // 设置上边距为100
        textfield.setPrefSize(300, 40);

        Button button = new Button("建立");
        button.setPrefSize(97, 41);
        button.setLayoutX(132);
        button.setLayoutY(200);

        button.setOnAction(e -> {
            String temp_path = File_path + textfield.getText() + ".txt";
            if(create_file(temp_path)){
                ALL_List.add(new List_Info(textfield.getText() + ".txt"));
                create_List();
            }
            SongList_view.getChildren().clear();
            loadFile();
            closeWindow(popupStage);
        });

        VBox root = new VBox(textfield);
        root.setLayoutX(40);
        root.setLayoutY(10);

        popupRoot.getChildren().add(root);
        popupRoot.getChildren().add(button);
        popupRoot.getChildren().add(label);
        Scene popupScene = new Scene(popupRoot, 400, 300);
        popupStage.setScene(popupScene);
        popupStage.showAndWait();
    }

    //建立新標籤
    @FXML
    void call_labelPopup(MouseEvent event) throws IOException{
        System.out.println("call a popupScene");

        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initOwner(Main. primaryStage);

        Pane popupRoot = FXMLLoader.load(getClass().getResource("new_list_popupScene.fxml"));
        TextField textfield = new TextField();
        VBox.setMargin(textfield, new Insets(80, 0, 0, 0)); // 设置上边距为50
        textfield.setPrefSize(110, 40);


        Label label = new Label("新增標籤");
        label.setLayoutX(35);
        label.setLayoutY(30);
        label.setFont(new Font(32));

        TextArea textArea = new TextArea();
        for(int i = 0 ; i < Labels.size() ; i++){
            if( i == Labels.size() - 1){
                textArea.appendText(Labels.get(i));
            }
            else{
                textArea.appendText(Labels.get(i) + "\n");
            }
        }
        textArea.setLayoutX(200);
        textArea.setLayoutY(20);
        textArea.setPrefWidth(150);
        textArea.setPrefHeight(260);
        textArea.setEditable(false);

        Button button = new Button("建立");
        button.setPrefSize(97, 41);
        button.setLayoutX(45);
        button.setLayoutY(150);

        button.setOnAction(e -> {
            if(!textfield.getText().equals("")) {
                for(int i = 0 ; i < Labels.size() ; i++){
                    if (!Labels.get(i).equals(textfield.getText())){
                        if ( i == Labels.size() - 1){
                            Labels.add(textfield.getText());
                            update_Label_file();
                            textArea.clear();
                            for(int j = 0 ; j < Labels.size() ; j++){
                                if( j == Labels.size() - 1){
                                    textArea.appendText(Labels.get(j));
                                }
                                else{
                                    textArea.appendText(Labels.get(j) + "\n");
                                }
                            }
                        }
                    }
                    else{
                        break;
                    }
                }
            }
            textfield.clear();
        });

        VBox root = new VBox(textfield);
        root.setLayoutX(40);
        root.setLayoutY(10);

        popupRoot.getChildren().add(root);
        popupRoot.getChildren().add(button);
        popupRoot.getChildren().add(textArea);
        popupRoot.getChildren().add(label);
        Scene popupScene = new Scene(popupRoot, 400, 300);
        popupStage.setScene(popupScene);
        popupStage.showAndWait();
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
                            Song song = new Song(row[0], row[1].replace(ownerlname, ""),row[2].replaceAll(" ", ""),row[3]);
                            song.setOwner(User_Map.get(ownerlname));
                            List.add(song);
                        }
                        else{
                            List.add(new Song(row[0], row[1],row[2].replaceAll(" ", ""),row[3]));
                        }
                    }

                    if(row.length >= 5){
                        List.get(index_line).setPreference(Integer.parseInt(row[4]));
                        for (int i = 5 ; i < row.length ; i++){
                            List.get(index_line).addLabel(row[i]);
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
    //將List資料編寫成檔案
    void saveSystem(File file, ObservableList<Song> List){
        try {
            PrintWriter printWriter = new PrintWriter(file);
            printWriter.write("<Name>	<Channel>	<Duration>	<URL>\n");
            for(int i = 0; i < List.size() ; i++){
                printWriter.write(List.get(i).getName() + "\t" + List.get(i).getChannel());
                if(!List.get(i).getOwner().equals("")){
                    printWriter.write(User_to_Youtube.get(List.get(i).getOwner()));
                }
                printWriter.write("\t" + List.get(i).getDuration() + "\t" + List.get(i).getLink() + "\t" + List.get(i).getPreference());

                for(int j = 0 ; j < List.get(i).getLabelsize() ; j++){
                    printWriter.write("\t" + List.get(i).getLabel(j));
                }
                printWriter.write("\n");
            }
            printWriter.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    //讀取File_name.txt後建立本地歌單
    public void loadFile(){
        ALL_List.clear();
        try{
            BufferedReader reader = null;
            String line = "";
            int index_line = 0; //目前讀到第幾列
            try{

                File file = new File("src\\main\\java\\SongList_File\\File_name.txt");
                reader = new BufferedReader(new FileReader(file));
                File_path = file.getAbsolutePath().replace("File_name.txt", "");
                System.out.println(File_path);
                now_list_amount = 0; //清空清單按鈕數
                //一次讀一列
                while((line = reader.readLine()) != null){

                    String[] row = line.split("\t");
                    if(index_line >= 0){
                        path = "src\\main\\java\\SongList_File\\SongList_File\\" + row[0];

                        ALL_List.add(new List_Info(row[0]));
                        create_listInfo(row[0]);
                    }
                    for (int i = 4 ; i < row.length ; i++){
//                        List.get(index_line).addLabel(row[i]);
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


    //讀取Label_name.txt後建立標籤
    public void loadLabel(){
        Labels.clear();
        try{
            BufferedReader reader = null;
            String line = "";
            int index_line = 0; //目前讀到第幾列
            try{

                File file = new File(File_path + "Label_name.txt");
                reader = new BufferedReader(new FileReader(file));
                //一次讀一列
                while((line = reader.readLine()) != null){
                    if(!line.equals("")){
                        Labels.add(line);
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
        finally {
            for (int i = 0 ; i < Labels.size() ; i++){
                System.out.println(Labels.get(i));
            }
        }
    }
    //建立歌單預覽按鈕
    void create_listInfo(String list_name) throws IOException {
        list_name = list_name.replace(".txt", "");
        Button songlist_name = new Button(list_name);
        songlist_name.getStyleClass().add("list_button");

        int button_height = 50;
        songlist_name.setPrefSize(125, button_height);

        songlist_name.setOnMouseClicked(event -> {
            if(event.getButton() == MouseButton.SECONDARY){
                list_buttons.setVisible(true);
                now_choose_list = songlist_name.getText();
                System.out.println(scroll_value + " " + SongList_view.getPrefHeight() + " " + scroll_value * (SongList_view.getPrefHeight() - List_Pane.getPrefHeight()));
                list_buttons.setLayoutX(songlist_name.getLayoutX() + 100);
                list_buttons.setLayoutY(songlist_name.getLayoutY() + 20 - scroll_value * (SongList_view.getPrefHeight() - List_Pane.getPrefHeight()));
            }
            else{
                if(!now_choose_list.equals(songlist_name.getText())){
                    list_buttons.setVisible(false);
                }
                System.out.println("Button clicked!");
                listname = songlist_name.getText();
                ListName.setText(listname);
                List.clear();
                path = "src\\main\\java\\SongList_File\\" + songlist_name.getText() + ".txt";
                now_list_path = path;
                readFile(path);
            }
        });
        System.out.println(songlist_name.getText());
        SongList_view.getChildren().add(songlist_name);
        now_list_amount ++;
        SongList_view.setPrefHeight(now_list_amount * button_height);
        //設置scroll Pane的高度動態調整
        if(SongList_view.getPrefHeight() > ALL_List_Pane.getPrefHeight()){
            List_Pane.setPrefHeight(ALL_List_Pane.getPrefHeight());
        }
        else{
            List_Pane.setPrefHeight(SongList_view.getPrefHeight() + 3);
        }
        //設置scroll Pane的寬度動態調整
        if(SongList_view.getPrefHeight() > List_Pane.getPrefHeight()){
            List_Pane.setPrefWidth(145);
        }
        else{
            List_Pane.setPrefWidth(129);
        }

        SongList_view.layout();
    }

    void closeWindow(Stage stage) {
        // 关闭窗口
        stage.close();
    }

    //創立新的歌單文件
    boolean create_file(String path){
        File file = new File(path);

        try {
            if (file.createNewFile()) {
                System.out.println("文件創建成功！");
                return true;
            } else {
                System.out.println("文件已存在！");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    boolean del_file(String path){
        File file = new File(path);
        if (file.delete()) {
            System.out.println("文件刪除成功！");
            return true;
        } else {
            System.out.println(path);
            System.out.println("文件刪除失敗！");
        }
        return false;
    }

    //將目前有的本地檔案以文件儲存
    void create_List() {
        File file = new File("src\\main\\java\\SongList_File\\File_name.txt");
        try {
            PrintWriter printWriter = new PrintWriter(file);
            for(int i = 0 ; i < ALL_List.size() ; i++) {
                if (i == ALL_List.size() - 1){
                    printWriter.write(ALL_List.get(i).getName());
                }
                else{
                    printWriter.write(ALL_List.get(i).getName() + "\n");
                }
            }
            printWriter.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    void update_Label_file() {
        File file = new File(File_path + "Label_name.txt");
        try {
            PrintWriter printWriter = new PrintWriter(file);

            for(int i = 0 ; i < Labels.size() ; i++) {
                if (i == Labels.size() - 1){
                    printWriter.write(Labels.get(i));
                }
                else{
                    printWriter.write(Labels.get(i) + "\n");
                }
            }
            printWriter.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void export_Url(MouseEvent event) throws IOException {
        if (listname.equals("未選取歌單")){
            no_choose_list();
        }
        else{
            for(Song song: List){
                System.out.println(song.getLink());

            }
            call_url();
        }
    }

    void call_url() throws IOException {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initOwner(Main.primaryStage);

        Pane popupRoot = FXMLLoader.load(getClass().getResource("new_list_popupScene.fxml"));

        TextArea textArea = new TextArea();
        for(int i = 0 ; i < List.size() ; i++){
            if( i == List.size() - 1){
                textArea.appendText(List.get(i).getLink());
            }
            else{
                textArea.appendText(List.get(i).getLink() + "\n");
            }
        }
        textArea.setLayoutX(100);
        textArea.setLayoutY(20);
        textArea.setPrefWidth(500);
        textArea.setPrefHeight(450);
        textArea.getStyleClass().add("text-area_v2");
        textArea.setEditable(false);


//        Label label1 = new Label("警告！");
//        label1.setLayoutX(130);
//        label1.setLayoutY(60);
//        label1.setFont(new Font(45));
//        label1.getStyleClass().add("error_label");
//        Label label2 = new Label("你未選取歌單");
//        label2.setLayoutX(65);
//        label2.setLayoutY(130);
//        label2.setFont(new Font(45));
//        label2.getStyleClass().add("error_label");
//        popupRoot.getChildren().addAll(label1, label2);
        popupRoot.getChildren().addAll(textArea);
        Scene popupScene = new Scene(popupRoot, 700, 500);
        popupStage.setScene(popupScene);
        popupStage.showAndWait();
    }

    //替轉至編輯頁面
    @FXML
    void chscene_editor(MouseEvent event) throws IOException {
        if (listname.equals("未選取歌單")){
            no_choose_list();
        }
        else{
            Parent root = FXMLLoader.load(getClass().getResource("ed_Scene.fxml"));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }

    }


    @FXML
    void bt_chscene_double(MouseEvent event) throws IOException {
        if(listname.equals("未選取歌單")){
            no_choose_list();
        }
        else{
            Parent root = FXMLLoader.load(getClass().getResource("double_Scene.fxml"));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }

    }


    @FXML
    void rename_List(MouseEvent event) throws IOException {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initOwner(Main.primaryStage);

        Label label = new Label("重新命名為：");
        label.setLayoutX(76);
        label.setLayoutY(60);
        label.setFont(new Font(32));

        Pane popupRoot = FXMLLoader.load(getClass().getResource("new_list_popupScene.fxml"));
        TextField textfield = new TextField();
        textfield.setPrefWidth(200);
        VBox.setMargin(textfield, new Insets(100, 0, 0, 0)); // 设置上边距为100
        textfield.setPrefSize(300, 40);

        Button button = new Button("確定更動");
        button.setPrefSize(97, 41);
        button.setLayoutX(132);
        button.setLayoutY(200);

        button.setOnAction(e -> {
            String temp_path = File_path + now_choose_list + ".txt";
            File file = new File(temp_path);
            File newFile = new File(file.getParent(), textfield.getText() + ".txt");
            for( List_Info index: ALL_List){
                System.out.println(index.getName());
                if(index.getName().equals(now_choose_list + ".txt")){
                    index.setName(textfield.getText() + ".txt");
                    file.renameTo(newFile);
                    create_List();
                    SongList_view.getChildren().clear();
                    loadFile();
                    listname = textfield.getText();
                    ListName.setText(listname);
                    break;
                }
            }
            closeWindow(popupStage);
            list_buttons.setVisible(false);
        });

        VBox root = new VBox(textfield);
        root.setLayoutX(40);
        root.setLayoutY(10);

        popupRoot.getChildren().add(root);
        popupRoot.getChildren().add(button);
        popupRoot.getChildren().add(label);
        Scene popupScene = new Scene(popupRoot, 400, 300);
        popupStage.setScene(popupScene);
        popupStage.showAndWait();
    }

    @FXML
    void del_Lsit(MouseEvent event) {
        String temp_path = File_path + now_choose_list + ".txt";
        if(del_file(temp_path)){
            for(int i = 0 ; i < ALL_List.size() ; i++){
                if(ALL_List.get(i).getName().equals(now_choose_list + ".txt")){
                    ALL_List.remove(i);
                    listname = "未選取歌單";
                    ListName.setText(listname);
                    List.clear();
                    break;
                }
            }
            create_List();
        }
        SongList_view.getChildren().clear();
        loadFile();

        list_buttons.setVisible(false);
    }

    @FXML
    void exite_list_pane(MouseEvent event) {
        list_buttons.setVisible(false);
    }

    //若沒有選取歌單則警告
    void no_choose_list() throws IOException {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initOwner(Main.primaryStage);

        Pane popupRoot = FXMLLoader.load(getClass().getResource("new_list_popupScene.fxml"));


        Label label1 = new Label("警告！");
        label1.setLayoutX(130);
        label1.setLayoutY(60);
        label1.setFont(new Font(45));
        label1.getStyleClass().add("error_label");
        Label label2 = new Label("你未選取歌單");
        label2.setLayoutX(65);
        label2.setLayoutY(130);
        label2.setFont(new Font(45));
        label2.getStyleClass().add("error_label");
        popupRoot.getChildren().addAll(label1, label2);
        Scene popupScene = new Scene(popupRoot, 400, 300);
        popupStage.setScene(popupScene);
        popupStage.showAndWait();
    }
    //初始化
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fileChooser.setInitialDirectory(new File("C:\\Users\\user\\Downloads"));    //將初始路徑設為"下載"

        for(int i = 0 ; i < Username.length ; i++){
            User_Map.put(Youtubename[i], Username[i]);
            User_to_Youtube.put(Username[i], Youtubename[i]);
        }
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

        ListName.setText(listname);
        SongTableView.setItems(List);

        loadFile();
        loadLabel();

        List_Pane.vvalueProperty().addListener((observable, oldValue, newValue) -> {
            scroll_value = (double) newValue;
            list_buttons.setLayoutY(list_buttons.getLayoutY() - ((double) newValue - (double) oldValue) * (SongList_view.getPrefHeight() - List_Pane.getPrefHeight()));
            if(list_buttons.getLayoutY() < 0){
                list_buttons.setVisible(false);
            }
            else if(list_buttons.getLayoutY() > List_Pane.getPrefHeight()){
                list_buttons.setVisible(false);
            }

        });
    }
}
