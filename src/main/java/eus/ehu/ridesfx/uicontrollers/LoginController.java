package eus.ehu.ridesfx.uicontrollers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import eus.ehu.ridesfx.businessLogic.BlFacade;
import eus.ehu.ridesfx.domain.Traveler;
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

        if (logInButt.getText() == null || password.getText() == null) {
            return;
        } else {

            try {

                if ((businessLogic.logIn(email.getText(), password.getText()))) {

                    hasLogin.setText("Login successful");
                    mGUI.showScene("Query Rides");
                    mGUI.mGUIC.setRolName(businessLogic.getCurrentUser().getName());

                    if (businessLogic.getCurrentUser().getClass().equals(Traveler.class)) {

                        mGUI.mGUIC.getCreateRidesBtn().setVisible(false);
                        mGUI.mGUIC.getQueryRidesBtn().setVisible(true);
                        mGUI.showScene("Query Rides");


                    } else {

                        mGUI.mGUIC.getQueryRidesBtn().setVisible(false);
                        mGUI.mGUIC.getCreateRidesBtn().setVisible(true);
                        mGUI.showScene("Create Ride");

                    }

                }


            } catch (UnknownUser unknownUser) {
                System.out.println("Unknown user");
                hasLogin.setText("Incorrect credentials");
            }

        }


    }


    @FXML
    void initialize() {

    }

    @Override
    public void setMainApp(MainGUI mainGUI) {
        this.mGUI = mainGUI;
    }


}
