package com.example.proj;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class LoginController {
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
    private TextField password_log;

    @FXML
    private AnchorPane questionTest;

    @FXML
    private AnchorPane resetPassword;

    @FXML
    private TextField setPass;

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
    private TextField passWordReg;

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

    private String validLogin(String id, String passWord) {
        Authorization authorization = new Authorization(id, passWord);
        if(authorization.checkLoginMember()) {
            return "memLog";
        } else if(authorization.checkLoginasLibrarian()) {
            return "libLog";
        } else {return "invalid";}
    }

    public void LoginTab(ActionEvent event) {
        if (event.getSource() != null) {
            if (event.getSource() == Login) {
                if (validLogin(name_log.getText(), password_log.getText()) == "libLog") {
                    wrong.setVisible(false);
                    System.out.println("Librarian logged in");
                } else if (validLogin(name_log.getText(), password_log.getText()) == "memLog") {
                    wrong.setVisible(false);
                    System.out.println("Login successfully");
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
        Authorization authorization = new Authorization();
        authorization.setId(tmp);
        if (event.getSource() == nameForgot) {
            if (authorization.checkMemberID()) {
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
        Authorization authorization = new Authorization();
        authorization.setId(tmp);
            if (event.getSource() == answerButton) {
                int num = Integer.parseInt(Answer.getText());
                if (authorization.checkMemberBookNum(num)) {
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
        Authorization authorization = new Authorization();
        authorization.setId(tmp);
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
        Authorization authorization = new Authorization(idSign,passSign);
        if(event.getSource() == signUp) {
            if (authorization.checkRegisterMember()) {
                Member member = new Member(idSign, AccountStatus.ACTIVE, passSign, 0, 100);
                Librarian librarian = new Librarian();
                librarian.addNewMemberDatabase(member);
                RegisterTab.toBack();
            } else {
                if (nameReg.getText().trim().isEmpty() || passWordReg.getText().trim().isEmpty()) {
                    emptyTextReg.setVisible(true);
                    nameTakenReg.setVisible(false);
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
}