package eus.ehu.ridesfx.uicontrollers;

import java.util.Date;
import java.util.ResourceBundle;

import eus.ehu.ridesfx.businessLogic.BlFacade;
import eus.ehu.ridesfx.domain.*;
import eus.ehu.ridesfx.ui.MainGUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

public class AlertTableController implements Controller{

    @FXML
    private TableColumn<Alerts, Date> date;

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

    public AlertTableController(BlFacade bl, MainGUI mainGUIC) {

        this.businessLogic = bl;
        this.mGUI = mainGUIC;

    }

    public AlertTableController(BlFacade bl) {

        this.businessLogic = bl;

    }

    @FXML
    void onClickCancelAlert(ActionEvent event) {

        Ride r = tblAlerts.getSelectionModel().getSelectedItem();

        takeRideBttn.setStyle("-fx-background-color: #f85774");
        cancelAlertBttn.setStyle("-fx-background-color: #f85774");

        if(businessLogic.getCurrentUser().getClass().equals(Traveler.class) && r != null){

            businessLogic.cancelAlert(r);
            takeRideBttn.setVisible(false);
            cancelAlertBttn.setVisible(true);


        } else if (r == null){

            message.setText("You must select an alert to cancel");
            cancelAlertBttn.setVisible(true);
            takeRideBttn.setVisible(false);


        } else if(businessLogic.getCurrentUser().getClass().equals(Driver.class)){

            message.setText("Choose the ride you want to take");
            cancelAlertBttn.setVisible(false);
            takeRideBttn.setVisible(true);


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

    @Override
    public void changeLanguage(ResourceBundle resources) {

    }

    @FXML
    void initialize() {

        takeRideBttn.setStyle("-fx-background-color: #f85774");
        cancelAlertBttn.setStyle("-fx-background-color: #f85774");

        if(businessLogic.getCurrentUser().getClass().equals(Traveler.class)){

            takeRideBttn.setVisible(false);
            cancelAlertBttn.setVisible(true);
            price.setVisible(false);


        } else if(businessLogic.getCurrentUser().getClass().equals(Driver.class)) {

            cancelAlertBttn.setVisible(false);
            takeRideBttn.setVisible(true);

        }

    }

}
