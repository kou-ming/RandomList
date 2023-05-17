package com.example.randomlist;

import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;

public class HelloController implements Initializable {


    static public ArrayList<Song> songslist = new ArrayList<>();    //儲存歌曲的arraylist
    static public ObservableList<Song> List = FXCollections.observableArrayList();

    public String path = "C:";
    private int string_len = 0;
    FileChooser fileChooser = new FileChooser();
    @FXML
    private Button bt_getTxt;

    @FXML
    private TextArea Information;

    @FXML
    private TextArea textarea;

    @FXML
    private TableView<Song> table;

    @FXML
    private TableColumn<Song, String> name;

    @FXML
    private TableColumn<Song, String> channel;

    @FXML
    private TableColumn<Song, String> duration;

    @FXML
    private TableColumn<Song, String> link;

    @FXML
    private TextFlow textpanel;

    @FXML
    private TableView<Song> songname_table;

    @FXML
    private TableColumn<Song, String> songname;

    @FXML
    private Button chscene_editor;

    @FXML
    void getTxt(MouseEvent event){
        File file = fileChooser.showOpenDialog(new Stage());
        int i = 0;
        if (file != null) {
            path = file.getAbsolutePath();
        }

        try{
            BufferedReader reader = null;
            String line = "";
            try {
                reader = new BufferedReader(new FileReader(path));
                while((line = reader.readLine()) != null) {
                    String[] row = line.split("\t");

                    if(i > 0){
                        songslist.add(new Song(row[0], row[1],row[2],row[3]));
                        List.add(new Song(row[0], row[1],row[2],row[3]));
                        if(row[0].length() > string_len){
                            string_len = row[0].length();
                        }
//                        Text text = new Text(row[0] + "\n");
//                        text.setWrappingWidth(TextFlow.USE_PREF_SIZE);
                        textpanel.getChildren().add( new Text(row[0] + "\n"));
                    }
                    i += 1;
                }
            }
            catch(Exception e) {
                e.printStackTrace();
            }
            finally{
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            songname_table.setPrefWidth(string_len * 8);

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void saveTxt(MouseEvent event) {
        File file = fileChooser.showSaveDialog(new Stage());
        saveSystem(file, List);

    }

    @FXML
    void get_Info(MouseEvent event) {
        Song songinfo = songname_table.getSelectionModel().getSelectedItem();
        Information.clear();
        Information.appendText(songinfo.getName() + "\n");
        Information.appendText(songinfo.getChannel() + "\n");
        Information.appendText(songinfo.getDuration() + "\n");
        Information.appendText(songinfo.getLink());

    }


    @FXML
    void chscene_editor(ActionEvent event) throws IOException{
        System.out.println("change");
        Parent root = FXMLLoader.load(getClass().getResource("ed_Scene.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    @FXML
    void drag_start(MouseEvent event) {
//        System.out.println("drag_start");
//        final Dragboard dragboard = songname_table.startDragAndDrop(TransferMode.MOVE);
//        //dragboard.setDragView(songname_table.snapshot(null, null)); // 設置拖動時顯示的圖像
//        ClipboardContent content = new ClipboardContent();
//        content.putString("SelectedRows");
//        dragboard.setContent(content);
    }
    @FXML
    void draging(DragEvent event) {
//        event.acceptTransferModes(TransferMode.MOVE);
    }

    @FXML
    void drag_end(DragEvent event) {
//        Dragboard dragboard = event.getDragboard();
//        if (dragboard.hasString()) {
//            int targetIndex = 0;
//            int selectedIndex = 1;
//            /* 調整TableView中的數據 */
//            event.setDropCompleted(true);
//        } else {
//            event.setDropCompleted(false);
//        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fileChooser.setInitialDirectory(new File("C:\\Users\\user\\Downloads"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        channel.setCellValueFactory(new PropertyValueFactory<>("channel"));
        duration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        link.setCellValueFactory(new PropertyValueFactory<>("link"));

        songname.setCellValueFactory(new PropertyValueFactory<>("name"));

        table.setItems(List);
        songname_table.setItems(List);
//        songname_table.setPrefWidth(string_len);

    }

    public void saveSystem(File file, ObservableList<Song> List){
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
}
