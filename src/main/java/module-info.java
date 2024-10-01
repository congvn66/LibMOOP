module com.example.proj {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    opens com.example.proj to javafx.fxml;
    exports com.example.proj;
}