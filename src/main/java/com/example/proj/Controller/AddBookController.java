package com.example.proj.Controller;

import com.example.proj.Models.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class AddBookController implements Initializable {
    @FXML
    private DatePicker addDateOfPurchasesPicker;
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
    private Button backToApiSearchBut;


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

    @FXML
    private ImageView bookThumbnail;

    @FXML
    private Button importImage;

    @FXML
    private Label bookISBN;

    private List<Label> checkList;

    private boolean checkBook;

    private Alert informationAlert;

    private Alert confirmationAlert;

    private static BookItem bookItem;

    private File importedFile;

    private ImageImportService imageImportService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<BookStatus> bookStatusObservableList = FXCollections.observableArrayList(BookStatus.AVAILABLE, BookStatus.LOST, BookStatus.LOANED, BookStatus.RESERVED);
        addBookStatusChoiceBox.getItems().addAll(bookStatusObservableList);
        ObservableList<Boolean> bookIsRefOnlyList = FXCollections.observableArrayList(Boolean.TRUE, Boolean.FALSE);
        addIsRefOnlyChoiceBox.getItems().addAll(bookIsRefOnlyList);
        ObservableList<BookFormat> bookFormatObservableList = FXCollections.observableArrayList(BookFormat.AUDIOBOOK, BookFormat.EBOOK, BookFormat.JOURNAL, BookFormat.HARDCOVER, BookFormat.MAGAZINE, BookFormat.NEWSPAPER, BookFormat.PAPERBACK);
        addBookFormatChoiceBox.getItems().addAll(bookFormatObservableList);
        checkList = new ArrayList<>();
        Collections.addAll(checkList, checkBookFormat, checkLocation, checkNumber, checkPrice, checkRef, checkStatus, checkAuthorDesc);
        checkBook = true;
        informationAlert = new Alert(Alert.AlertType.INFORMATION);
        informationAlert.setContentText("Book has been added successfully!");
        setConfirmationAlert();
        setBook(bookItem);
        imageImportService = new ImageImportService();
    }

    public static void setBookItem(BookItem newBookItem) {
        bookItem = newBookItem;
    }

    public void setConfirmationAlert() {
        confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setContentText("There are already file with the same name in the book store \nDo you still want to replace it?");
        confirmationAlert.getButtonTypes().setAll(ButtonType.YES, ButtonType.CANCEL);
    }
    public void setAddBookTab(ActionEvent event) {
        if (event.getSource() == backToApiSearchBut) {
            ((Stage)backToApiSearchBut.getScene().getWindow()).close();
        } else if (event.getSource() == importImage) {
            importedFile = imageImportService.importImage((Stage)importImage.getScene().getWindow());
            if (importedFile != null) {
                if (!imageImportService.movableImageToFolder(importedFile)) {
                    if (confirmationAlertTab()) {
                        setBookThumbnail(importedFile);
                    } else {
                        importedFile = null;
                    }
                } else {
                    setBookThumbnail(importedFile);
                }
            }
        }
        else if (event.getSource() == finalAddBookBut) {
            checkBook = true;
            String price = addPriceText.getText();
            checkPriceTab(price);

            String num = addBookNumberText.getText();
            checkBookNumberTab(num);

            String authorDesc = addAuthorDescText.getText();
            if (!authorDesc.equals("")) {
                checkAuthorDesc.setVisible(false);
            } else {
                checkAuthorDesc.setText("Please fill in the author description.");
                checkAuthorDesc.setVisible(true);
            }

            Boolean isRef = addIsRefOnlyChoiceBox.getValue();
            if (isRef != null) {
                checkRef.setVisible(false);
            } else {
                checkRef.setText("Please select reference status.");
                checkRef.setVisible(true);
            }

            BookStatus bookStatus = addBookStatusChoiceBox.getValue();
            if (bookStatus != null) {
                checkStatus.setVisible(false);
            } else {
                checkStatus.setText("Please select the book status.");
                checkStatus.setVisible(true);
            }

            BookFormat bookFormat = addBookFormatChoiceBox.getValue();
            if (bookFormat != null) {
                checkBookFormat.setVisible(false);
            } else {
                checkBookFormat.setText("Please select the book format.");
                checkBookFormat.setVisible(true);
            }

            String bookLocation = addBookLocationText.getText();
            checkBookLocationTab(bookLocation);

            Date addDateOfPurchasesPicker = java.sql.Date.valueOf(LocalDate.now());

            for (Label i : checkList) {
                if (i.isVisible()) {
                    checkBook = false;
                    break;
                }
            }
            if (checkBook) {
                if (importedFile != null) {
                    imageImportService.moveImageToFolder(importedFile);
                }

                BookItem bookItem = new BookItem(this.bookItem.getISBN(), this.bookItem.getTitle(), this.bookItem.getSubject(), this.bookItem.getPublisher()
                        , this.bookItem.getLanguage(), this.bookItem.getNumberOfPage(), this.bookItem.getAuthor().getName(), authorDesc, null, isRef
                        , Double.parseDouble(price), bookFormat, bookStatus, addDateOfPurchasesPicker, this.bookItem.getPublicationDate(), Integer.parseInt(num),
                        bookLocation, importedFile == null ? this.bookItem.getImgName() : importedFile.getName());
                CurrentLibrarian.getLibrarian().addBookItem(bookItem);
                CurrentLibrarian.addBookObservableList(bookItem);

                informationAlert.showAndWait();
                ((Stage)backToApiSearchBut.getScene().getWindow()).close();
            }
        }
    }

    public void checkPriceTab(String price) {
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
    }

    public void checkBookNumberTab(String num) {
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
    }

    public void checkBookLocationTab(String bookLocation) {
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
    }

    public boolean confirmationAlertTab() {
        Optional<ButtonType> type = confirmationAlert.showAndWait();
        if (type.isPresent()) {
            if (type.get() == ButtonType.YES) {
                return true;
            }
        }
        return false;
    }
    public void setBookThumbnail(File fileImage) {
        bookThumbnail.setImage(new Image(fileImage.toURI().toString()));
    }

    public void setBook(BookItem bookItem) {
        bookTitleLabel.setText(bookItem.getTitle());
        bookPublisherLabel.setText(bookItem.getPublisher());
        bookSubjectLabel.setText(bookItem.getSubject());
        bookLanguageLabel.setText(bookItem.getLanguage());
        bookPageLabel.setText(bookItem.getNumberOfPage());
        authorNameLabel.setText(bookItem.getAuthor().getName());
        addPublicationDatePicker.setValue(bookItem.getPublicationDate() != null ? bookItem.getPublicationDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null);
        addDateOfPurchasesPicker.setValue(LocalDate.now());
        bookISBN.setText(bookItem.getISBN());
        if (!bookItem.checkURL()) {
            importImage.setDisable(false);
            bookThumbnail.setImage(new Image(getClass().getResource(bookItem.generateImagePathFromImageName(bookItem.getImgName())).toExternalForm()));
        } else {
            importImage.setDisable(true);
            bookThumbnail.setImage((new Image(bookItem.getImgName())));
        }
    }

}
