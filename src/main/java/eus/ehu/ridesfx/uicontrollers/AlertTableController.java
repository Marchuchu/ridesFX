package eus.ehu.ridesfx.uicontrollers;

import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import eus.ehu.ridesfx.businessLogic.BlFacade;
import eus.ehu.ridesfx.domain.Alert;
import eus.ehu.ridesfx.domain.Driver;
import eus.ehu.ridesfx.domain.Traveler;
import eus.ehu.ridesfx.domain.User;
import eus.ehu.ridesfx.ui.MainGUI;
import eus.ehu.ridesfx.utils.StringUtils;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class AlertTableController implements Controller {

    ObservableList<Alert> observableArray = FXCollections.observableArrayList();
    @FXML
    private TableColumn<Alert, Date> date;
    @FXML
    private TableColumn<Alert, String> from;
    @FXML
    private TableColumn<Alert, String> to;
    @FXML
    private TableColumn<Alert, String> user;
    @FXML
    private Button cancelAlertBttn;
    @FXML
    private AnchorPane mainWrapper;
    @FXML
    private Text setPriceTXT;
    @FXML
    private Label message;
    @FXML
    private Button takeRideBttn;
    @FXML
    private TableView<Alert> tblAlerts;
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

    @Override
    public void setMainApp(MainGUI mainGUI) {
        this.mGUI = mainGUI;
    }


    @FXML
    void initialize() {
        price.setText("0");
        tblAlerts.setItems(observableArray);

        message.setVisible(false);

        setUpAlertsSelection();
        showHide();
        changeLanguage();

        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        from.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFrom()));
        to.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTo()));
        user.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUser().getName()));
    }


    //Buttons methods

    @FXML
    void onClickCancelAlert(ActionEvent event) {
        Alert r = tblAlerts.getSelectionModel().getSelectedItem();

        takeRideBttn.setStyle("-fx-background-color: #f85774");
        cancelAlertBttn.setStyle("-fx-background-color: #f85774");

        if (r != null) {

            businessLogic.cancelAlert(r);
            observableArray.remove(r);

            showErrorMessage("AlertCanceledSuccessfully", message, "-fx-text-fill: #188a2e", 5);

        } else {

            showErrorMessage("YouMustSelectAnAlertToCancel", message, "-fx-text-fill: #f85774", 5);

        }

    }

    @FXML
    void onClickTakeRide(ActionEvent event) {

        Alert r = tblAlerts.getSelectionModel().getSelectedItem();

        if (r != null) {
            businessLogic.takeRide(r, 1, Integer.parseInt(price.getText()));

            cancelAlertBttn.setVisible(false);
            takeRideBttn.setStyle("-fx-background-color: #f85774");

            showErrorMessage("RideTakenSuccessfully", message, "-fx-text-fill: #188a2e", 5);

        } else {

            showErrorMessage("YouMustSelectARideToTake", message, "-fx-text-fill: #f85774", 5);

        }

    }


    //Auxiliar methods

    @Override
    public void changeLanguage() {
        takeRideBttn.setText(StringUtils.translate("TakeRide"));
        cancelAlertBttn.setText(StringUtils.translate("CancelAlert"));
        from.setText(StringUtils.translate("From"));
        to.setText(StringUtils.translate("To"));
        user.setText(StringUtils.translate("User"));
        date.setText(StringUtils.translate("Date"));
        price.setPromptText(StringUtils.translate("Price"));
    }

    public void showHide() {
        if (businessLogic.getCurrentUser() instanceof Traveler) {
            takeRideBttn.setVisible(false);
            cancelAlertBttn.setVisible(true);
            price.setVisible(false);
            setPriceTXT.setVisible(false);
            cancelAlertBttn.setStyle("-fx-background-color: #f85774");
        } else if (businessLogic.getCurrentUser() instanceof Driver) {
            cancelAlertBttn.setVisible(false);
            takeRideBttn.setVisible(true);
            price.setVisible(true);
            setPriceTXT.setVisible(true);
            takeRideBttn.setStyle("-fx-background-color: #f85774");
        }
    }

    @Override
    public void time(int s, Label msg) {
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(s * 1000);
                Platform.runLater(() -> msg.setVisible(false));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    private void setUpAlertsSelection() {
        tblAlerts.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                System.out.println("Selected alert: " + newSelection);
            }
        });
    }

    @Override
    public void getAlerts(User u) {
        List<Alert> alerts = businessLogic.getAlerts(u);
        observableArray.setAll(alerts);
    }

    @Override
    public void getAllAlerts() {
        List<Alert> alerts = businessLogic.getAllAlerts();
        observableArray.setAll(alerts);
    }

    public TableView<Alert> getTblAlerts() {
        return tblAlerts;
    }

    @Override
    public void showErrorMessage(String txt, Label label, String style, int t){

        label.setText(StringUtils.translate(txt));
        label.setStyle(style);
        label.setVisible(true);
        time(t, label);

    }


    //Unused methods

    @Override
    public void updateComboBoxes(String from) {

    }

    @Override
    public void clearData() {

    }

    @Override
    public void loadMessages() {

    }

    @Override
    public void loadMessages(User u) {

    }
}