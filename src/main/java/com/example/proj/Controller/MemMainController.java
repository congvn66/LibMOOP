package com.example.proj.Controller;

import com.example.proj.Models.BookItem;
import com.example.proj.Models.BookStatus;
import com.example.proj.Models.CurrentLibrarian;
import com.example.proj.Models.CurrentMember;
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
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
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
    private Button MemCancelBookBut;

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
    private Button cancelBookBut;

    @FXML
    private Label checkBookUpdateLabel;

    @FXML
    private Label checkBookUpdateLabel1;

    @FXML
    private Label checkSearchLabel;

    @FXML
    private Label checkSearchLabel1;

    @FXML
    private Button myBookManageBut;

    @FXML
    private TableColumn<BookItem, String> memBookAuthorColumn;

    @FXML
    private TableColumn<BookItem, String> memBookAuthorDescColumn;

    @FXML
    private ChoiceBox<String> memBookChoiceBox;

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
    private Button searchMemBookBut;

    @FXML
    private TextField searchMemBookTextField;

    @FXML
    private ChoiceBox<String> searchOptionChoiceBox;

    @FXML
    private TextField searchTextField;

    @FXML
    private Label totalBooks;

    @FXML
    private Label totalMemNum;

    @FXML
    private Button watchBookBut;

    @FXML
    private Label welcomeText;

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
        memBookSubjectColumn.setCellValueFactory(new PropertyValueFactory<>("subject"));
        memBookAuthorColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getAuthor().getName());
        });
        memBookAuthorDescColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getAuthor().getDescription());
        });
        memBookTable.setItems(a);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        totalBooks.setText(CurrentMember.getMember().getCatalog().getTotalBooks().get() + "/5000");
        progressBar.setProgress((double) CurrentMember.getMember().getCatalog().getBookId().size() / 5000);
        percentage.setText(String.valueOf((double) CurrentMember.getMember().getCatalog().getBookId().size() / 50 + "%"));
        CurrentMember.getMember().getCatalog().getTotalBooks().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> {
                totalBooks.setText(CurrentMember.getMember().getCatalog().getTotalBooks().get() + "/5000");
                progressBar.setProgress((double)CurrentMember.getMember().getCatalog().getTotalBooks().get() / 5000);
                percentage.setText((double) CurrentMember.getMember().getCatalog().getTotalBooks().get() / 50 + "%");
            });
        });
        booksAvailableNum.setText(String.valueOf(CurrentMember.getMember().getCatalog().getBookStatus().get(BookStatus.AVAILABLE).size()));
        CurrentMember.getMember().getCatalog().getBookStatus().get(BookStatus.AVAILABLE).addListener((ListChangeListener<BookItem>) change -> {
            Platform.runLater(() -> {
                booksAvailableNum.setText(String.valueOf(CurrentMember.getMember().getCatalog().getBookStatus().get(BookStatus.AVAILABLE).size()));
            });
        });
        bookReservedNum.setText(String.valueOf(CurrentMember.getMember().getCatalog().getBookStatus().get(BookStatus.RESERVED).size()));
        CurrentMember.getMember().getCatalog().getBookStatus().get(BookStatus.RESERVED).addListener((ListChangeListener<BookItem>) change -> {
            Platform.runLater(() -> {
                bookReservedNum.setText(String.valueOf(CurrentMember.getMember().getCatalog().getBookStatus().get(BookStatus.RESERVED).size()));
            });
        });
        bookLoanedNum.setText(String.valueOf(CurrentMember.getMember().getCatalog().getBookStatus().get(BookStatus.LOANED).size()));
        CurrentMember.getMember().getCatalog().getBookStatus().get(BookStatus.LOANED).addListener((ListChangeListener<BookItem>) change -> {
            Platform.runLater(() -> {
                bookLoanedNum.setText(String.valueOf(CurrentMember.getMember().getCatalog().getBookStatus().get(BookStatus.LOANED).size()));
            });
        });
        bookLostNum.setText(String.valueOf(CurrentMember.getMember().getCatalog().getBookStatus().get(BookStatus.LOST).size()));
        CurrentMember.getMember().getCatalog().getBookStatus().get(BookStatus.LOST).addListener((ListChangeListener<BookItem>) change -> {
            Platform.runLater(() -> {
                bookLostNum.setText(String.valueOf(CurrentMember.getMember().getCatalog().getBookStatus().get(BookStatus.LOST).size()));
            });
        });
        welcomeText.setText("Hello " + CurrentMember.getMember().getId());
        setLibBookTable(CurrentMember.getListOfLibBook());
        setMemBookTable(CurrentMember.getListOfMemBook());
        ObservableList<String> searchOptionList = FXCollections.observableArrayList("ID", "Title", "Author", "Subject");
        searchOptionChoiceBox.getItems().addAll(searchOptionList);
        memBookChoiceBox.getItems().addAll(searchOptionList);
        bookTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                WatchBookController.setBookItem(newValue);
            }
        });
    }

    public void setMainTab(ActionEvent event) {
        if (event.getSource() == bookManageBut) {
            BookManagementTab.toFront();
            backBut.toFront();
        } else if (event.getSource() == myBookManageBut) {
            MemBookTab.toFront();
            backBut.toFront();
        } else if (event.getSource() == backBut) {
            MainTab.toFront();
            ExitBut.toFront();
        } else if (event.getSource() == ExitBut) {
            Platform.exit();
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
                            default:
                                break;
                        }
                    }
                }
            } catch (Exception e) {
                checkSearchLabel.setText("Please select search option");
                checkSearchLabel.setVisible(true);
            }
        } else if (event.getSource() == cancelBookBut) {
            searchTextField.setText("");
            CurrentMember.returnListOfLibBook();
            setLibBookTable(CurrentMember.getListOfLibBook());
            checkBookUpdateLabel.setVisible(false);
            checkSearchLabel.setVisible(false);
            bookTable.getSelectionModel().select(null);
        } else if (event.getSource() == watchBookBut) {
            if (bookTable.getSelectionModel().getSelectedItem() != null) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/proj/FXML/WatchBook.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(fxmlLoader.load());
                stage.setTitle("Library Management System");
                stage.setScene(scene);
                stage.initStyle(StageStyle.UTILITY);
                stage.show();
                WatchBookController.setStage(stage);
            }
        }
    }

}
