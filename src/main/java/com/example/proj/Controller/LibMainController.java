package com.example.proj.Controller;

import com.example.proj.Models.*;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.Period;
import java.util.*;

public class LibMainController implements Initializable {
    @FXML
    private AnchorPane BookManagementTab;

    @FXML
    private Button ExitBut;

    @FXML
    private AnchorPane LibraryDiaryTab;


    @FXML
    private AnchorPane MainTab;

    @FXML
    private AnchorPane MemberManagementTab;

    @FXML
    private TableColumn<Member, Object> accountIdColumn;

    @FXML
    private TableColumn<Member, Object> accountStatusColumn;

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
    private TableColumn<Member, Object> booksNumColumn;

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
    private Button memberManageBut;

    @FXML
    private TableView<Member> memberTable;

    @FXML
    private Label percentage;

    @FXML
    private TableColumn<Member, Object> pointsColumn;

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
    private TableColumn<BookItem, Object> bookAuthorColumn;

    @FXML
    private TableColumn<BookItem, Object> bookAuthorDesColumn;

    @FXML
    private TableColumn<BookItem, Object> bookIdColumn;
    @FXML
    private TableColumn<BookItem, Object> bookLocationColumn;
    @FXML
    private TableColumn<BookItem, Object> bookNumberColumn;

    @FXML
    private TableColumn<BookItem, Object> bookPriceColumn;

    @FXML
    private TableColumn<BookItem, Object> bookRefColumn;
    @FXML
    private TableColumn<BookItem, Object> bookStatusColumn;

    @FXML
    private TableColumn<BookItem, Object> bookSubjectColumn;

    @FXML
    private TableView<BookItem> bookTable;

    @FXML
    private TableColumn<BookItem, Object> bookTitleColumn;

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
    @FXML
    private Label checkBookUpdateLabel;
    @FXML
    private DatePicker diaryStartDatePicker;
    @FXML
    private DatePicker diaryEndDatePicker;
    @FXML
    private TextField diaryIDText;
    @FXML
    private ChoiceBox<String> diaryChoiceBox;
    @FXML
    private Button diarySearchBut;
    @FXML
    private Label diaryCheckChoiceBox;
    @FXML
    private Label diaryCheckIdLabel;
    @FXML
    private Label diaryCheckStartDateLabel;
    @FXML
    private Label diaryCheckEndDateLabel;
    @FXML
    private BarChart<String, Number> diaryGraph;

    @FXML
    private Button addImageBut;

    @FXML
    private ImageView bookThumbnail;

    @FXML
    private Button searchApiBut;

    private File importedFile;

    private ImageImportService imageImportService;

    private Alert confirmationAlert;

    public void setMemberTable(ObservableList a) {
        setMemberColumnFormat(accountIdColumn);
        accountIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        setMemberColumnFormat(accountStatusColumn);
        accountStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        setMemberColumnFormat(booksNumColumn);
        booksNumColumn.setCellValueFactory(new PropertyValueFactory<>("totalBooksCheckedOut"));
        setMemberColumnFormat(pointsColumn);
        pointsColumn.setCellValueFactory(new PropertyValueFactory<>("point"));
        memberTable.setItems(a);
    }

    public void setBookTable(ObservableList a) {
        setBookColumnFormat(bookIdColumn);
        bookIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        setBookColumnFormat(bookTitleColumn);
        bookTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        setBookColumnFormat(bookSubjectColumn);
        bookSubjectColumn.setCellValueFactory(new PropertyValueFactory<>("subject"));
        setBookColumnFormat(bookRefColumn);
        bookRefColumn.setCellValueFactory(new PropertyValueFactory<>("isReferenceOnly"));
        setBookColumnFormat(bookStatusColumn);
        bookStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        setBookColumnFormat(bookPriceColumn);
        bookPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        setBookColumnFormat(bookAuthorColumn);
        bookAuthorColumn.setCellValueFactory(cellData -> {
            return new SimpleObjectProperty<>(cellData.getValue().getAuthor().getName());
        });
        setBookColumnFormat(bookAuthorDesColumn);
        bookAuthorDesColumn.setCellValueFactory(cellData -> {
            return new SimpleObjectProperty<>(cellData.getValue().getAuthor().getDescription());
        });
        setBookColumnFormat(bookNumberColumn);
        bookNumberColumn.setCellValueFactory(cellData -> {
            return new SimpleObjectProperty<>(cellData.getValue().getRack().getNumber());
        });
        setBookColumnFormat(bookLocationColumn);
        bookLocationColumn.setCellValueFactory(cellData -> {
            return new SimpleObjectProperty<>(cellData.getValue().getRack().getLocationIdentifier());
        });
        bookTable.setItems(a);
    }

    public void setBookColumnFormat(TableColumn<BookItem, Object> column) {
        column.setCellFactory(new Callback<TableColumn<BookItem, Object>, TableCell<BookItem, Object>>() {
            @Override
            public TableCell<BookItem, Object> call(TableColumn<BookItem, Object> bookItemStringTableColumn) {
                return new TableCell<BookItem, Object>() {
                    private final Text text = new Text();

                    {
                        bookItemStringTableColumn.widthProperty().addListener((observable, oldValue, newValue) -> {
                            Platform.runLater(() -> {
                                text.setWrappingWidth(bookItemStringTableColumn.getWidth());
                            });
                        });
                        text.setWrappingWidth(bookItemStringTableColumn.getWidth());
                        text.setTextAlignment(TextAlignment.CENTER);
                        setGraphic(text);
                    }

                    @Override
                    protected void updateItem(Object item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            text.setText(null);
                        } else {
                            if (item instanceof String) {
                                text.setText((String)item);
                            } else if (item instanceof Integer) {
                                text.setText(((Integer)item).toString());
                            } else if (item instanceof Boolean) {
                                text.setText(((Boolean)item).toString());
                            } else if (item instanceof Double) {
                                text.setText(((Double)item).toString());
                            } else if (item instanceof BookStatus) {
                                text.setText(((BookStatus)item).toString());
                            }
                        }
                    }
                };
            }
        });
    }

    public void setMemberColumnFormat(TableColumn<Member, Object> memberColumn) {
        memberColumn.setCellFactory(new Callback<TableColumn<Member, Object>, TableCell<Member, Object>>() {
            @Override
            public TableCell<Member, Object> call(TableColumn<Member, Object> memberStringTableColumn) {
                return new TableCell<Member, Object>() {
                    private final Text text = new Text();

                    {
                        memberStringTableColumn.widthProperty().addListener((observable, oldValue, newValue) -> {
                            Platform.runLater(() -> {
                                text.setWrappingWidth(memberStringTableColumn.getWidth());
                            });
                        });
                        text.setWrappingWidth(memberStringTableColumn.getWidth());
                        text.setTextAlignment(TextAlignment.CENTER);
                        setGraphic(text);
                    }

                    @Override
                    protected void updateItem(Object item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            text.setText(null);
                        } else {
                            if (item instanceof String) {
                                text.setText((String)item);
                            } else if (item instanceof Integer) {
                                text.setText(((Integer)item).toString());
                            } else if (item instanceof AccountStatus) {
                                text.setText(((AccountStatus)item).toString());
                            }
                        }
                    }
                };
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        totalBooks.setText(CurrentLibrarian.getLibrarian().getCatalog().getTotalBooks().get() + "/5000");
        progressBar.setProgress((double) CurrentLibrarian.getLibrarian().getCatalog().getBookId().size() / 5000);
        percentage.setText((double) CurrentLibrarian.getLibrarian().getCatalog().getBookId().size() / 50 + "%");
        CurrentLibrarian.getLibrarian().getCatalog().getTotalBooks().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> {
                totalBooks.setText(CurrentLibrarian.getLibrarian().getCatalog().getTotalBooks().get() + "/5000");
                progressBar.setProgress((double)CurrentLibrarian.getLibrarian().getCatalog().getTotalBooks().get() / 5000);
                percentage.setText((double) CurrentLibrarian.getLibrarian().getCatalog().getTotalBooks().get() / 50 + "%");
            });
        });
        booksAvailableNum.setText(String.valueOf(CurrentLibrarian.getLibrarian().getCatalog().getBookStatus().get(BookStatus.AVAILABLE.toString()).size()));
        CurrentLibrarian.getLibrarian().getCatalog().getBookStatus().get(BookStatus.AVAILABLE.toString()).addListener((ListChangeListener<BookItem>) change -> {
                    Platform.runLater(() -> {
                        booksAvailableNum.setText(String.valueOf(CurrentLibrarian.getLibrarian().getCatalog().getBookStatus().get(BookStatus.AVAILABLE.toString()).size()));
                    });
                });
        bookReservedNum.setText(String.valueOf(CurrentLibrarian.getLibrarian().getCatalog().getBookStatus().get(BookStatus.RESERVED.toString()).size()));
        CurrentLibrarian.getLibrarian().getCatalog().getBookStatus().get(BookStatus.RESERVED.toString()).addListener((ListChangeListener<BookItem>) change -> {
            Platform.runLater(() -> {
                bookReservedNum.setText(String.valueOf(CurrentLibrarian.getLibrarian().getCatalog().getBookStatus().get(BookStatus.RESERVED.toString()).size()));
            });
        });
        bookLoanedNum.setText(String.valueOf(CurrentLibrarian.getLibrarian().getCatalog().getBookStatus().get(BookStatus.LOANED.toString()).size()));
        CurrentLibrarian.getLibrarian().getCatalog().getBookStatus().get(BookStatus.LOANED.toString()).addListener((ListChangeListener<BookItem>) change -> {
            Platform.runLater(() -> {
                bookLoanedNum.setText(String.valueOf(CurrentLibrarian.getLibrarian().getCatalog().getBookStatus().get(BookStatus.LOANED.toString()).size()));
            });
        });
        bookLostNum.setText(String.valueOf(CurrentLibrarian.getLibrarian().getCatalog().getBookStatus().get(BookStatus.LOST.toString()).size()));
        CurrentLibrarian.getLibrarian().getCatalog().getBookStatus().get(BookStatus.LOST.toString()).addListener((ListChangeListener<BookItem>) change -> {
            Platform.runLater(() -> {
                bookLostNum.setText(String.valueOf(CurrentLibrarian.getLibrarian().getCatalog().getBookStatus().get(BookStatus.LOST.toString()).size()));
            });
        });
        totalMemNum.setText(String.valueOf(CurrentLibrarian.getLibrarian().getMemberMap().size()));
        CurrentLibrarian.getMemberObservableList().addListener((ListChangeListener<Member>) change -> {
            Platform.runLater(() -> {
                totalMemNum.setText(String.valueOf(CurrentLibrarian.getLibrarian().getMemberMap().size()));
            });
        });
        diaryChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            switch (newValue) {
                case "Total Logs", "Total member register" -> {
                    diaryIDText.setText("");
                    diaryIDText.setVisible(false);
                    diaryCheckIdLabel.setVisible(false);
                    diaryCheckChoiceBox.setVisible(false);
                }
                case "Logs by member" -> {
                    diaryIDText.setVisible(true);
                    diaryCheckChoiceBox.setVisible(false);
                }
                default -> {
                }
            }
        });
        welcomeText.setText("Hello " + CurrentLibrarian.getLibrarian().getId());
        setMemberTable(CurrentLibrarian.getMemberObservableList());
        memberTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_NEXT_COLUMN);
        setBookTable(CurrentLibrarian.getBookObservableList());
        bookTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_NEXT_COLUMN);
        ObservableList<AccountStatus> statusList = FXCollections.observableArrayList(AccountStatus.ACTIVE, AccountStatus.NONE, AccountStatus.BLACKLISTED);
        statusChoiceBox.getItems().addAll(statusList);
        ObservableList<BookStatus> bookStatusList = FXCollections.observableArrayList(BookStatus.AVAILABLE, BookStatus.LOANED, BookStatus.RESERVED, BookStatus.LOST);
        bookStatusChoiceBox.getItems().addAll(bookStatusList);
        ObservableList<Boolean> refList = FXCollections.observableArrayList(Boolean.TRUE, Boolean.FALSE);
        isRefOnlyChoiceBox.getItems().addAll(refList);
        ObservableList<String> searchOptionList = FXCollections.observableArrayList("ID", "Title", "Author", "Subject", "Status");
        searchOptionChoiceBox.getItems().addAll(searchOptionList);
        ObservableList<String> diaryChoice = FXCollections.observableArrayList("Total Logs", "Logs by member", "Total member register");
        diaryChoiceBox.getItems().addAll(diaryChoice);
        SpinnerValueFactory<Integer> integerSpinnerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0, 1);
        addPointSpinner.setValueFactory(integerSpinnerValueFactory);
        setConfirmationAlert();
        imageImportService = new ImageImportService();
        addImageBut.setDisable(true);
        memberTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                statusChoiceBox.setValue(newValue.getStatus());
                addPointSpinner.getValueFactory().setValue(newValue.getPoint());
                checkIdLabel.setVisible(false);
            } else {
                statusChoiceBox.setValue(null);
                addPointSpinner.getValueFactory().setValue(0);
            }
        });
        bookTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateIdTextField.setText(newValue.getId());
                bookStatusChoiceBox.setValue(newValue.getStatus());
                numberTextField.setText(String.valueOf(newValue.getRack().getNumber()));
                isRefOnlyChoiceBox.setValue(newValue.getIsReferenceOnly());
                priceTextField.setText(String.valueOf(newValue.getPrice()));
                locationTextField.setText(newValue.getRack().getLocationIdentifier());
                if (newValue.checkURL()) {
                    bookThumbnail.setImage(new Image(newValue.getImgName()));
                    addImageBut.setDisable(true);
                } else {
                    bookThumbnail.setImage(new Image(getClass().getResource(newValue.generateImagePathFromImageName(newValue.getImgName())).toExternalForm()));
                    addImageBut.setDisable(false);
                }
                checkBookUpdateLabel.setVisible(false);
            } else {
                updateIdTextField.setText("");
                bookStatusChoiceBox.setValue(null);
                numberTextField.setText("");
                isRefOnlyChoiceBox.setValue(null);
                priceTextField.setText("");
                locationTextField.setText("");
                bookThumbnail.setImage(null);
                addImageBut.setDisable(true);
            }
        });
    }

    public void setConfirmationAlert() {
        confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setContentText("There are already file with the same name in the book store \nDo you still want to replace it?");
        confirmationAlert.getButtonTypes().setAll(ButtonType.YES, ButtonType.CANCEL);
    }

    private void disableMainButton(Button[] list, Button x, boolean backToMain) {
        for (Button i : list) {
            i.setDisable(i.equals(x) && !backToMain);
        }
    }
    public void setMainTab(ActionEvent event) throws IOException {
        Button[] listOfMainBut = {memberManageBut, bookManageBut, diaryBut};
        if (event.getSource() == memberManageBut) {
            MemberManagementTab.toFront();
            backBut.toFront();
            disableMainButton(listOfMainBut, memberManageBut, false);
        } else if (event.getSource() == bookManageBut) {
            BookManagementTab.toFront();
            backBut.toFront();
            disableMainButton(listOfMainBut, bookManageBut, false);
        } else if (event.getSource() == diaryBut) {
            LibraryDiaryTab.toFront();
            backBut.toFront();
            disableMainButton(listOfMainBut, diaryBut, false);
        } else if (event.getSource() == backBut) {
            MainTab.toFront();
            ExitBut.toFront();
            disableMainButton(listOfMainBut, diaryBut, true);
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
            if (memberTable.getSelectionModel().getSelectedItem() != null && statusChoiceBox.getValue() != null && addPointSpinner.getValue() instanceof Integer) {
                CurrentLibrarian.getLibrarian().changeMemberStatus(memberTable.getSelectionModel().getSelectedItem().getId(), statusChoiceBox.getValue());
                CurrentLibrarian.getLibrarian().updatePoint(memberTable.getSelectionModel().getSelectedItem().getId(), addPointSpinner.getValue());
                CurrentLibrarian.updateMemberObservableList(memberTable.getSelectionModel().getSelectedItem());
            }
        } else if (event.getSource() == cancelBut) {
            enterIdTextField.setText("");
            CurrentLibrarian.returnMemberObservableList();
            setMemberTable(CurrentLibrarian.getMemberObservableList());
            checkIdLabel.setVisible(false);
            memberTable.getSelectionModel().select(null);
        } else if (event.getSource() == deleteBut) {
            if (memberTable.getSelectionModel().getSelectedItem() != null) {
                CurrentLibrarian.getLibrarian().deleteMemberAccount(memberTable.getSelectionModel().getSelectedItem().getId());
                CurrentLibrarian.deleteMemberObservableList(memberTable.getSelectionModel().getSelectedItem());
            }
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
                                BookItem bookItem = CurrentLibrarian.getLibrarian().getCatalog().findBookById(searchString);
                                if (bookItem != null) {
                                    tmp = FXCollections.singletonObservableList(bookItem);
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
                                tmp = FXCollections.observableArrayList(CurrentLibrarian.getLibrarian().getCatalog().findBooksBySubject(searchString));
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
                                tmp = FXCollections.observableArrayList(CurrentLibrarian.getLibrarian().getCatalog().findBooksByTitle(searchString));
                                if (tmp.size() != 0) {
                                    CurrentLibrarian.setBookObservableList(tmp);
                                    setBookTable(CurrentLibrarian.getBookObservableList());
                                    checkSearchLabel.setVisible(false);
                                } else {
                                    checkSearchLabel.setText("Title doesn't exist");
                                    checkSearchLabel.setVisible(true);
                                }
                                break;
                            case "Status":
                                tmp = FXCollections.observableArrayList(CurrentLibrarian.getLibrarian().getCatalog().findBooksByStatus(searchString.toUpperCase()));
                                if (tmp.size() != 0) {
                                    CurrentLibrarian.setBookObservableList(tmp);
                                    setBookTable(CurrentLibrarian.getBookObservableList());
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
            }
        } else if (event.getSource() == updateBookBut) {
            if (bookTable.getSelectionModel().getSelectedItem() != null) {
                boolean legitUpdate = true;
                ArrayList<String> updates = new ArrayList<>();
                try {
                    double priceUpdate = Double.parseDouble(priceTextField.getText());
                    int numberUpdate = Integer.parseInt(numberTextField.getText());
                    String[] location = locationTextField.getText().split(" ");
                    int numberOfRack = Integer.parseInt(location[1]);
                    if (!locationTextField.getText().contains("Rack") || priceUpdate < 0 || numberUpdate < 0 || numberOfRack < 0) {
                        throw new Exception();
                    }
                } catch (Exception n) {
                    legitUpdate = false;
                    checkBookUpdateLabel.setText("Wrong update format for price, number or location.");
                    checkBookUpdateLabel.setVisible(true);
                }
                try {
                    updates.add(String.valueOf(isRefOnlyChoiceBox.getValue()));
                    updates.add(priceTextField.getText());
                    updates.add(String.valueOf(bookStatusChoiceBox.getValue()));
                    updates.add(numberTextField.getText());
                    updates.add(locationTextField.getText());
                    updates.add(importedFile != null ? importedFile.getName() : bookTable.getSelectionModel().getSelectedItem().getImgName());
                } catch (NullPointerException n) {
                    legitUpdate = false;
                    checkBookUpdateLabel.setText("Please choose all categories.");
                    checkBookUpdateLabel.setVisible(true);
                }
                if (legitUpdate) {
                    checkBookUpdateLabel.setVisible(false);
                    int j = 0;
                    for (Integer i : new Integer[]{10, 11, 13, 16, 17, 18}) {
                        CurrentLibrarian.getLibrarian().updateBook(updateIdTextField.getText(), i, updates.get(j));
                        j++;
                    }
                    CurrentLibrarian.updateBookObservableList(bookTable.getSelectionModel().getSelectedItem());
                    if (importedFile != null) {
                        imageImportService.moveImageToFolder(importedFile);
                        importedFile = null;
                    }
                }
            }
        } else if (event.getSource() == cancelBookBut) {
            searchTextField.setText("");
            CurrentLibrarian.returnBookObservableList();
            setBookTable(CurrentLibrarian.getBookObservableList());
            checkBookUpdateLabel.setVisible(false);
            checkSearchLabel.setVisible(false);
            bookTable.getSelectionModel().select(null);
        } else if (event.getSource() == deleteBookBut) {
            if (bookTable.getSelectionModel().getSelectedItem() != null) {
                CurrentLibrarian.getLibrarian().removeBook(bookTable.getSelectionModel().getSelectedItem().getId());
                CurrentLibrarian.deleteBookObservableList(bookTable.getSelectionModel().getSelectedItem());
            }
        } else if (event.getSource() == searchApiBut) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/proj/FXML/ApiSearchBook.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Library Management System");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();
        } else if (event.getSource() == addBookBut) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/proj/FXML/ManuallyAddBook.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Library Management System");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } else if (event.getSource() == addImageBut) {
            importedFile = imageImportService.importImage((Stage)addImageBut.getScene().getWindow());
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
            return type.get() == ButtonType.YES;
        }
        return false;
    }

    public void setBookThumbnail(File fileImage) {
        bookThumbnail.setImage(new Image(fileImage.toURI().toString()));
    }

    public XYChart.Series<String, Number> getListOfTotalLogs(LinkedHashMap<Date, ObservableList<Log>> totalLog, Date startDate, Date endDate) {
        XYChart.Series<String, Number> listOfTotalLogs = new XYChart.Series<>();
        Period period = Period.between(startDate.toLocalDate(), endDate.toLocalDate());
        for (int i = 0; i <= period.getDays(); i++) {
            listOfTotalLogs.getData().add(new XYChart.Data<>(startDate.toLocalDate().plusDays(i).toString(), totalLog.get(java.sql.Date.valueOf(startDate.toLocalDate().plusDays(i))) != null ? totalLog.get(java.sql.Date.valueOf(startDate.toLocalDate().plusDays(i))).size() : 0));
        }
        return listOfTotalLogs;
    }

    public XYChart.Series<String, Number> getListOfTotalMemberRegister(LinkedHashMap<Date, Integer> memberRegister, Date startDate, Date endDate) {
        XYChart.Series<String, Number> listOfMemberRegister = new XYChart.Series<>();
        Period period = Period.between(startDate.toLocalDate(), endDate.toLocalDate());
        for (int i = 0; i <= period.getDays(); i++) {
            listOfMemberRegister.getData().add(new XYChart.Data<>(startDate.toLocalDate().plusDays(i).toString(),
                    memberRegister.get(java.sql.Date.valueOf(startDate.toLocalDate().plusDays(i))) != null ? memberRegister.get(java.sql.Date.valueOf(startDate.toLocalDate().plusDays(i))) : 0));
        }
        return listOfMemberRegister;
    }

    public XYChart.Series<String, Number> getListOfMemberLogs(HashMap<String, LinkedHashMap<Date, ObservableList<Log>>> memberLogs, String id, Date startDate, Date endDate) {
        XYChart.Series<String, Number> listOfMemberRegister = new XYChart.Series<>();
        Period period = Period.between(startDate.toLocalDate(), endDate.toLocalDate());
        for (int i = 0; i <= period.getDays(); i++) {
            listOfMemberRegister.getData().add(new XYChart.Data<>(startDate.toLocalDate().plusDays(i).toString(), memberLogs.get(id).get(java.sql.Date.valueOf(startDate.toLocalDate().plusDays(i))) != null ? memberLogs.get(id).get(java.sql.Date.valueOf(startDate.toLocalDate().plusDays(i))).size() : 0));
        }
        return listOfMemberRegister;
    }

    public void setLibraryDiaryTab(ActionEvent event) {
       if (event.getSource() == diarySearchBut) {
               Date startDate = null;
               Date endDate = null;
               if (diaryStartDatePicker.getValue() != null) {
                   startDate = Date.valueOf(diaryStartDatePicker.getValue());
                   diaryCheckStartDateLabel.setVisible(false);
               } else {
                   diaryCheckStartDateLabel.setText("Please select a date");
                   diaryCheckStartDateLabel.setVisible(true);
               }
               if (diaryEndDatePicker.getValue() != null) {
                   endDate = Date.valueOf(diaryEndDatePicker.getValue());
                   diaryCheckEndDateLabel.setVisible(false);
               } else {
                   diaryCheckEndDateLabel.setText("Please select a date");
                   diaryCheckEndDateLabel.setVisible(true);
               }
               if (startDate != null && endDate != null) {
                   if (startDate.after(endDate)) {
                       diaryCheckStartDateLabel.setText("Start date must be before end date");
                       diaryCheckEndDateLabel.setText("End date must be after start date");
                       diaryCheckStartDateLabel.setVisible(true);
                       diaryCheckEndDateLabel.setVisible(true);
                   } else {
                       long dateDiff = (endDate.getTime() - startDate.getTime()) / (24 * 60 * 60 * 1000);
                       if (dateDiff > 29) {
                       diaryCheckStartDateLabel.setText("Gap between 2 dates is equal to \nor over 30 days");
                       diaryCheckEndDateLabel.setText("Gap between 2 dates is equal to \nor over 30 days");
                       diaryCheckStartDateLabel.setVisible(true);
                       diaryCheckEndDateLabel.setVisible(true);
                       } else {
                           try {
                               if (diaryChoiceBox.getValue().equals("Total Logs")) {
                                   diaryCheckChoiceBox.setVisible(false);
                                   diaryGraph.setTitle("Total Logs By Day Graph");
                                   XYChart.Series tmp = getListOfTotalLogs(CurrentLibrarian.getLibrarian().getTotalLogs(), startDate, endDate);
                                   diaryGraph.setData(FXCollections.observableArrayList(tmp));
                               } else if (diaryChoiceBox.getValue().equals("Total member register")) {
                                   diaryCheckChoiceBox.setVisible(false);
                                   diaryGraph.setTitle("Member Register By Day");
                                   diaryGraph.setData(FXCollections.observableArrayList(getListOfTotalMemberRegister(CurrentLibrarian.getLibrarian().getMemberRegister(), startDate, endDate)));
                               } else if (diaryChoiceBox.getValue().equals("Logs by member")) {
                                   diaryCheckChoiceBox.setVisible(false);
                                   String id = diaryIDText.getText();
                                   if (id.equals("")) {
                                       diaryCheckIdLabel.setText("Please fill in an ID");
                                       diaryCheckIdLabel.setVisible(true);
                                   } else if (!CurrentLibrarian.getLibrarian().getMemberLogs().containsKey(id)) {
                                       diaryCheckIdLabel.setText("ID doesn't exist");
                                       diaryCheckIdLabel.setVisible(true);
                                   } else {
                                       diaryGraph.setTitle("Member Logs By Day");
                                       diaryGraph.setData(FXCollections.observableArrayList(getListOfMemberLogs(CurrentLibrarian.getLibrarian().getMemberLogs(), id, startDate, endDate)));
                                   }
                               }
                               PauseTransition pauseTransition = new PauseTransition(Duration.millis(2000));
                               pauseTransition.play();
                           } catch (NullPointerException n) {
                               diaryCheckChoiceBox.setVisible(true);
                           }
                       }
                   }
               }
       }
    }
    }
