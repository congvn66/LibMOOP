module com.example.proj {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires jdk.compiler;
    requires java.net.http;

    requires java.sql;
    requires java.desktop;
    requires org.json;
    requires com.google.zxing;
    requires com.google.zxing.javase;


    opens com.example.proj to javafx.fxml;
    exports com.example.proj;
    exports com.example.proj.Models;
    opens com.example.proj.Models to javafx.fxml;
    exports com.example.proj.Controller;
    opens com.example.proj.Controller to javafx.fxml;
}