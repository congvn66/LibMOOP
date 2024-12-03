package com.example.proj.Controller;

import com.example.proj.Models.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;
import java.util.ResourceBundle;

public class MemMainController implements Initializable {
    @FXML
    private AnchorPane BookManagementTab;

    @FXML
    private Button ExitBut;

    @FXML
    private AnchorPane MainTab;

    @FXML
    private AnchorPane MemBookTab;

    @FXML
    private Button cancelBookBut;

    @FXML
    private Button backBut;

    @FXML
    private TableColumn<BookItem, String> bookAuthorColumn;

    @FXML
    private TableColumn<BookItem, String> bookAuthorDesColumn;

    @FXML
    private TableColumn<BookItem, String> bookIdColumn;

    @FXML
    private Label bookLoanedNum;

    @FXML
    private TableColumn<BookItem, String> bookLocationColumn;

    @FXML
    private Label bookLostNum;

    @FXML
    private Button bookManageBut;

    @FXML
    private TableColumn<BookItem, Integer> bookNumberColumn;

    @FXML
    private TableColumn<BookItem, Double> bookPriceColumn;

    @FXML
    private TableColumn<BookItem, Boolean> bookRefColumn;

    @FXML
    private Label bookReservedNum;

    @FXML
    private TableColumn<BookItem, BookStatus> bookStatusColumn;

    @FXML
    private TableColumn<BookItem, String> bookSubjectColumn;

    @FXML
    private TableView<BookItem> bookTable;

    @FXML
    private TableColumn<BookItem, String> bookTitleColumn;

    @FXML
    private Label booksAvailableNum;

    @FXML
    private Button memCancelBookBut;

    @FXML
    private Label checkSearchLabel;

    @FXML
    private Button myBookManageBut;

    @FXML
    private TableColumn<BookItem, String> memBookAuthorColumn;

    @FXML
    private TableColumn<BookItem, String> memBookAuthorDescColumn;


    @FXML
    private TableColumn<BookItem, String> memBookIdColumn;

    @FXML
    private TableColumn<BookItem, String> memBookSubjectColumn;

    @FXML
    private TableView<BookItem> memBookTable;

    @FXML
    private TableColumn<BookItem, String> memBookTitleColumn;

    @FXML
    private Button memLostBookBut;

    @FXML
    private Label percentage;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Button renewBookBut;

    @FXML
    private Button returnBookBut;

    @FXML
    private Button searchBookBut;

    @FXML
    private ChoiceBox<String> searchOptionChoiceBox;

    @FXML
    private TextField searchTextField;

    @FXML
    private Label totalBooks;

    @FXML
    private Label yourBookNum;

    @FXML
    private Button watchBookBut;

    @FXML
    private Label welcomeText;

    @FXML
    private VBox notificationVBox;

    @FXML
    private Label memPoint;

    private Alert alertInfo;

    private Alert alertConfirm;


    public void setLibBookTable(ObservableList a) {
        bookIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        bookTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        bookSubjectColumn.setCellValueFactory(new PropertyValueFactory<>("subject"));
        bookRefColumn.setCellValueFactory(new PropertyValueFactory<>("isReferenceOnly"));
        bookStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        bookPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        bookAuthorColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getAuthor().getName());
        });
        bookAuthorDesColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getAuthor().getDescription());
        });
        bookNumberColumn.setCellValueFactory(cellData -> {
            return new SimpleObjectProperty<>(cellData.getValue().getRack().getNumber());
        });
        bookLocationColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getRack().getLocationIdentifier());
        });
        bookTable.setItems(a);
    }

    public void setMemBookTable(ObservableList a) {
        memBookIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        memBookTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        memBookTitleColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(item);
                    setWrapText(true);
                }
            }
        });
        memBookSubjectColumn.setCellValueFactory(new PropertyValueFactory<>("subject"));
        memBookAuthorColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getAuthor().getName());
        });
        memBookAuthorDescColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getAuthor().getDescription());
        });
        memBookTable.setItems(a);
    }

    public void setNotificationVBox(ObservableList<Notification> a) {
        notificationVBox.getChildren().clear();
        notificationVBox.setSpacing(10);
        for (Notification i : a) {
            Label tmp = new Label(i.toString());
            tmp.setWrapText(true);
            tmp.setTextAlignment(TextAlignment.CENTER);
            notificationVBox.getChildren().add(tmp);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        totalBooks.setText(CurrentMember.getMember().getCatalog().getTotalBooks().get() + "/5000");
        progressBar.setProgress((double) CurrentMember.getMember().getCatalog().getBookId().size() / 5000);
        percentage.setText(String.valueOf((double) CurrentMember.getMember().getCatalog().getBookId().size() / 50 + "%"));
        CurrentMember.getMember().getCatalog().getTotalBooks().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> {
                totalBooks.setText(CurrentMember.getMember().getCatalog().getTotalBooks().get() + "/5000");
                progressBar.setProgress((double) CurrentMember.getMember().getCatalog().getTotalBooks().get() / 5000);
                percentage.setText((double) CurrentMember.getMember().getCatalog().getTotalBooks().get() / 50 + "%");
            });
        });
        memPoint.setText(String.valueOf(CurrentMember.getMember().getPoint()));
        CurrentMember.getMember().getPointProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> {
                memPoint.setText(String.valueOf(CurrentMember.getMember().getPoint()));
            });
        });
        booksAvailableNum.setText(String.valueOf(CurrentMember.getMember().getCatalog().getBookStatus().get(BookStatus.AVAILABLE.toString()).size()));
        CurrentMember.getMember().getCatalog().getBookStatus().get(BookStatus.AVAILABLE.toString()).addListener((ListChangeListener<BookItem>) change -> {
            Platform.runLater(() -> {
                booksAvailableNum.setText(String.valueOf(CurrentMember.getMember().getCatalog().getBookStatus().get(BookStatus.AVAILABLE.toString()).size()));
            });
        });
        bookReservedNum.setText(String.valueOf(CurrentMember.getMember().getCatalog().getBookStatus().get(BookStatus.RESERVED.toString()).size()));
        CurrentMember.getMember().getCatalog().getBookStatus().get(BookStatus.RESERVED.toString()).addListener((ListChangeListener<BookItem>) change -> {
            Platform.runLater(() -> {
                bookReservedNum.setText(String.valueOf(CurrentMember.getMember().getCatalog().getBookStatus().get(BookStatus.RESERVED.toString()).size()));
            });
        });
        bookLoanedNum.setText(String.valueOf(CurrentMember.getMember().getCatalog().getBookStatus().get(BookStatus.LOANED.toString()).size()));
        CurrentMember.getMember().getCatalog().getBookStatus().get(BookStatus.LOANED.toString()).addListener((ListChangeListener<BookItem>) change -> {
            Platform.runLater(() -> {
                bookLoanedNum.setText(String.valueOf(CurrentMember.getMember().getCatalog().getBookStatus().get(BookStatus.LOANED.toString()).size()));
            });
        });
        bookLostNum.setText(String.valueOf(CurrentMember.getMember().getCatalog().getBookStatus().get(BookStatus.LOST.toString()).size()));
        CurrentMember.getMember().getCatalog().getBookStatus().get(BookStatus.LOST.toString()).addListener((ListChangeListener<BookItem>) change -> {
            Platform.runLater(() -> {
                bookLostNum.setText(String.valueOf(CurrentMember.getMember().getCatalog().getBookStatus().get(BookStatus.LOST.toString()).size()));
            });
        });
        yourBookNum.setText(String.valueOf(CurrentMember.getMember().getLogger().getMemListOfLog().size()));
        CurrentMember.getMember().getLogger().getMemListOfLog().addListener((ListChangeListener<Log>) change -> {
            Platform.runLater(() -> {
                yourBookNum.setText(String.valueOf(CurrentMember.getMember().getLogger().getMemListOfLog().size()));
            });
        });

        welcomeText.setText("Hello " + CurrentMember.getMember().getId());
        setLibBookTable(CurrentMember.getListOfLibBook());
        setMemBookTable(CurrentMember.getListOfMemBook());
        ObservableList<String> searchOptionList = FXCollections.observableArrayList("ID", "Title", "Author", "Subject", "Status");
        searchOptionChoiceBox.getItems().addAll(searchOptionList);
        bookTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                WatchBookController.setBookItem(newValue);
            }
        });
        setNotificationVBox(CurrentMember.getMember().getNotificationBox().getNotifications());
        CurrentMember.getMember().getNotificationBox().getNotifications().addListener((ListChangeListener<Notification>) change -> {
            setNotificationVBox(CurrentMember.getMember().getNotificationBox().getNotifications());
        });
        alertInfo = new Alert(Alert.AlertType.INFORMATION);
        alertConfirm = new Alert(Alert.AlertType.CONFIRMATION);
        alertConfirm.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
    }

    private void disableMainButton(Button[] list, Button x, boolean backToMain) {
        for (Button i : list) {
            if (i.equals(x) && !backToMain) {
                i.setDisable(true);
            } else {
                i.setDisable(false);
            }
        }
    }

    public void setMainTab(ActionEvent event) throws IOException {
        Button[] listOfMainBut = {bookManageBut, myBookManageBut};
        if (event.getSource() == bookManageBut) {
            BookManagementTab.toFront();
            backBut.toFront();
            disableMainButton(listOfMainBut, bookManageBut, false);
        } else if (event.getSource() == myBookManageBut) {
            MemBookTab.toFront();
            backBut.toFront();
            disableMainButton(listOfMainBut, myBookManageBut, false);
        } else if (event.getSource() == backBut) {
            MainTab.toFront();
            ExitBut.toFront();
            disableMainButton(listOfMainBut, myBookManageBut, true);
        } else if (event.getSource() == ExitBut) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/proj/FXML/Login.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Library Management System");
            stage.setScene(scene);
            stage.setResizable(false);
            ExitBut.getScene().getWindow().hide();
            stage.show();
        }
    }

    public void setBookManagementTab(ActionEvent event) throws IOException {
        if (event.getSource() == searchBookBut) {
            try {
                if (searchOptionChoiceBox != null) {
                    String searchString = searchTextField.getText().trim();
                    if (searchString.isEmpty()) {
                        checkSearchLabel.setText("Please fill in");
                        checkSearchLabel.setVisible(true);
                    } else {
                        ObservableList<BookItem> tmp = FXCollections.observableArrayList();
                        switch (searchOptionChoiceBox.getValue()) {
                            case "ID":
                                BookItem bookItem = CurrentMember.getMember().getCatalog().findBookById(searchString);
                                if (bookItem != null) {
                                    tmp = FXCollections.singletonObservableList(bookItem);
                                    CurrentMember.setListOfLibBook(FXCollections.observableArrayList(tmp));
                                    setLibBookTable(CurrentMember.getListOfLibBook());
                                    checkSearchLabel.setVisible(false);
                                } else {
                                    checkSearchLabel.setText("ID doesn't exist");
                                    checkSearchLabel.setVisible(true);
                                }
                                break;
                            case "Author":
                                tmp = FXCollections.observableArrayList(CurrentMember.getMember().getCatalog().findBooksByAuthor(searchString));
                                if (tmp.size() != 0) {
                                    CurrentMember.setListOfLibBook(tmp);
                                    setLibBookTable(CurrentMember.getListOfLibBook());
                                    checkSearchLabel.setVisible(false);
                                } else {
                                    checkSearchLabel.setText("Author doesn't exist");
                                    checkSearchLabel.setVisible(true);
                                }
                                break;
                            case "Subject":
                                tmp = FXCollections.observableArrayList(CurrentMember.getMember().getCatalog().findBooksBySubject(searchString));
                                if (tmp.size() != 0) {
                                    CurrentMember.setListOfLibBook(tmp);
                                    setLibBookTable(CurrentMember.getListOfLibBook());
                                    checkSearchLabel.setVisible(false);
                                } else {
                                    checkSearchLabel.setText("Subject doesn't exist");
                                    checkSearchLabel.setVisible(true);
                                }
                                break;
                            case "Title":
                                tmp = FXCollections.observableArrayList(CurrentMember.getMember().getCatalog().findBooksByTitle(searchString));
                                if (tmp.size() != 0) {
                                    CurrentMember.setListOfLibBook(tmp);
                                    setLibBookTable(CurrentMember.getListOfLibBook());
                                    checkSearchLabel.setVisible(false);
                                } else {
                                    checkSearchLabel.setText("Title doesn't exist");
                                    checkSearchLabel.setVisible(true);
                                }
                                break;
                            case "Status":
                                tmp = FXCollections.observableArrayList(CurrentMember.getMember().getCatalog().findBooksByStatus(searchString.toUpperCase()));
                                if (tmp.size() != 0) {
                                    CurrentMember.setListOfLibBook(tmp);
                                    setLibBookTable(CurrentMember.getListOfLibBook());
                                    checkSearchLabel.setVisible(false);
                                } else {
                                    checkSearchLabel.setText("Status doesn't exist");
                                    checkSearchLabel.setVisible(true);
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }
            } catch (Exception e) {
                checkSearchLabel.setText("Please select search option");
                checkSearchLabel.setVisible(true);
                e.printStackTrace();
            }
        } else if (event.getSource() == cancelBookBut) {
            searchTextField.setText("");
            CurrentMember.returnListOfLibBook();
            setLibBookTable(CurrentMember.getListOfLibBook());
            checkSearchLabel.setVisible(false);
            bookTable.getSelectionModel().select(null);
        } else if (event.getSource() == watchBookBut) {
            if (bookTable.getSelectionModel().getSelectedItem() != null) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/proj/FXML/WatchBook.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(fxmlLoader.load());
                stage.setTitle("Library Management System");
                stage.setScene(scene);
                stage.initStyle(StageStyle.UNDECORATED);
                stage.setResizable(false);
                stage.show();
            }
        }
    }

    public void setMemBookTab(ActionEvent event) throws ParseException {
        if (event.getSource() == memCancelBookBut) {
            memBookTable.getSelectionModel().select(null);
        } else if (event.getSource() == renewBookBut) {
            if (memBookTable.getSelectionModel().getSelectedItem() != null) {
                BookItem renewBook = memBookTable.getSelectionModel().getSelectedItem();
                alertInfo.setContentText(CurrentMember.getMember().basicActions(renewBook.getId(), Date.valueOf(LocalDate.now()), "RENEW"));
                CurrentMember.updateListOfMemBook(renewBook);
                CurrentMember.updateListOfLibBook(renewBook);
                alertInfo.showAndWait();
            }
        } else if (event.getSource() == returnBookBut) {
            if (memBookTable.getSelectionModel().getSelectedItem() != null) {
                BookItem returnBook = memBookTable.getSelectionModel().getSelectedItem();
                alertInfo.setContentText(CurrentMember.getMember().basicActions(returnBook.getId(), Date.valueOf(LocalDate.now()), "RETURN"));
                CurrentMember.deleteListOfMemBook(returnBook);
                CurrentMember.updateListOfLibBook(returnBook);
                alertInfo.showAndWait();
            }
        } else if (event.getSource() == memLostBookBut) {
            if (memBookTable.getSelectionModel().getSelectedItem() != null) {
                BookItem lostBook = memBookTable.getSelectionModel().getSelectedItem();
                Notification notification = CurrentMember.getMember().getNotificationBox().findNotification(lostBook.getId());
                CurrentMember.getMember().updateBook(lostBook.getId(), 13, "LOST");
                CurrentMember.deleteListOfMemBook(lostBook);
                CurrentMember.updateListOfLibBook(lostBook);
                alertInfo.setContentText("You have lost " + Math.min(50, CurrentMember.getMember().getPoint()) + " points for report after the due date!");
                CurrentLibrarian.getLibrarian().updatePoint(CurrentMember.getMember().getId(), Math.max(0, CurrentMember.getMember().getPoint() - 50));
                alertInfo.showAndWait();
            }
        }
    }
}

