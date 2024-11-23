package com.example.proj.Controller;
import com.example.proj.Models.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Date;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;

public class WatchBookController implements Initializable {
    private static Stage stage;
    @FXML
    private TextField idText;

    @FXML
    private TextArea authorDescText;

    @FXML
    private TextField authorNameText;

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
    private TextField isbnText;

    @FXML
    private TextField languageText;

    @FXML
    private TextField numberOfPageText;

    @FXML
    private TextField priceText;

    @FXML
    private DatePicker publicationDatePicker;

    @FXML
    private TextField publisherText;

    @FXML
    private TextField subjectText;

    @FXML
    private TextField titleText;

    private Alert alert;

    private static BookItem bookItem;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        isbnText.setText(bookItem.getISBN());
        titleText.setText(bookItem.getTitle());
        subjectText.setText(bookItem.getSubject());
        publisherText.setText(bookItem.getPublisher());
        languageText.setText(bookItem.getLanguage());
        numberOfPageText.setText(bookItem.getNumberOfPage());
        authorNameText.setText(bookItem.getAuthor().getName());
        authorDescText.setText(bookItem.getAuthor().getDescription());
        idText.setText(bookItem.getId());
        isRefOnlyChoiceBox.setValue(bookItem.getIsReferenceOnly());
        bookStatusChoiceBox.setValue(bookItem.getStatus());
        bookFormatChoiceBox.setValue(bookItem.getFormat());
        priceText.setText(String.valueOf(bookItem.getPrice()));
        dateOfPurchasesPicker.setValue(bookItem.getDateOfPurchase().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        publicationDatePicker.setValue(bookItem.getPublicationDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        bookNumberText.setText(String.valueOf(bookItem.getRack().getNumber()));
        bookLocationText.setText(bookItem.getRack().getLocationIdentifier());
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("You have successfully borrowed this book!");
    }
    public static void setStage(Stage watchStage) {
        stage = watchStage;
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
                checkBorrowBookLabel.setVisible(false);
                CurrentMember.getMember().basicActions(bookItem.getId(), Date.valueOf(LocalDate.now()), "LEND");
                CurrentMember.addListOfMemBook(bookItem);
                CurrentMember.updateListOfLibBook(bookItem);
                alert.showAndWait();
                stage.close();
            }
        } else if (event.getSource() == backToLibBookBut) {
            stage.close();
        }
    }
}
