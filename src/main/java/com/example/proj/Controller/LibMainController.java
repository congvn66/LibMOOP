package com.example.proj.Controller;

import com.example.proj.Models.*;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
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

    @FXML
    private Spinner<Integer> addPointSpinner;
    @FXML
    private TableColumn<BookItem, String> bookAuthorColumn;

    @FXML
    private TableColumn<BookItem, String> bookAuthorDesColumn;

    @FXML
    private TableColumn<BookItem, String> bookIdColumn;
    @FXML
    private TableColumn<BookItem, String> bookLocationColumn;
    @FXML
    private TableColumn<BookItem, Integer> bookNumberColumn;

    @FXML
    private TableColumn<BookItem, Double> bookPriceColumn;

    @FXML
    private TableColumn<BookItem, Boolean> bookRefColumn;
    @FXML
    private TableColumn<BookItem, BookStatus> bookStatusColumn;

    @FXML
    private TableColumn<BookItem, String> bookSubjectColumn;

    @FXML
    private TableView<BookItem> bookTable;

    @FXML
    private TableColumn<BookItem, String> bookTitleColumn;

    @FXML
    private ChoiceBox<String> searchOptionChoiceBox;

    @FXML
    private ChoiceBox<Boolean> isRefOnlyChoiceBox;
    @FXML
    private ChoiceBox<BookStatus> bookStatusChoiceBox;
    @FXML
    private TextField searchTextField;
    @FXML
    private Label checkSearchLabel;

    @FXML
    private TextField priceTextField;
    @FXML
    private TextField numberTextField;
    @FXML
    private TextField locationTextField;
    @FXML
    private TextField updateIdTextField;
    @FXML
    private Button addBookBut;
    @FXML
    private Button updateBookBut;
    @FXML
    private Button cancelBookBut;
    @FXML
    private Button deleteBookBut;
    @FXML
    private Button searchBookBut;


    public void setMemberTable(ObservableList a) {
        accountIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        accountStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        booksNumColumn.setCellValueFactory(new PropertyValueFactory<>("totalBooksCheckedOut"));
        pointsColumn.setCellValueFactory(new PropertyValueFactory<>("point"));
        memberTable.setItems(a);
    }

    public void setBookTable(ObservableList a) {
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        totalBooks.setText(String.valueOf(CurrentLibrarian.getLibrarian().getCatalog().getTotalBooks() + "/5000"));
        booksAvailableNum.setText(String.valueOf(CurrentLibrarian.getLibrarian().getCatalog().getBookStatus().get(BookStatus.AVAILABLE).size()));
        bookReservedNum.setText(String.valueOf(CurrentLibrarian.getLibrarian().getCatalog().getBookStatus().get(BookStatus.RESERVED).size()));
        bookLoanedNum.setText(String.valueOf(CurrentLibrarian.getLibrarian().getCatalog().getBookStatus().get(BookStatus.LOANED).size()));
        bookLostNum.setText(String.valueOf(CurrentLibrarian.getLibrarian().getCatalog().getBookStatus().get(BookStatus.LOST).size()));
        totalMemNum.setText(String.valueOf(CurrentLibrarian.getLibrarian().getMemberMap().size()));
        progressBar.setProgress((double) CurrentLibrarian.getLibrarian().getCatalog().getBookId().size() / 5000);
        percentage.setText(String.valueOf((double) CurrentLibrarian.getLibrarian().getCatalog().getBookId().size() / 50 + "%"));
        welcomeText.setText("Hello " + CurrentLibrarian.getLibrarian().getId());
        setMemberTable(CurrentLibrarian.getMemberObservableList());
        setBookTable(CurrentLibrarian.getBookObservableList());
        ObservableList<AccountStatus> statusList = FXCollections.observableArrayList(AccountStatus.ACTIVE, AccountStatus.NONE, AccountStatus.BLACKLISTED);
        statusChoiceBox.getItems().addAll(statusList);
        ObservableList<BookStatus> bookStatusList = FXCollections.observableArrayList(BookStatus.AVAILABLE, BookStatus.LOANED, BookStatus.RESERVED, BookStatus.LOST);
        bookStatusChoiceBox.getItems().addAll(bookStatusList);
        ObservableList<Boolean> refList = FXCollections.observableArrayList(Boolean.TRUE, Boolean.FALSE);
        isRefOnlyChoiceBox.getItems().addAll(refList);
        ObservableList<String> searchOptionList = FXCollections.observableArrayList("ID", "Title", " Author", "Subject");
        searchOptionChoiceBox.getItems().addAll(searchOptionList);
        SpinnerValueFactory<Integer> integerSpinnerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0, 1);
        addPointSpinner.setValueFactory(integerSpinnerValueFactory);
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
                    CurrentLibrarian.setMemberObservableList(tmp);
                    setMemberTable(CurrentLibrarian.getMemberObservableList());
                    checkIdLabel.setVisible(false);
                } else {
                    enterIdTextField.setText("");
                    checkIdLabel.setText("Id doesn't exist");
                    checkIdLabel.setVisible(true);
                }
            }
        } else if (event.getSource() == updateBut) {
            if (statusChoiceBox.getValue() != null && addPointSpinner.getValue() instanceof Integer) {
                CurrentLibrarian.getLibrarian().changeMemberStatus(memberTable.getSelectionModel().getSelectedItem().getId(), statusChoiceBox.getValue());
                CurrentLibrarian.getLibrarian().updatePoint(memberTable.getSelectionModel().getSelectedItem().getId(), addPointSpinner.getValue());
                CurrentLibrarian.updateMemberObservableList(memberTable.getSelectionModel().getSelectedItem());
            }
        } else if (event.getSource() == cancelBut) {
            enterIdTextField.setText("");
            CurrentLibrarian.returnMemberObservableList();
            setMemberTable(CurrentLibrarian.getMemberObservableList());
            checkIdLabel.setVisible(false);
        } else if (event.getSource() == deleteBut) {
            if (memberTable.getSelectionModel().getSelectedItem() != null) {
                CurrentLibrarian.getLibrarian().deleteMemberAccount(memberTable.getSelectionModel().getSelectedItem().getId());
                CurrentLibrarian.deleteMemberObservableList(memberTable.getSelectionModel().getSelectedItem());
            }
        }
    }

    public void setBookManagementTab(ActionEvent event) {
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
                                 tmp = FXCollections.observableArrayList(CurrentLibrarian.getLibrarian().getCatalog().findBookById(searchString));
                                if (tmp.size() != 0) {
                                    CurrentLibrarian.setBookObservableList(FXCollections.observableArrayList(tmp));
                                    setBookTable(CurrentLibrarian.getBookObservableList());
                                    checkSearchLabel.setVisible(false);
                                } else {
                                    checkSearchLabel.setText("ID doesn't exist");
                                    checkSearchLabel.setVisible(true);
                                }
                                break;
                            case "Author":
                                tmp = FXCollections.observableArrayList(CurrentLibrarian.getLibrarian().getCatalog().findBooksByAuthor(searchString));
                                if (tmp.size() != 0) {
                                    CurrentLibrarian.setBookObservableList(tmp);
                                    setBookTable(CurrentLibrarian.getBookObservableList());
                                    checkSearchLabel.setVisible(false);
                                } else {
                                    checkSearchLabel.setText("Author doesn't exist");
                                    checkSearchLabel.setVisible(true);
                                }
                                break;
                            case "Subject":
                                tmp =FXCollections.observableArrayList(CurrentLibrarian.getLibrarian().getCatalog().findBooksBySubject(searchString));
                                if (tmp.size() != 0) {
                                    CurrentLibrarian.setBookObservableList(tmp);
                                    setBookTable(CurrentLibrarian.getBookObservableList());
                                    checkSearchLabel.setVisible(false);
                                } else {
                                    checkSearchLabel.setText("Subject doesn't exist");
                                    checkSearchLabel.setVisible(true);
                                }
                                break;
                            case "Title":
                                tmp =FXCollections.observableArrayList(CurrentLibrarian.getLibrarian().getCatalog().findBooksByTitle(searchString));
                                if (tmp.size() != 0) {
                                    CurrentLibrarian.setBookObservableList(tmp);
                                    setBookTable(CurrentLibrarian.getBookObservableList());
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
            }
                catch (Exception e) {
                e.printStackTrace();
                    checkSearchLabel.setText("Please select search option");
                    checkSearchLabel.setVisible(true);
                }
            }
        }
    }
