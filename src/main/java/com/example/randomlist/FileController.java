package com.example.randomlist;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class FileController implements Initializable {

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
        readFile();

    }

    //導入歌單(不清除上一個歌單紀錄)
    @FXML
    void AddList(MouseEvent event) {
        System.out.println("addList");
        readFile();
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
        Song songinfo = SongTableView.getSelectionModel().getSelectedItem();    //取得哥死資訊
        Song_Info.clear();
        Song_Info.appendText(songinfo.getName() + "\n");
        Song_Info.appendText(songinfo.getChannel() + "\n");
        Song_Info.appendText(songinfo.getDuration() + "\n");
        Song_Info.appendText(songinfo.getLink());
    }
    
    //初始化
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fileChooser.setInitialDirectory(new File("C:\\Users\\user\\Downloads"));    //將初始路徑設為"下載"

        //初始化表格
        SongName.setSortable(false);
        SongName.setCellValueFactory(new PropertyValueFactory<>("name"));
        SongTableView.setItems(List);
    }

    //讀取檔案
    void readFile(){
        File file = fileChooser.showOpenDialog(new Stage());    //可以在頁面選擇檔案位置
        if (file != null) {
            path = file.getAbsolutePath();  //若檔案不為空則重設路徑
        }

        try{
            BufferedReader reader = null;
            String line = "";
            int index_line = 0; //目前讀到第幾列
            try{
                reader = new BufferedReader(new FileReader(path));

                //一次讀一列
                while((line = reader.readLine()) != null){
                    index_line += 1;
                    String[] row = line.split("\t");
                    if(index_line > 1){
                        List.add(new Song(row[0], row[1],row[2],row[3]));
                    }
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
