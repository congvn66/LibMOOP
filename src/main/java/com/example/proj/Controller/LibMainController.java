package com.example.proj.Controller;

import com.example.proj.Models.*;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

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
    private TableColumn<Member, String> accountIdColumn;

    @FXML
    private TableColumn<Member, AccountStatus> accountStatusColumn;

    @FXML
    private Button backBut;

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
    private TableColumn<Member, Integer> booksNumColumn;

    @FXML
    private Button cancelBut;

    @FXML
    private Label checkIdLabel;

    @FXML
    private Button deleteBut;

    @FXML
    private Button diaryBut;

    @FXML
    private TextField enterIdTextField;

    @FXML
    private Button loggerManageBut;

    @FXML
    private Button memberManageBut;

    @FXML
    private TableView<Member> memberTable;

    @FXML
    private Label percentage;

    @FXML
    private TableColumn<Member, Integer> pointsColumn;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private ChoiceBox<AccountStatus> statusChoiceBox;

    @FXML
    private Label totalBooks;

    @FXML
    private Label totalMemNum;

    @FXML
    private Button updateBut;

    @FXML
    private Label welcomeText;

    @FXML
    private Button searchMemBut;

    public void setMemberTable(ObservableList a) {
        accountIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        accountStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        booksNumColumn.setCellValueFactory(new PropertyValueFactory<>("totalBooksCheckedOut"));
        pointsColumn.setCellValueFactory(new PropertyValueFactory<>("point"));
        memberTable.setItems(a);
    }

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
        welcomeText.setText("Hello " + CurrentLibrarian.getLibrarian().getId());
        setMemberTable(CurrentLibrarian.getMemberObservableList());
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

    public void setMemberManagementTab(ActionEvent event) {
        if (event.getSource() == searchMemBut) {
            String id = enterIdTextField.getText().trim();
            if (id.isEmpty()) {
                checkIdLabel.setText("Please enter ID");
                checkIdLabel.setVisible(true);
            } else {
                if (CurrentLibrarian.getLibrarian().getMemberMap().containsKey(id)) {
                    ObservableList tmp = FXCollections.observableArrayList();
                    tmp.add(CurrentLibrarian.getLibrarian().getMemberMap().get(id));
                    setMemberTable(tmp);
                    checkIdLabel.setVisible(false);
                } else {
                    enterIdTextField.setText("");
                    checkIdLabel.setText("Id doesn't exist");
                    checkIdLabel.setVisible(true);
                }
            }
        } else if (event.getSource() == memberTable) {
            if (memberTable.getSelectionModel().getSelectedItems().size() != 0) {
                ObservableList<AccountStatus> statusList = FXCollections.observableArrayList(AccountStatus.ACTIVE, AccountStatus.BLACKLISTED, AccountStatus.NONE);
                statusChoiceBox.setItems(statusList);
            }
        }
    }
}
