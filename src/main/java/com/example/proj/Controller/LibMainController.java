package com.example.proj.Controller;

import com.example.proj.Models.BookStatus;
import com.example.proj.Models.Catalog;
import com.example.proj.Models.Librarian;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class LibMainController implements Initializable {
    @FXML
    private AnchorPane BookManagementTab;

    @FXML
    private Button ExitBut;

    @FXML
    private AnchorPane LibraryDiaryTab;

    @FXML
    private AnchorPane LoggerManagementTab;

    @FXML
    private AnchorPane MainTab;

    @FXML
    private AnchorPane MemberManagementTab;

    @FXML
    private Label bookLoanedNum;

    @FXML
    private Label bookLostNum;

    @FXML
    private Button bookManageBut;

    @FXML
    private Label bookReservedNum;

    @FXML
    private Label booksAvailableNum;

    @FXML
    private Button diaryBut;

    @FXML
    private Button loggerManageBut;

    @FXML
    private Button memberManageBut;

    @FXML
    private Label percentage;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label totalBooks;

    @FXML
    private Label totalMemNum;

    @FXML
    private Label welcomeText;

    @FXML
    private Button backBut;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        displayNumberOfBooks();
    }

    public void displayNumberOfBooks() {
        Catalog catalog = new Catalog();
        Librarian librarian = new Librarian();
        catalog.loadCatalogFromDatabase();
        totalBooks.setText(String.valueOf(catalog.getTotalBooks() + "/5000"));
        booksAvailableNum.setText(String.valueOf(catalog.getBookStatus().get(BookStatus.AVAILABLE).size()));
        bookReservedNum.setText(String.valueOf(catalog.getBookStatus().get(BookStatus.RESERVED).size()));
        bookLoanedNum.setText(String.valueOf(catalog.getBookStatus().get(BookStatus.LOANED).size()));
        bookLostNum.setText(String.valueOf(catalog.getBookStatus().get(BookStatus.LOST).size()));
        totalMemNum.setText(String.valueOf(librarian.getMemberMap().size()));
        progressBar.setProgress((double) catalog.getBookId().size() / 5000);
        percentage.setText(String.valueOf((double) catalog.getBookId().size() / 50 + "%"));
        welcomeText.setText("Hello " + CurrentAccount.getName());
    }

    public void setMainTab(ActionEvent event) {
                if (event.getSource() == memberManageBut) {
                    MemberManagementTab.toFront();
                    backBut.toFront();
                } else if (event.getSource() == bookManageBut) {
                    BookManagementTab.toFront();
                    backBut.toFront();
                } else if (event.getSource() == loggerManageBut) {
                    LoggerManagementTab.toFront();
                    backBut.toFront();
                } else if (event.getSource() == diaryBut) {
                    LibraryDiaryTab.toFront();
                    backBut.toFront();
                } else if (event.getSource() == backBut) {
                    MainTab.toFront();
                    ExitBut.toFront();
                } else if (event.getSource() == ExitBut) {
                    Platform.exit();
                }
        }
}

