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
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.time.ZoneId;
import java.util.*;
public class ManuallyAddBookController implements Initializable{
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
        @FXML
        private AnchorPane addBookAnchorPane;

        @FXML
        private Button importImage;

        @FXML
        private ImageView image;

        private Alert confirmationAlert;

        private List<Label> checkList;

        private boolean checkBook;

        private Alert informationAlert;

        private File importedFile;

        private ImageImportService imageImportService;

        String imagePath;

        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {
            ObservableList<BookStatus> bookStatusObservableList = FXCollections.observableArrayList(BookStatus.AVAILABLE, BookStatus.LOST, BookStatus.LOANED, BookStatus.RESERVED);
            addBookStatusChoiceBox.getItems().addAll(bookStatusObservableList);
            ObservableList<Boolean> bookIsRefOnlyList = FXCollections.observableArrayList(Boolean.TRUE, Boolean.FALSE);
            addIsRefOnlyChoiceBox.getItems().addAll(bookIsRefOnlyList);
            ObservableList<BookFormat> bookFormatObservableList = FXCollections.observableArrayList(BookFormat.AUDIOBOOK, BookFormat.EBOOK, BookFormat.JOURNAL, BookFormat.HARDCOVER, BookFormat.MAGAZINE, BookFormat.NEWSPAPER, BookFormat.PAPERBACK);
            addBookFormatChoiceBox.getItems().addAll(bookFormatObservableList);
            checkList = new ArrayList<>();
            Collections.addAll(checkList, checkISBN, checkAuthorName, checkLanguage, checkBookFormat, checkDateOfPurchase, checkLocation, checkNumber, checkNumberOfPages, checkPrice, checkPublication, checkPublisher, checkRef, checkStatus, checkSubject, checkTitle, checkAuthorDesc);
            checkBook = true;
            setConfirmationAlert();
            setInformationAlert();
            imageImportService = new ImageImportService();
            importedFile = null;
            imagePath = null;
        }

        public void setInformationAlert() {
            informationAlert = new Alert(Alert.AlertType.INFORMATION);
            informationAlert.setContentText("Book has been added successfully!");
        }
        public void setAddBookTab(ActionEvent event) {
            if (event.getSource() == backToLibMainBut) {
                ((Stage)backToLibMainBut.getScene().getWindow()).close();
            } else if (event.getSource() == finalAddBookBut) {
                checkBook = true;
                String isbn = addISBNText.getText();
                setCheckISBN(isbn);
                String numberOfPage = addNumberOfPageText.getText();
                setCheckNumberOfPages(numberOfPage);
                String price = addPriceText.getText();
                setCheckPrice(price);
                String num = addBookNumberText.getText();
                setCheckNumber(num);
                String title = addTitleText.getText();
                setCheckTitle(title);
                String subject = addSubjectText.getText();
                setCheckSubject(subject);
                String publisher = addPublisherText.getText();
                setCheckPublisher(publisher);
                String language = addLanguageText.getText();
                setCheckLanguage(language);
                String authorName = addAuthorNameText.getText();
                setCheckAuthorName(authorName);
                String authorDesc = addAuthorDescText.getText();
                setCheckAuhtorDesc(authorDesc);
                Boolean isRef = addIsRefOnlyChoiceBox.getValue();
                setCheckIsRef(isRef);
                BookStatus bookStatus = addBookStatusChoiceBox.getValue();
                setCheckBookStatus(bookStatus);
                BookFormat bookFormat = addBookFormatChoiceBox.getValue();
                setCheckBookFormat(bookFormat);
                String bookLocation = addBookLocationText.getText();
                setCheckLocation(bookLocation);
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
                        checkDateOfPurchase.setText("Date of purchase must be \nafter publication date.");
                        checkDateOfPurchase.setVisible(true);
                        checkPublication.setText("Publication date must be \nbefore date of purchase.");
                        checkPublication.setVisible(true);
                    } else {
                        checkPublication.setVisible(false);
                    }
                } else {
                    checkPublication.setText("Please select the publication date.");
                    checkPublication.setVisible(true);
                }

                for (Label i : checkList) {
                    if (i.isVisible()) {
                        checkBook = false;
                        break;
                    }
                }
                if (checkBook) {
                    if (importedFile == null) {
                        imagePath = "image.png";
                    } else {
                        imagePath = importedFile.getName();
                        imageImportService.moveImageToFolder(importedFile);
                        importedFile = null;
                    }
                    BookItem bookItem = new BookItem(isbn, title, subject, publisher, language, numberOfPage, authorName
                            , authorDesc, null, isRef, Double.parseDouble(price), bookFormat, bookStatus, dateOfPurchase, publicationDate
                            , Integer.parseInt(num), bookLocation, imagePath);
                    CurrentLibrarian.getLibrarian().addBookItem(bookItem);
                    CurrentLibrarian.addBookObservableList(bookItem);
                    informationAlert.showAndWait();
                    addBookAnchorPane.getChildren().stream().filter(node -> node instanceof TextField || node instanceof TextArea || node instanceof ChoiceBox<?> || node instanceof DatePicker || node instanceof ImageView).forEach(node -> {
                        if (node instanceof TextField) {
                            ((TextField) node).setText("");
                        } else if (node instanceof TextArea) {
                            ((TextArea) node).setText("");
                        } else if (node instanceof ChoiceBox<?>) {
                            ((ChoiceBox<?>) node).getSelectionModel().clearSelection();
                        } else if (node instanceof DatePicker) {
                            ((DatePicker) node).setValue(null);
                        } else if (node instanceof ImageView) {
                            ((ImageView)node).setImage(null);
                        }
                    });
                }
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

    public void setBookThumbnail(File importedFile) {
            image.setImage(new Image(importedFile.toURI().toString()));
    }

    public void setCheckISBN(String isbn) {
        try {
            if (isbn == "") {
                checkISBN.setText("Please fill in.");
                checkISBN.setVisible(true);
            } else {
                long isbnNum = Long.parseLong(addISBNText.getText());
                if (isbn.length() == 13 || isbn.length() == 10) {
                    checkISBN.setVisible(false);
                } else {
                    checkISBN.setText("ISBN must contains 10 numbers or 13 numbers.");
                    checkISBN.setVisible(true);
                }
            }
        } catch (NumberFormatException n) {
            checkISBN.setText("Wrong format. Please enter again.");
            checkISBN.setVisible(true);
        }
    }

    public void setCheckNumberOfPages(String numberOfPage) {
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
    }

    public void setCheckPrice(String price) {
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

    public void setCheckNumber(String num) {
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

    public void setCheckLocation(String bookLocation) {
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

    public void setConfirmationAlert() {
        confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setContentText("There are already file with the same name in the book store \nDo you still want to replace it?");
        confirmationAlert.getButtonTypes().setAll(ButtonType.YES, ButtonType.CANCEL);
    }

    public void setCheckTitle(String title){
        if (!title.equals("")) {
            checkTitle.setVisible(false);
        } else {
            checkTitle.setText("Please fill in the title.");
            checkTitle.setVisible(true);
        }
    }

    public void setCheckSubject(String subject){
        if (!subject.equals("")) {
            checkSubject.setVisible(false);
        } else {
            checkSubject.setText("Please fill in the subject.");
            checkSubject.setVisible(true);
        }
    }

    public void setCheckPublisher(String publisher){
        if (!publisher.equals("")) {
            checkPublisher.setVisible(false);
        } else {
            checkPublisher.setText("Please fill in the publisher.");
            checkPublisher.setVisible(true);
        }

    }

    public void setCheckLanguage(String language){
        if (!language.equals("")) {
            checkLanguage.setVisible(false);
        } else {
            checkLanguage.setText("Please fill in the language.");
            checkLanguage.setVisible(true);
        }
    }

    public void setCheckAuthorName(String authorName){
        if (!authorName.equals("")) {
            checkAuthorName.setVisible(false);
        } else {
            checkAuthorName.setText("Please fill in the author name.");
            checkAuthorName.setVisible(true);
        }
    }

    public void setCheckAuhtorDesc(String authorDesc){
        if (!authorDesc.equals("")) {
            checkAuthorDesc.setVisible(false);
        } else {
            checkAuthorDesc.setText("Please fill in the author description.");
            checkAuthorDesc.setVisible(true);
        }
    }

    public void setCheckIsRef(Boolean isRef){
        if (isRef != null) {
            checkRef.setVisible(false);
        } else {
            checkRef.setText("Please select reference status.");
            checkRef.setVisible(true);
        }
    }

    public void setCheckBookStatus(BookStatus bookStatus){
        if (bookStatus != null) {
            checkStatus.setVisible(false);
        } else {
            checkStatus.setText("Please select the book status.");
            checkStatus.setVisible(true);
        }
    }

    public void setCheckBookFormat(BookFormat bookFormat){
        if (bookFormat != null) {
            checkBookFormat.setVisible(false);
        } else {
            checkBookFormat.setText("Please select the book format.");
            checkBookFormat.setVisible(true);
        }
    }
}
