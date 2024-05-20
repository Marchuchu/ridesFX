package eus.ehu.ridesfx.uicontrollers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import eus.ehu.ridesfx.businessLogic.BlFacade;
import eus.ehu.ridesfx.domain.Traveler;
import eus.ehu.ridesfx.exceptions.UnknownUser;
import eus.ehu.ridesfx.ui.MainGUI;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

public class LoginController implements Controller {

    BlFacade businessLogic;
    MainGUI mGUI;

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
    void onClickLogIn(ActionEvent event) {

        if (logInButt.getText() == null || password.getText() == null) {

            mGUI.mGUIC.getSeeAlertsBttn().setVisible(false);

            return;
        } else {

            try {

                if ((businessLogic.logIn(email.getText(), password.getText()))) {

                    //hasLogin.setText("Login successful");

                    email.setText("");
                    password.setText("");

                    mGUI.showScene("Query Rides");
                    mGUI.mGUIC.setRolName(businessLogic.getCurrentUser().getName());

                    if (businessLogic.getCurrentUser().getClass().equals(Traveler.class)) {

                        mGUI.mGUIC.getCreateRidesBtn().setVisible(false);
                        mGUI.mGUIC.getQueryRidesBtn().setVisible(true);
                        mGUI.mGUIC.getSeeAlertsBttn().setVisible(true);
                        mGUI.showScene("Query Rides");


                    } else {

                        mGUI.mGUIC.getQueryRidesBtn().setVisible(false);
                        mGUI.mGUIC.getCreateRidesBtn().setVisible(true);
                        mGUI.mGUIC.getSeeAlertsBttn().setVisible(true);
                        mGUI.showScene("Create Ride");

                    }

                }


            } catch (UnknownUser unknownUser) {
                hasLogin.setText("Login failed");
                hasLogin.setStyle("-fx-text-fill: #d54242");
                PauseTransition pause = new PauseTransition(Duration.seconds(3));
                pause.setOnFinished(e -> {
                    hasLogin.setText("");
                });
                pause.play();

            }

        }


    }


    @FXML
    void initialize() {

        logInButt.setStyle("-fx-background-color: #f85774;");
        logInButt.setText("Log In");

    }

    @Override
    public void setMainApp(MainGUI mainGUI) {
        this.mGUI = mainGUI;
    }


}
