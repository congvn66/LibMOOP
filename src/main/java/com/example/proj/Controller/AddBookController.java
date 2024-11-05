package com.example.proj.Controller;

import com.example.proj.Models.AccountStatus;
import com.example.proj.Models.BookFormat;
import com.example.proj.Models.BookItem;
import com.example.proj.Models.BookStatus;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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
    private TextField addAuthorNameText;

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
    private TextField addISBNText;

    @FXML
    private TextField addIdText;

    @FXML
    private ChoiceBox<Boolean> addIsRefOnlyChoiceBox;

    @FXML
    private TextField addLanguageText;

    @FXML
    private TextField addNumberOfPageText;

    @FXML
    private TextField addPriceText;

    @FXML
    private DatePicker addPublicationDatePicker;

    @FXML
    private TextField addPublisherText;

    @FXML
    private TextField addSubjectText;

    @FXML
    private TextField addTitleText;

    @FXML
    private Button backToLibMainBut;

    @FXML
    private Label checkAuthorName;

    @FXML
    private Label checkBookFormat;

    @FXML
    private Label checkDateOfPurchase;

    @FXML
    private Label checkID;

    @FXML
    private Label checkISBN;

    @FXML
    private Label checkLanguage;

    @FXML
    private Label checkLocation;

    @FXML
    private Label checkNumber;

    @FXML
    private Label checkNumberOfPages;

    @FXML
    private Label checkPrice;

    @FXML
    private Label checkPublication;

    @FXML
    private Label checkPublisher;

    @FXML
    private Label checkRef;

    @FXML
    private Label checkStatus;

    @FXML
    private Label checkSubject;

    @FXML
    private Label checkTitle;
    @FXML
    private Label checkAuthorDesc;

    @FXML
    private Button finalAddBookBut;

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
        Collections.addAll(checkList, checkID, checkISBN, checkAuthorName, checkLanguage, checkBookFormat, checkDateOfPurchase, checkLocation, checkNumber, checkNumberOfPages, checkPrice, checkPublication, checkPublisher, checkRef, checkStatus, checkSubject, checkTitle, checkAuthorDesc);
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
            String isbn = addISBNText.getText();
            String numberOfPage = addNumberOfPageText.getText();
            String id = addIdText.getText();
            String price = addPriceText.getText();
            String num = addBookNumberText.getText();
            try {
                if (isbn == "") {
                    checkISBN.setText("Please fill in.");
                    checkISBN.setVisible(true);
                } else {
                    long isbnNum = Long.parseLong(addISBNText.getText());
                    if (isbn.length() == 13) {
                        checkISBN.setVisible(false);
                    } else {
                        checkISBN.setText("ISBN must contains 13 numbers.");
                        checkISBN.setVisible(true);
                    }
                }
            } catch (NumberFormatException n) {
                checkISBN.setText("Wrong format. Please enter again.");
                checkISBN.setVisible(true);
            }
            try {
                if (numberOfPage.equals("")) {
                    checkNumberOfPages.setText("Please fill in");
                    checkNumberOfPages.setVisible(true);
                } else {
                    int pagesNum = Integer.parseInt(addNumberOfPageText.getText());
                    if (pagesNum <= 0) {
                        throw new NumberFormatException();
                    }
                    checkNumberOfPages.setVisible(false);
                }
            } catch (NumberFormatException n) {
                checkNumberOfPages.setText("Wrong format. Please enter again");
                checkNumberOfPages.setVisible(true);
            }
            try {
                if (id.equals("")) {
                    checkID.setText("Please fill in.");
                    checkID.setVisible(true);
                } else {
                    int idNum = Integer.parseInt(addIdText.getText());
                    if (idNum <= 0) {
                        throw new NumberFormatException();
                    }
                    if (CurrentLibrarian.getLibrarian().getCatalog().getBookId().containsKey(id)) {
                        checkID.setText("Id has already existed");
                        checkID.setVisible(true);
                        addIdText.setText("");
                    } else {
                        checkID.setVisible(false);
                    }
                }
            } catch (NumberFormatException n) {
                checkID.setText("Wrong format. Please enter again.");
                checkID.setVisible(true);
            }
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
            String title = addTitleText.getText();
            String subject = addSubjectText.getText();
            String publisher = addPublisherText.getText();
            String language = addLanguageText.getText();
            String authorName = addAuthorNameText.getText();
            String authorDesc = addAuthorDescText.getText();
            Boolean isRef = addIsRefOnlyChoiceBox.getValue();
            BookStatus bookStatus = addBookStatusChoiceBox.getValue();
            BookFormat bookFormat = addBookFormatChoiceBox.getValue();
            String bookLocation = addBookLocationText.getText();

            if (!title.equals("")) {
                checkTitle.setVisible(false);
            } else {
                checkTitle.setText("Please fill in the title.");
                checkTitle.setVisible(true);
            }

            if (!subject.equals("")) {
                checkSubject.setVisible(false);
            } else {
                checkSubject.setText("Please fill in the subject.");
                checkSubject.setVisible(true);
            }

            if (!publisher.equals("")) {
                checkPublisher.setVisible(false);
            } else {
                checkPublisher.setText("Please fill in the publisher.");
                checkPublisher.setVisible(true);
            }

            if (!language.equals("")) {
                checkLanguage.setVisible(false);
            } else {
                checkLanguage.setText("Please fill in the language.");
                checkLanguage.setVisible(true);
            }

            if (!authorName.equals("")) {
                checkAuthorName.setVisible(false);
            } else {
                checkAuthorName.setText("Please fill in the author name.");
                checkAuthorName.setVisible(true);
            }

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
            Date dateOfPurchase = new Date();
            Date publicationDate = new Date();
            if (addDateOfPurchasesPicker.getValue() != null) {
                dateOfPurchase = Date.from(addDateOfPurchasesPicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
                checkDateOfPurchase.setVisible(false);
            } else {
                checkDateOfPurchase.setText("Please select the date of purchase.");
                checkDateOfPurchase.setVisible(true);
            }

            if (addPublicationDatePicker.getValue() != null && addDateOfPurchasesPicker.getValue() != null) {
                publicationDate = Date.from(addPublicationDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
                if (publicationDate.after(dateOfPurchase)) {
                    checkDateOfPurchase.setText("Date of purchase must be after publication date.");
                    checkDateOfPurchase.setVisible(true);
                    checkPublication.setText("Publication date must be before date of purchase.");
                    checkPublication.setVisible(true);
                } else {
                    checkPublication.setVisible(false);
                }
            } else {
                checkPublication.setText("Please select the publication date.");
                checkPublication.setVisible(true);
            }


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
            if (checkBook) {
                BookItem bookItem = new BookItem(isbn, title, subject, publisher, language, numberOfPage, authorName
                        , authorDesc, id, isRef, Double.parseDouble(price), bookFormat, bookStatus, dateOfPurchase, publicationDate
                        , Integer.parseInt(num), bookLocation);
                CurrentLibrarian.getLibrarian().addBookItem(bookItem);
                CurrentLibrarian.addBookObservableList(bookItem);
                alert.showAndWait();
            }
        }
    }
}
