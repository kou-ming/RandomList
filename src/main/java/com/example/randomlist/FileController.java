package com.example.randomlist;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class FileController implements Initializable {

    @FXML
    private VBox SongList_view;

    @FXML
    private TableColumn<Song, String> ListName;

    @FXML
    private TableView<Song> SongTableView;

    @FXML
    private TextArea Song_Info;


    FileChooser fileChooser = new FileChooser();    //建立檔案選擇器
    public String path = "C:";  //預設檔案路徑

    public String File_path = "c:";
    static public ObservableList<Song> List = FXCollections.observableArrayList();  //儲存歌曲的List
    static public ObservableList<List_Info> ALL_List = FXCollections.observableArrayList();  //儲存本地歌單

    public String[] Youtubename = {"George","Inventor 發明家 697","Wang wen-ho","evan"};
    public String[] Username = {"薛耀智", "許高銘", "王文和", "鍾君逸"};

    public Map<String, String> User_Map = new HashMap<>();

    //開啟歌單(清除上一個歌單紀錄)
    @FXML
    void openList(MouseEvent event) {
        List.clear();
        System.out.println("openList");
        File file = fileChooser.showOpenDialog(new Stage());    //可以在頁面選擇檔案位置
        if (file != null) {
            path = file.getAbsolutePath();  //若檔案不為空則重設路徑
        }
        readFile(path);

    }

    //導入歌單(不清除上一個歌單紀錄)
    @FXML
    void AddList(MouseEvent event) {
        System.out.println("addList");
        File file = fileChooser.showOpenDialog(new Stage());    //可以在頁面選擇檔案位置
        if (file != null) {
            path = file.getAbsolutePath();  //若檔案不為空則重設路徑
        }
        readFile(path);
    }

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
            Song_Info.appendText(songinfo.getName() + "\n");
            Song_Info.appendText(songinfo.getChannel() + "\n");
            Song_Info.appendText(songinfo.getDuration() + "\n");
            Song_Info.appendText(songinfo.getLink() + "\n");
            Song_Info.appendText("偏好: " + songinfo.getPreference() + "\n");
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
        for(int i = 0 ; i < ALL_List.size() ; i++) {
            System.out.println(ALL_List.get(i).getName());
        }
        Stage popupStage = new Stage();
        Pane popupRoot = FXMLLoader.load(getClass().getResource("new_list_popupScene.fxml"));




//        TextField textField2 = new TextField();
//        textField2.setPrefWidth(200);

        TextField textfield = new TextField();
        textfield.setPrefWidth(200);
//        textfield.setLayoutX(26);


        VBox.setMargin(textfield, new Insets(50, 0, 0, 0)); // 设置上边距为50

        TextField textField1 = new TextField();
        textfield.setPrefSize(300, 40);

        VBox.setMargin(textfield, new Insets(100, 0, 0, 0)); // 设置上边距为50


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
        Scene popupScene = new Scene(popupRoot, 400, 300);
        popupStage.setScene(popupScene);
        popupStage.show();

    }
    //初始化
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fileChooser.setInitialDirectory(new File("C:\\Users\\user\\Downloads"));    //將初始路徑設為"下載"

        for(int i = 0 ; i < Username.length ; i++){
            User_Map.put(Youtubename[i], Username[i]);
        }
        //初始化表格
        ListName.setSortable(false);
        ListName.setCellValueFactory(new PropertyValueFactory<>("name"));
        SongTableView.setItems(List);

        loadFile();
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
                            List.add(song);
                        }
                        else{
                            List.add(new Song(row[0], row[1],row[2],row[3]));
                        }
                    }
                    for (int i = 4 ; i < row.length ; i++){
                        List.get(index_line).addLabel(row[i]);
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
                System.out.println(user);
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
                printWriter.write(List.get(i).getName() + "\t" + List.get(i).getChannel() + "\t" + List.get(i).getDuration() + "\t" + List.get(i).getLink() + "\n");
            }

            System.out.println(file.getName());
            System.out.println(file.getPath());
            printWriter.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


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

    //建立歌單預覽按鈕
    void create_listInfo(String list_name) throws IOException {
        list_name = list_name.replace(".txt", "");
        Button songlist_name = new Button(list_name);
//        songlist_name.getStylesheets().add(String.valueOf(getClass().getResource("file_Scene.css")));
        songlist_name.getStyleClass().add("list_button");
        songlist_name.setPrefSize(125, 50);
        songlist_name.setOnAction(event -> {
            System.out.println("Button clicked!");
            ListName.setText(songlist_name.getText());
            List.clear();
            path = "src\\main\\java\\SongList_File\\" + songlist_name.getText() + ".txt";
            readFile(path);
        });
        System.out.println(songlist_name.getText());
        SongList_view.getChildren().add(songlist_name);
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
                System.out.println("文件创建成功！");
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
    //替轉至編輯頁面
    @FXML
    void chscene_editor(MouseEvent event) throws IOException {
        System.out.println("change");
        Parent root = FXMLLoader.load(getClass().getResource("ed_Scene.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
