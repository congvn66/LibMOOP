package com.example.proj.Controller;

import com.example.proj.Models.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class LoginController extends Application {
    @FXML
    private TextField Answer;

    @FXML
    private Button Login;

    @FXML
    private AnchorPane LoginTab;

    @FXML
    private Button answerButton;

    @FXML
    private Label answerCheck;

    @FXML
    private Button doneSetPass;

    @FXML
    private Hyperlink forgot_password;

    @FXML
    private Label idCheck;

    @FXML
    private Button nameForgot;

    @FXML
    private TextField nameInForgot;

    @FXML
    private TextField name_log;

    @FXML
    private PasswordField password_log;

    @FXML
    private AnchorPane questionTest;

    @FXML
    private AnchorPane resetPassword;

    @FXML
    private PasswordField setPass;

    @FXML
    private Label wrong;

    @FXML
    private AnchorPane nameForgotTab;

    @FXML
    private Button signUp;

    @FXML
    private Button loginInReg;

    @FXML
    private TextField nameReg;

    @FXML
    private PasswordField passWordReg;

    @FXML
    private Label nameTakenReg;

    @FXML
    private Button regInLogin;

    @FXML
    private AnchorPane RegisterTab;

    @FXML
    private Label emptyTextReg;

    @FXML
    private Button backToLoginInName;

    @FXML
    private Button backToLoginInQuestion;

    @FXML
    private Button backToLoginInReset;

    @FXML
    private Button exit;

    public void LoginTab(ActionEvent event) throws IOException {
        if (event.getSource() != null) {
            if (event.getSource() == Login) {
                Authentication authentication = new Authentication(name_log.getText(), password_log.getText());
                Librarian librarian = new Librarian();
                if (authentication.checkLoginasLibrarian() != null) {
                    CurrentLibrarian currentLibrarian = new CurrentLibrarian(authentication.checkLoginasLibrarian());
                    wrong.setVisible(false);
                    System.out.println("Librarian logged in");
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/proj/FXML/LibMain.fxml"));
                        Stage stage = new Stage();
                        Scene scene = new Scene(fxmlLoader.load());
                    stage.setTitle("Library Management System");
                    stage.setScene(scene);
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setResizable(false);
                    Login.getScene().getWindow().hide();
                    stage.show();
                    } catch (IOException i) {
                        i.printStackTrace();
                    }
                } else if (authentication.checkLoginMember(librarian) != null) {
                    CurrentLibrarian currentLibrarian = new CurrentLibrarian(librarian);
                    CurrentMember currentMember = new CurrentMember(authentication.checkLoginMember(librarian));
                    wrong.setVisible(false);
                    System.out.println("Login successfully");
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/proj/FXML/MemMain.fxml"));
                    Stage stage = new Stage();
                    Scene scene = new Scene(fxmlLoader.load());
                    stage.setTitle("Library Management System");
                    stage.setScene(scene);
                    stage.setResizable(false);
                    stage.initModality(Modality.APPLICATION_MODAL);
                    Login.getScene().getWindow().hide();
                    stage.show();
                } else {
                    wrong.setVisible(true);
                    System.out.println("Login failed");
                }
            } else if (event.getSource() == forgot_password) {
                nameForgotTab.toFront();
                wrong.setVisible(false);
            } else if (event.getSource() == regInLogin) {
                RegisterTab.toFront();
                wrong.setVisible(false);
            } else if (event.getSource() == exit) {
                Platform.exit();
            }
            name_log.setText("");
            password_log.setText("");
        }
    }

    public void nameInForgotTab(ActionEvent event) {
        String tmp = nameInForgot.getText();
        Authentication authentication = new Authentication();
        authentication.setId(tmp);
        if (event.getSource() == nameForgot) {
            if (authentication.checkMemberID()) {
                idCheck.setVisible(false);
                questionTest.toFront();
            } else {
                idCheck.setVisible(true);
                nameInForgot.setText("");
            }
        } else if (event.getSource() == backToLoginInName) {
            nameInForgot.setText("");
            idCheck.setVisible(false);
            LoginTab.toFront();
        }
    }
    public void questionTestTab(ActionEvent event) {
        try{
        String tmp = nameInForgot.getText();
        Authentication authentication = new Authentication();
        authentication.setId(tmp);
            if (event.getSource() == answerButton) {
                int num = Integer.parseInt(Answer.getText());
                if (authentication.checkMemberBookNum(num)) {
                    answerCheck.setVisible(false);
                    Answer.setText("");
                    resetPassword.toFront();
                } else {
                    answerCheck.setVisible(true);
                }
            } else if (event.getSource() == backToLoginInQuestion) {
                answerCheck.setVisible(false);
                Answer.setText("");
                LoginTab.toFront();
                nameInForgot.setText("");
            }
        } catch (NumberFormatException n){
            answerCheck.setVisible(true);
            Answer.setText("");
        }
    }
    public void resetPasswordTab(ActionEvent event) {
        String tmp = nameInForgot.getText();
        Authentication authentication = new Authentication();
        authentication.setId(tmp);
        if (event.getSource() == doneSetPass && setPass.getText() != null) {
            Librarian librarian = new Librarian();
            librarian.updatePassWord(tmp,setPass.getText());
            setPass.setText("");
            LoginTab.toFront();
            nameInForgot.setText("");
        } else if (event.getSource() == backToLoginInReset) {
            setPass.setText("");
            LoginTab.toFront();
            nameInForgot.setText("");
        }
    }

    public void registerTab(ActionEvent event) {
        String idSign = nameReg.getText();
        String passSign = passWordReg.getText();
        Authentication authentication = new Authentication(idSign,passSign);
        if(event.getSource() == signUp) {
            if (nameReg.getText().trim().isEmpty() || passWordReg.getText().trim().isEmpty()) {
                emptyTextReg.setVisible(true);
                nameTakenReg.setVisible(false);
                nameReg.setText("");
                passWordReg.setText("");
            }
            else {
            if (authentication.checkRegisterMember()) {
                Member member = new Member(idSign, AccountStatus.ACTIVE, passSign, 0, 100);
                Librarian librarian = new Librarian();
                librarian.addNewMemberDatabase(member);
                RegisterTab.toBack();
            } else {
                    nameReg.setText("");
                    passWordReg.setText("");
                    emptyTextReg.setVisible(false);
                    nameTakenReg.setVisible(true);
                }
            }
        } else if(event.getSource() == loginInReg) {
            RegisterTab.toBack();
            nameReg.setText("");
            passWordReg.setText("");
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/proj/FXML/Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Library Management System");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
