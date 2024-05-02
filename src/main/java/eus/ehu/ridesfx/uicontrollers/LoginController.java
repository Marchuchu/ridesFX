package eus.ehu.ridesfx.uicontrollers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import eus.ehu.ridesfx.businessLogic.BlFacade;
import eus.ehu.ridesfx.exceptions.UnknownUser;
import eus.ehu.ridesfx.ui.MainGUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class LoginController implements Controller {

    BlFacade businessLogic;
    MainGUI mGUI;
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private TextField email;
    @FXML
    private Label hasLogin;
    @FXML
    private Button logInButt;

    @FXML
    private TextField password;

    public LoginController(BlFacade bl, MainGUI mainGUIC) {
        this.businessLogic = bl;
        this.mGUI = mainGUIC;

    }

    public LoginController(BlFacade bl) {
        this.businessLogic = bl;

    }

    @FXML
    void onClickLogIn(ActionEvent event) throws UnknownUser, IOException {

        if (logInButt.getText() == null
                || password.getText() == null) {
            return;
        } else {

                if ((businessLogic.logIn(email.getText(), password.getText()))) {

                    hasLogin.setText("Login successful");

                    //mGUI = new MainGUI(businessLogic);
                    mGUI.showScene("Query Rides");

                } else {

                    hasLogin.setText("Incorrect credentials");

                }

        }

        try {
            businessLogic.logIn(logInButt.getText(), password.getText());

        } catch (UnknownUser unknownUser) {
            System.out.println("Unknown user");
        }

    }

//     private void showScene(String scene) {
//        switch (scene) {
//            case "Query Rides" -> mGUI.showScene("Query Rides");
//            case "Create Ride" -> mGUI.showScene("Create Ride");
//            case "Log In" -> mGUI.showScene("Log In");
//            case "Sign Up" -> mGUI.showScene("Sign Up");
//
//        }
//    }

    @FXML
    void initialize()  {

    }

    @Override
    public void setMainApp(MainGUI mainGUI) {
        this.mGUI = mainGUI;
    }


}
