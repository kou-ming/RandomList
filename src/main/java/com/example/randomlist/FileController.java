package com.example.randomlist;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class FileController implements Initializable {

    @FXML
    private VBox SongList_view;

    @FXML
    private TableColumn<Song, String> SongName;

    @FXML
    private TableView<Song> SongTableView;

    @FXML
    private TextArea Song_Info;

    FileChooser fileChooser = new FileChooser();    //建立檔案選擇器
    public String path = "C:";  //預設檔案路徑

    static public ObservableList<Song> List = FXCollections.observableArrayList();  //儲存歌曲的List

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
            Song_Info.appendText("標籤：");
            for(int i = 0 ; i < songinfo.getLabelsize() ; i++){
                Song_Info.appendText(" " + songinfo.getLabel(i));
            }
        }
    }
    
    //初始化
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fileChooser.setInitialDirectory(new File("C:\\Users\\user\\Downloads"));    //將初始路徑設為"下載"

        //初始化表格
        SongName.setSortable(false);
        SongName.setCellValueFactory(new PropertyValueFactory<>("name"));
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
                        List.add(new Song(row[0], row[1],row[2],row[3]));
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


    void loadFile(){
        try{
            BufferedReader reader = null;
            String line = "";
            int index_line = 0; //目前讀到第幾列
            try{
                reader = new BufferedReader(new FileReader("C:\\Javafx_homework\\RandomList\\src\\main\\java\\SongList_File\\File_name.txt"));

                //一次讀一列
                while((line = reader.readLine()) != null){

                    String[] row = line.split("\t");
                    if(index_line >= 0){
                        path = "C:\\Javafx_homework\\RandomList\\src\\main\\java\\SongList_File\\" + row[0];
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

    void create_listInfo(String list_name) throws IOException {
        list_name = list_name.replace(".txt", "");
        Button songlist_name = new Button(list_name);
        songlist_name.setPrefSize(125, 50);
        songlist_name.setOnAction(event -> {
            System.out.println("Button clicked!");
            List.clear();
            path = "C:\\Javafx_homework\\RandomList\\src\\main\\java\\SongList_File\\" + songlist_name.getText() + ".txt";
            readFile(path);
        });
        System.out.println(songlist_name.getText());
        SongList_view.getChildren().add(songlist_name);
        SongList_view.layout();

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
