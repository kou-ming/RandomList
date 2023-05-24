module com.example.randomlist {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    //requires org.jsoup;

    opens com.example.randomlist to javafx.fxml;
    exports com.example.randomlist;
}