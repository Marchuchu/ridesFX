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

    public TableView<Alert> getTblAlerts() {
        return tblAlerts;
    }

    public AlertTableController(BlFacade bl, MainGUI mainGUIC) {
        this.businessLogic = bl;
        this.mGUI = mainGUIC;
    }

    public AlertTableController(BlFacade bl) {
        this.businessLogic = bl;
    }

    @FXML
    void onClickCancelAlert(ActionEvent event) {
        Alert r = tblAlerts.getSelectionModel().getSelectedItem();

        takeRideBttn.setStyle("-fx-background-color: #f85774");
        cancelAlertBttn.setStyle("-fx-background-color: #f85774");

        if (businessLogic.getCurrentUser() instanceof Traveler && r != null) {
            try {
                businessLogic.cancelAlert(r);
                observableArray.remove(r);
                message.setText(translate("AlertCanceledSuccessfully"));
                time(5, message);
            } catch (IllegalArgumentException e) {
                message.setText(e.getMessage());
                time(5, message);
            }
        } else if (r == null) {
            message.setText(translate("YouMustSelectAnAlertToCancel"));
            time(5, message);
        } else if (businessLogic.getCurrentUser() instanceof Driver) {
            message.setText("Choose the ride you want to take");
            time(5, message);
        }

        takeRideBttn.setVisible(false);
        cancelAlertBttn.setStyle("-fx-background-color: #f85774");
        message.setText(translate("ChooseTheRideYouWantToTake"));
        time(5, message);
    }


//
//    @FXML
//    void onClickCancelAlert(ActionEvent event) {
//        Alerts r = tblAlerts.getSelectionModel().getSelectedItem();
//
//        takeRideBttn.setStyle("-fx-background-color: #f85774");
//        cancelAlertBttn.setStyle("-fx-background-color: #f85774");
//
//        if (businessLogic.getCurrentUser() instanceof Traveler && r != null) {
//            businessLogic.cancelAlert(r);
//            takeRideBttn.setVisible(false);
//            cancelAlertBttn.setVisible(true);
//        } else if (r == null) {
//            message.setText(translate("YouMustSelectAnAlertToCancel"));
//            time(5, message);
//            cancelAlertBttn.setVisible(true);
//            takeRideBttn.setVisible(false);
//        } else if (businessLogic.getCurrentUser() instanceof Driver) {
//            message.setText("Choose the ride you want to take");
//            time(5, message);
//            cancelAlertBttn.setVisible(false);
//            takeRideBttn.setVisible(true);
//        }
//
//        takeRideBttn.setVisible(false);
//        cancelAlertBttn.setStyle("-fx-background-color: #f85774");
//        message.setText(translate("ChooseTheRideYouWantToTake"));
//        time(5, message);
//    }

    @FXML
    void onClickTakeRide(ActionEvent event) {
        Alert r = tblAlerts.getSelectionModel().getSelectedItem();

        if (businessLogic.getCurrentUser() instanceof Driver && r != null) {
            businessLogic.takeRide(r, 1, Integer.parseInt(price.getText()));

            cancelAlertBttn.setVisible(false);
            takeRideBttn.setStyle("-fx-background-color: #f85774");
            message.setText(translate("RideTakenSuccessfully"));
            time(5, message);

        } else {
            message.setText(translate("YouMustSelectARideToTake"));
            time(5, message);
        }


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
    public void changeLanguage() {
        takeRideBttn.setText(StringUtils.translate("TakeRide"));
        cancelAlertBttn.setText(StringUtils.translate("CancelAlert"));
        from.setText(StringUtils.translate("From"));
        to.setText(StringUtils.translate("To"));
        user.setText(StringUtils.translate("User"));
        date.setText(StringUtils.translate("Date"));
        price.setPromptText(StringUtils.translate("Price"));
    }

    String translate(String txt) {
        return ResourceBundle.getBundle("Etiquetas").getString(txt);
    }

    @FXML
    void initialize() {
        price.setText("0");
        tblAlerts.setItems(observableArray);

        setUpAlertsSelection();
        showHide();
        changeLanguage();

        // Configurar las columnas de la tabla
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        from.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFrom()));
        to.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTo()));
        user.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUser().getName()));
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
    @Override
    public void updateComboBoxes(String from) {

    }
}
