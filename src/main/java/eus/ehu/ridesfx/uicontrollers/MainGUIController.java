package eus.ehu.ridesfx.uicontrollers;

import eus.ehu.ridesfx.businessLogic.BlFacade;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import eus.ehu.ridesfx.ui.MainGUI;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;

public class MainGUIController implements Controller {

    @FXML
    private Label selectOptionLbl;

    @FXML
    private Label lblDriver;

    @FXML
    private ResourceBundle resources;

    @FXML
    private Label rolName;

    @FXML
    private Button signUpButton;

    @FXML
    private Text userNames;

      private BlFacade businessLogic;

    @FXML
    private BorderPane mainWrapper;

    private MainGUI mGUI;

    public MainGUIController() {
    }

    public MainGUIController(BlFacade blFacade) {
        businessLogic = blFacade;
    }

    public BlFacade getBusinessLogic() {
        return businessLogic;
    }

    void showName(String role) {

        if (role.equals("Driver")) {
            rolName.setText(businessLogic.getCurrentDriver().getName());
        } else if (role.equals("Traveler")) {
            rolName.setText(businessLogic.getCurrentTraveler().getName());
        } else {

            rolName.setText("Anonymous");

        }

    }

    public BorderPane getMainWrapper() {
        return mainWrapper;
    }

    @FXML
    void onClickSignUp(ActionEvent event) throws IOException {
        mGUI.showScene("Sign Up");
        showName(rolName.getText());
    }


    @FXML
    void onClickLogIn(ActionEvent event) throws IOException {
        mGUI.showScene("Log In");
        showName(rolName.getText());

    }

    @FXML
    void queryRides(ActionEvent event) throws IOException {

        mGUI.showScene("Query Rides");
    }

    @FXML
    void createRide(ActionEvent event) throws IOException {

        mGUI.showScene("Create Ride");
    }

    @FXML
    public void initialize() throws IOException {

    }


    @Override
    public void setMainApp(MainGUI mainGUI) {
        this.mGUI = mainGUI;
    }

}
