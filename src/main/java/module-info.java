module com.example.proj {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;
    requires jdk.compiler;


    opens com.example.proj to javafx.fxml;
    exports com.example.proj;
}