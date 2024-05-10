package eus.ehu.ridesfx.uicontrollers;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import eus.ehu.ridesfx.businessLogic.BlFacade;
import eus.ehu.ridesfx.domain.Driver;
import eus.ehu.ridesfx.domain.Ride;
import eus.ehu.ridesfx.domain.Traveler;
import eus.ehu.ridesfx.domain.User;
import eus.ehu.ridesfx.ui.MainGUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

public class AlertTableController implements Controller{

    @FXML
    private TableColumn<Ride, Date> date;

    @FXML
    private TableColumn<Ride, String> from;

    @FXML
    private TableColumn<Ride, String> to;

    @FXML
    private TableColumn<Ride, User> user;

    @FXML
    private Button cancelAlertBttn;

    @FXML
    private AnchorPane mainWrapper;

    @FXML
    private Label message;

    @FXML
    private Button takeRideBttn;

    @FXML
    private TableView<Ride> tblAlerts;

    @FXML
    private TextField price;

    private MainGUI mGUI;
    private BlFacade businessLogic;

    @FXML
    void onClickCancelAlert(ActionEvent event) {

        Ride r = tblAlerts.getSelectionModel().getSelectedItem();

        if(businessLogic.getCurrentUser().getClass().equals(Traveler.class) && r != null){

            businessLogic.cancelAlert(r);

        } else {

            message.setText("You must select an alert to cancel");

        }

        takeRideBttn.setVisible(false);
        cancelAlertBttn.setStyle("-fx-background-color: #f85774");
        message.setText("Choose the alert you want to cancel");

    }

    @FXML
    void onClickTakeRide(ActionEvent event) {

        Ride r = tblAlerts.getSelectionModel().getSelectedItem();

        if(businessLogic.getCurrentUser().getClass().equals(Driver.class) && r != null){

            businessLogic.takeRide(r, 1, Integer.parseInt(price.getText()));

        } else {

            message.setText("You must select a ride to take");

        }

        cancelAlertBttn.setVisible(false);
        takeRideBttn.setStyle("-fx-background-color: #f85774");
        message.setText("Choose the ride you want to take");

    }

    private void setUpAlertsSelection() {

        tblAlerts.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                System.out.println("Selected alert: " + newSelection);
            }

        });

    }

    @Override
    public void setMainApp(MainGUI mainGUI) {

        this.mGUI = mainGUI;

    }

    @FXML
    void initialize() {

    }

}
