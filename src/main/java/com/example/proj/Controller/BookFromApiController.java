package com.example.proj.Controller;

import com.example.proj.Models.BookItem;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class BookFromApiController {
    @FXML
    private Label authorNameLabel;

    @FXML
    private Label bookLanguageLabel;

    @FXML
    private Label bookSubjectLabel;

    @FXML
    private ImageView bookThumbnail;

    @FXML
    private Label bookTitleLabel;

    @FXML
    private Button watchBook;

    private BookItem bookItem;


    public void setUpBook(BookItem bookItem) {
        this.bookItem = bookItem;
        bookTitleLabel.setText(bookItem.getTitle());
        bookLanguageLabel.setText(bookItem.getLanguage());
        bookSubjectLabel.setText(bookItem.getSubject());
        authorNameLabel.setText(bookItem.getAuthor().getName());
        if (bookItem.getImgName().equals("image.png")) {
            bookThumbnail.setImage(new Image(getClass().getResource(bookItem.generateImagePathFromImageName(bookItem.getImgName())).toExternalForm()));
        } else {
            bookThumbnail.setImage(new Image(bookItem.getImgName()));
        }
    }

    public void switchToAddBookTab(BookItem bookItem) {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/proj/FXML/AddBook.fxml"));
        try {
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            System.out.println("can not load fxml file at BookFromApi");
            e.printStackTrace();
        }
    }

    public void setBookFromApiTab(ActionEvent event) {
        if (event.getSource() == watchBook) {
           AddBookController.setBookItem(bookItem);
           switchToAddBookTab(bookItem);
        }
    }
}
