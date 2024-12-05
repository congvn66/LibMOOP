package com.example.proj.Controller;

import com.example.proj.Models.BookItem;
import com.example.proj.Models.GoogleBookApi;
import com.example.proj.Models.OpenLibraryApi;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ApiSearchController implements Initializable {
    @FXML
    private VBox apiBookListVbox;

    @FXML
    private Button searchBookBut;

    @FXML
    private ChoiceBox<String> searchOptionChoiceBox;

    @FXML
    private TextField searchTextField;

    @FXML
    Label checkSearchLabel;

    private List<BookItem> apiBookList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        searchOptionChoiceBox.getItems().addAll("ISBN", "Title", "Author");
        searchOptionChoiceBox.setValue("Title");
        apiBookList = new ArrayList<>();
    }

    public void setApiSearchTab(ActionEvent event) {
        if (event.getSource() == searchBookBut) {
            String searchText = searchTextField.getText();

            if (searchText.equals("")) {
                checkSearchLabel.setText("Please fill in");
                checkSearchLabel.setVisible(true);
                return;
            }

            Task<List<BookItem>> task = new Task<List<BookItem>>() {
                @Override
                protected List<BookItem> call() throws Exception {
                    return searchAPIByCategory(searchOptionChoiceBox.getValue(), searchText);
                }
            };

            task.setOnRunning(e -> {
                ProgressIndicator progressIndicator = new ProgressIndicator();
                progressIndicator.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
                apiBookListVbox.getChildren().clear();
                apiBookListVbox.getChildren().add(progressIndicator);
            });

            task.setOnSucceeded(e -> {
                apiBookList = task.getValue();
                if (apiBookList.size() != 0) {
                    generateBookFromApiTab(apiBookList);
                    checkSearchLabel.setVisible(false);
                } else {
                    checkSearchLabel.setText("There is no book like that");
                    checkSearchLabel.setVisible(true);
                    apiBookListVbox.getChildren().clear();
                }
            });

            task.setOnFailed(e -> {
                checkSearchLabel.setText("Error occurred while searching");
                checkSearchLabel.setVisible(true);
            });

            new Thread(task).start();
        }
    }


    public List<BookItem> searchAPIByCategory(String type, String searchText) {
        if (type.equals("ISBN")) {
            List<BookItem> bookList = new ArrayList<>();
            bookList.add(OpenLibraryApi.searchBookByIsbn(searchText));
            return bookList;
        } else if (type.equals("Title")) {
            return OpenLibraryApi.searchBooksByTitle(searchText);
        } else {
            return OpenLibraryApi.searchBooksByAuthor(searchText);
        }
    }

    public void generateBookFromApiTab(List<BookItem> bookItemList) {
        apiBookListVbox.getChildren().clear();
        try {
            for (BookItem i : bookItemList) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/proj/FXML/ApiBook.fxml"));
                AnchorPane anchorPane = loader.load();
                BookFromApiController bookFromApiController = loader.getController();
                bookFromApiController.setUpBook(i);
                apiBookListVbox.getChildren().add(anchorPane);
            }
        } catch (IOException i) {
            System.out.println("FXMl path does not exist");
            i.printStackTrace();
        }
    }
}
