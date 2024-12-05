package com.example.proj.Controller;
import com.example.proj.Models.*;
import com.google.zxing.WriterException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;

public class WatchBookController implements Initializable {
    @FXML
    private TextField idText;

    @FXML
    private Label authorDes;

    @FXML
    private Label authorName;

    @FXML
    private Button backToLibBookBut;

    @FXML
    private ChoiceBox<BookFormat> bookFormatChoiceBox;

    @FXML
    private TextField bookLocationText;

    @FXML
    private TextField bookNumberText;

    @FXML
    private ChoiceBox<BookStatus> bookStatusChoiceBox;

    @FXML
    private Button borrowBookBut;

    @FXML
    private Label checkBorrowBookLabel;

    @FXML
    private DatePicker dateOfPurchasesPicker;

    @FXML
    private ChoiceBox<Boolean> isRefOnlyChoiceBox;

    @FXML
    private Label isbn;

    @FXML
    private Label language;

    @FXML
    private Label numberOfPage;

    @FXML
    private TextField priceText;

    @FXML
    private DatePicker publicationDatePicker;

    @FXML
    private Label publisher;

    @FXML
    private Label subject;

    @FXML
    private Label title;

    @FXML
    private ImageView bookThumbnail;

    @FXML
    private ImageView qrCode;

    private Alert alert;

    private static BookItem bookItem;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        isbn.setText(bookItem.getISBN());
        title.setText(bookItem.getTitle());
        subject.setText(bookItem.getSubject());
        publisher.setText(bookItem.getPublisher());
        language.setText(bookItem.getLanguage());
        numberOfPage.setText(bookItem.getNumberOfPage());
        authorName.setText(bookItem.getAuthor().getName());
        authorDes.setText(bookItem.getAuthor().getDescription());
        idText.setText(bookItem.getId());
        isRefOnlyChoiceBox.setValue(bookItem.getIsReferenceOnly());
        bookStatusChoiceBox.setValue(bookItem.getStatus());
        bookFormatChoiceBox.setValue(bookItem.getFormat());
        priceText.setText(String.valueOf(bookItem.getPrice()));
        dateOfPurchasesPicker.setValue(bookItem.getDateOfPurchase().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        publicationDatePicker.setValue(bookItem.getPublicationDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        bookNumberText.setText(String.valueOf(bookItem.getRack().getNumber()));
        bookLocationText.setText(bookItem.getRack().getLocationIdentifier());
        if (bookItem.checkURL()) {
             bookThumbnail.setImage(new Image(bookItem.getImgName()));
        } else {
            bookThumbnail.setImage(new Image(getClass().getResource(bookItem.generateImagePathFromImageName(bookItem.getImgName())).toExternalForm()));
        }
        try {
            qrCode.setImage(QRCodeService.getInstance().generateQRCode(bookItem, (int)qrCode.getFitWidth(), (int)qrCode.getFitHeight()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (WriterException e) {
            throw new RuntimeException(e);
        }
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("You have successfully borrowed this book!");
    }

    public static void setBookItem(BookItem watchBookItem) {
        bookItem = watchBookItem;
    }

    public void setWatchBookTab(ActionEvent event) throws ParseException {
        if (event.getSource() == borrowBookBut) {
            if (CurrentMember.getMember().getPoint() == 0
                    || CurrentMember.getMember().getStatus() == AccountStatus.BLACKLISTED) {
                checkBorrowBookLabel.setText("Your credit is too low \nor you have been blacklisted");
                checkBorrowBookLabel.setVisible(true);
            } else if (bookItem.getStatus() != BookStatus.AVAILABLE || bookItem.getIsReferenceOnly()) {
                checkBorrowBookLabel.setText("Book can not be lent");
                checkBorrowBookLabel.setVisible(true);
            } else {
                checkBorrowBookLabel.setText(CurrentMember.getMember().basicActions(bookItem.getId(), Date.valueOf(LocalDate.now()), "LEND"));
                if (checkBorrowBookLabel.getText().equals("You have successfully borrowed this book")) {
                    checkBorrowBookLabel.setVisible(false);
                    CurrentMember.addListOfMemBook(bookItem);
                    CurrentMember.updateListOfLibBook(bookItem);
                    alert.showAndWait();
                    ((Stage) borrowBookBut.getScene().getWindow()).close();
                } else {
                    checkBorrowBookLabel.setVisible(true);
                }
            }
        } else if (event.getSource() == backToLibBookBut) {
            ((Stage)backToLibBookBut.getScene().getWindow()).close();
        }
    }
}
