package com.example.proj;

import com.example.proj.Models.BookFormat;
import com.example.proj.Models.BookItem;
import com.example.proj.Models.BookStatus;
import com.example.proj.Models.QRCodeService;
import com.google.zxing.WriterException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

public class QRCodeTest extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Tạo đối tượng BookItem để test
        //BookItem book = new BookItem("Effective Java", "Joshua Bloch", "9780134685991", "450,000 VND", "English", 416);
        BookItem bookItem = new BookItem(
                "9780134685991", // ISBN
                "Effective Java", // Title
                "Programming", // Subject
                "Addison-Wesley", // Publisher
                "English", // Language
                "416", // Number of Pages
                "author", // Author name
                "handsome", // Author description
                null, // ID
                false, // Is reference only
                45.0, // Price
                BookFormat.PAPERBACK, // Format
                BookStatus.AVAILABLE, // Status
                Date.valueOf(LocalDate.of(2017, 12, 27)), // Date of purchase
                Date.valueOf(LocalDate.of(2008, 5, 8)), // Publication date
                1, // Number
                "Shelf A", // Location
                "deafault.png"
        );

        try {
            // Gọi QRCodeService để tạo mã QR
            QRCodeService qrCodeService = QRCodeService.getInstance();
            javafx.scene.image.Image qrCodeImage = qrCodeService.generateQRCode(bookItem, 300, 300);

            // Hiển thị mã QR
            ImageView qrCodeView = new ImageView(qrCodeImage);
            qrCodeView.setFitWidth(300);
            qrCodeView.setFitHeight(300);

            VBox root = new VBox(qrCodeView);
            Scene scene = new Scene(root, 400, 400);
            primaryStage.setScene(scene);
            primaryStage.setTitle("QR Code Test");
            primaryStage.show();
        } catch (IOException | WriterException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
