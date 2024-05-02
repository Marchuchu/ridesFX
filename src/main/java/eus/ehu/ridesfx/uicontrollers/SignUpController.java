package eus.ehu.ridesfx.uicontrollers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import eus.ehu.ridesfx.businessLogic.BlFacade;
import eus.ehu.ridesfx.exceptions.UnknownUser;
import eus.ehu.ridesfx.ui.MainGUI;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class SignUpController implements Controller {

    String r;
    BlFacade businessLogic;
    MainGUI mainGUI;
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private TextField email;
    @FXML
    private Label hasLogin;
    @FXML
    private AnchorPane mainWrapper;
    @FXML
    private TextField name;
    @FXML
    private TextField password;
    @FXML
    private TextField repPas;
    @FXML
    private Button signUpButt;
    @FXML
    private ComboBox<String> role;

    private MainGUI.Window queryRideWin;

    @Override
    public void setMainApp(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
    }


    public class Window {
        private Controller controller;
        private Parent ui;
    }


    public SignUpController(BlFacade bl) {
        this.businessLogic = bl;
    }

    public SignUpController(BlFacade bl, MainGUI mainGUI) {
        this.businessLogic = bl;
        this.mainGUI = mainGUI;
    }

    @FXML
    void onClickSignUp(ActionEvent event) throws IOException {
        if (name.getText() == null || email.getText() == null || password.getText() == null || repPas.getText() == null || role.getValue() == null) {
            return;
        } else {
            if (password.getText().equals(repPas.getText())) {
                businessLogic.signUp(name.getText(), email.getText(), password.getText(), repPas.getText(), r);
                hasLogin.setText("Sign up successful");
                if (mainGUI != null) {
                    mainGUI.showScene("Query Rides");
                }
            } else {
                hasLogin.setText("Passwords do not match");
            }
        }
    }

//    void onClickSignUp(ActionEvent event) {
//
//        if(name.getText() == null || email.getText() == null || password.getText() == null || repPas.getText() == null || role.getValue() == null) {
//            return;
//        } else {
//            if (password.getText().equals(repPas.getText())) {
//                businessLogic.signUp(name.getText(), email.getText(), password.getText(), repPas.getText(), r);
//                hasLogin.setText("Sign up successful");
//                //mGUIC.showScene("Query Rides");
//            } else {
//                hasLogin.setText("Passwords do not match");
//            }
//        }
//
//    }

    @FXML
    void initialize() throws IOException {

        role.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            r = newValue;
        });

        role.setItems(FXCollections.observableArrayList("Driver", "Traveller"));

    }

}
