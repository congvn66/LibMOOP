package com.example.proj.Controller;

import com.example.proj.Models.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class AddBookController implements Initializable {
    private static Stage stage;
    @FXML
    private TextArea addAuthorDescText;

    @FXML
    private Label authorNameLabel;

    @FXML
    private ChoiceBox<BookFormat> addBookFormatChoiceBox;

    @FXML
    private TextField addBookLocationText;

    @FXML
    private TextField addBookNumberText;

    @FXML
    private ChoiceBox<BookStatus> addBookStatusChoiceBox;

    @FXML
    private DatePicker addDateOfPurchasesPicker;

    @FXML
    private ChoiceBox<Boolean> addIsRefOnlyChoiceBox;

    @FXML
    private Label bookLanguageLabel;

    @FXML
    private Label bookPageLabel;

    @FXML
    private TextField addPriceText;

    @FXML
    private DatePicker addPublicationDatePicker;

    @FXML
    private Label bookPublisherLabel;

    @FXML
    private Label bookSubjectLabel;

    @FXML
    private Label bookTitleLabel;

    @FXML
    private Button backToLibMainBut;


    @FXML
    private Label checkBookFormat;

    @FXML
    private Label checkDateOfPurchase;

    @FXML
    private Label checkLocation;

    @FXML
    private Label checkNumber;

    @FXML
    private Label checkPrice;

    @FXML
    private Label checkPublication;

    @FXML
    private Label checkRef;

    @FXML
    private Label checkStatus;
    @FXML
    private Label checkAuthorDesc;

    @FXML
    private Button finalAddBookBut;
    @FXML
    private AnchorPane addBookAnchorPane;

    private List<Label> checkList;

    private boolean checkBook;

    private Alert alert;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<BookStatus> bookStatusObservableList = FXCollections.observableArrayList(BookStatus.AVAILABLE, BookStatus.LOST, BookStatus.LOANED, BookStatus.RESERVED);
        addBookStatusChoiceBox.getItems().addAll(bookStatusObservableList);
        ObservableList<Boolean> bookIsRefOnlyList = FXCollections.observableArrayList(Boolean.TRUE, Boolean.FALSE);
        addIsRefOnlyChoiceBox.getItems().addAll(bookIsRefOnlyList);
        ObservableList<BookFormat> bookFormatObservableList = FXCollections.observableArrayList(BookFormat.AUDIOBOOK, BookFormat.EBOOK, BookFormat.JOURNAL, BookFormat.HARDCOVER, BookFormat.MAGAZINE, BookFormat.NEWSPAPER, BookFormat.PAPERBACK);
        addBookFormatChoiceBox.getItems().addAll(bookFormatObservableList);
        checkList = new ArrayList<>();
        Collections.addAll(checkList, checkBookFormat, checkDateOfPurchase, checkLocation, checkNumber, checkPrice, checkPublication, checkRef, checkStatus, checkAuthorDesc);
        checkBook = true;
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Book has been added successfully!");
    }

    public static void setStage (Stage addStage) {
        stage = addStage;
    }
    public void setAddBookTab(ActionEvent event) {
        if (event.getSource() == backToLibMainBut) {
            stage.close();
        } else if (event.getSource() == finalAddBookBut) {
            String price = addPriceText.getText();
            String num = addBookNumberText.getText();
            try {
                if (price.equals("")) {
                    checkPrice.setText("Please fill in.");
                    checkPrice.setVisible(true);
                } else {
                    double priceNum = Double.parseDouble(addPriceText.getText());
                    if (priceNum <= 0) {
                        throw new NumberFormatException();
                    }
                    checkPrice.setVisible(false);
                }
            } catch (NumberFormatException n) {
                checkPrice.setText("Wrong format. Please enter again.");
                checkPrice.setVisible(true);
            }
            try {
                if (num.equals("")) {
                    checkNumber.setText("Please fill in.");
                    checkNumber.setVisible(true);
                } else {
                    int number = Integer.parseInt(addBookNumberText.getText());
                    if (number <= 0) {
                        throw new NumberFormatException();
                    }
                    checkNumber.setVisible(false);
                }
            } catch (NumberFormatException n) {
                checkNumber.setText("Wrong format. Please enter again.");
                checkNumber.setVisible(true);
            }
            String authorDesc = addAuthorDescText.getText();
            Boolean isRef = addIsRefOnlyChoiceBox.getValue();
            BookStatus bookStatus = addBookStatusChoiceBox.getValue();
            BookFormat bookFormat = addBookFormatChoiceBox.getValue();
            String bookLocation = addBookLocationText.getText();

            if (!authorDesc.equals("")) {
                checkAuthorDesc.setVisible(false);
            } else {
                checkAuthorDesc.setText("Please fill in the author description.");
                checkAuthorDesc.setVisible(true);
            }

            if (isRef != null) {
                checkRef.setVisible(false);
            } else {
                checkRef.setText("Please select reference status.");
                checkRef.setVisible(true);
            }

            if (bookStatus != null) {
                checkStatus.setVisible(false);
            } else {
                checkStatus.setText("Please select the book status.");
                checkStatus.setVisible(true);
            }

            if (bookFormat != null) {
                checkBookFormat.setVisible(false);
            } else {
                checkBookFormat.setText("Please select the book format.");
                checkBookFormat.setVisible(true);
            }
            Date dateOfPurchase = java.sql.Date.valueOf(LocalDate.now());
            Date publicationDate = new Date();


            if (!bookLocation.equals("")) {
                String[] tmp = bookLocation.split(" ");
                try {
                    if (bookLocation.contains("Rack") && tmp.length == 2) {
                        if (Integer.valueOf(tmp[1]) instanceof Integer) {
                            checkLocation.setVisible(false);
                        }
                    } else {
                        checkLocation.setText("Format for location must be:\nRack + Number (Example: Rack 10).");
                        checkLocation.setVisible(true);
                    }
                } catch (NumberFormatException n) {
                    checkLocation.setText("Name of a rack must be a number");
                    checkLocation.setVisible(true);
                }
            } else {
                checkLocation.setText("Please fill in the book location.");
                checkLocation.setVisible(true);
            }
            for (Label i : checkList) {
                if (i.isVisible()) {
                    checkBook = false;
                    break;
                }
            }
//            if (checkBook) {
//                // fix!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//                BookItem bookItem = new BookItem(isbn, title, subject, publisher, language, numberOfPage, authorName
//                        , authorDesc, null, isRef, Double.parseDouble(price), bookFormat, bookStatus, dateOfPurchase, publicationDate
//                        , Integer.parseInt(num), bookLocation, "");
//                CurrentLibrarian.getLibrarian().addBookItem(bookItem);
//                CurrentLibrarian.addBookObservableList(bookItem);
//                alert.showAndWait();
//                addBookAnchorPane.getChildren().stream().filter(node -> node instanceof TextField || node instanceof TextArea || node instanceof ChoiceBox<?> || node instanceof DatePicker).forEach(node -> {
//                    if (node instanceof TextField) {
//                        ((TextField) node).setText("");
//                    } else if (node instanceof TextArea) {
//                        ((TextArea) node).setText("");
//                    } else if (node instanceof ChoiceBox<?>) {
//                        ((ChoiceBox<?>) node).getSelectionModel().clearSelection();
//                    } else if (node instanceof DatePicker) {
//                        ((DatePicker) node).setValue(null);
//                    }
//                });
//            }
        }
    }
}
