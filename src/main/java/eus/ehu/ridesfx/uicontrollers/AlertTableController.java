package eus.ehu.ridesfx.uicontrollers;

import eus.ehu.ridesfx.businessLogic.BlFacade;
import eus.ehu.ridesfx.domain.Ride;
import eus.ehu.ridesfx.ui.MainGUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.net.URL;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import static java.lang.Integer.parseInt;

public class AlertTableController implements Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableColumn<Ride, Integer> alertID;

    @FXML
    private TableColumn<Ride, Ride> alertRide;

    @FXML
    private DatePicker datePicker;

    @FXML
    private ComboBox<String> destinationComboBox;

    @FXML
    private ComboBox<String> originComboBox;

    @FXML
    private TableView<Ride> tblAlerts;

    private ObservableList<Ride> ride;

    @FXML
    void onClickCancelAlert(ActionEvent event) {

        ride.remove(tblAlerts.getSelectionModel().getSelectedItem());
        tblAlerts.setItems(ride);

    }

    @FXML
    void onClickCreateAlert(ActionEvent event) {

        alertID.setCellValueFactory(new PropertyValueFactory<>("id"));
        alertRide.setCellValueFactory(new PropertyValueFactory<>("ride"));

        int day = datePicker.getValue().getDayOfMonth();
        int month = datePicker.getValue().getMonthValue();
        int year = datePicker.getValue().getYear();

        Date date = new Date(year, month, day);

        ride = FXCollections.observableArrayList();
        ride.add(new Ride(parseInt(alertID.getText()), originComboBox.getValue(), destinationComboBox.getValue(), date));

        tblAlerts.setItems(ride);
        setUpAlertsSelection();

    }

    private void setUpAlertsSelection() {

        tblAlerts.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                System.out.println("Selected alert: " + newSelection);
            }
        });

    }


    private BlFacade bl;
    private MainGUI mainGUI;
    private Window aTC;


    @Override
    public void setMainApp(MainGUI mainGUI) {

    }

    private AlertTableController.Window load(String fxml) {

        try {

            FXMLLoader loader = new FXMLLoader(MainGUI.class.getResource(fxml), ResourceBundle.getBundle("Etiquetas", Locale.getDefault()));
            loader.setControllerFactory(controllerClass -> {
                try {
                    return controllerClass
                            .getConstructor(BlFacade.class)
                            .newInstance(bl);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            Parent ui = loader.load();
            Controller controller = loader.getController();

            AlertTableController.Window window = new AlertTableController.Window();
            window.controller = controller;
            window.ui = ui;
            return window;
        } catch (IOException e) {
            throw new RuntimeException(e);

        }

    }

    @FXML
    void initialize() {

        aTC = load("alertTable.fxml");

    }

    public class Window {
        private Controller controller;
        private Parent ui;
    }

}
