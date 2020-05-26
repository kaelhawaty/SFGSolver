module org.openjfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.management;
    requires JavaFXSmartGraph.master;
    opens org.openjfx to javafx.fxml;
    exports org.openjfx;
}