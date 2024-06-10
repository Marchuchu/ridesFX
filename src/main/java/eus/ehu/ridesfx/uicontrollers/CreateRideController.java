package eus.ehu.ridesfx.uicontrollers;

import eus.ehu.ridesfx.businessLogic.BlFacade;
import eus.ehu.ridesfx.domain.Alerts;
import eus.ehu.ridesfx.domain.Traveler;
import eus.ehu.ridesfx.domain.User;
import eus.ehu.ridesfx.ui.MainGUI;
import eus.ehu.ridesfx.utils.StringUtils;
import eus.ehu.ridesfx.utils.Dates;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.util.Callback;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class CreateRideController implements Controller {

    private BlFacade businessLogic;
    @FXML
    private DatePicker datePicker;
    private MainGUI mainGUI;
    @FXML
    private Label lblErrorMessage;
    @FXML
    private Label lblErrorMinBet;
    @FXML
    private Button btnCreateRide;
    @FXML
    private TextField txtArrivalCity;
    @FXML
    private TextField txtDepartCity;
    @FXML
    private TextField txtNumberOfSeats;
    @FXML
    private TextField txtPrice;

    @FXML

    private Label arrivalCity;

    @FXML
    private Label departCity;

    @FXML
    private Label nrSeats;

    @FXML
    private Label price;

    @FXML
    private Label rideDate;

    private List<LocalDate> holidays = new ArrayList<>();

    public CreateRideController(BlFacade bl, MainGUI mGUI) {
        this.businessLogic = bl;
        this.mainGUI = mGUI;
    }

    public CreateRideController(BlFacade bl) {
        this.businessLogic = bl;
    }

    private void clearErrorLabels() {
        lblErrorMessage.setText("");
        lblErrorMessage.getStyleClass().clear();
    }

    void displayMessage(String message, String label) {
        lblErrorMessage.getStyleClass().clear();
        lblErrorMessage.getStyleClass().setAll("lbl", "lbl-" + label);
        lblErrorMessage.setText(message);
    }


    @FXML
    void createRideClick(ActionEvent e) {

        String from = txtDepartCity.getText();
        String to = txtArrivalCity.getText();
        LocalDate date = datePicker.getValue();

        if (date == null) {
            lblErrorMessage.setText(StringUtils.translate("CreateRideGUI.FillDate"));
            time(5, lblErrorMessage);

            lblErrorMessage.setStyle("-fx-text-fill: #d54242");
            return;
        }

        if (date.isBefore(LocalDate.now())) {
            lblErrorMessage.setText(StringUtils.translate("CreateRideGUI.DateMustBeLaterThanToday"));
            time(5, lblErrorMessage);

            lblErrorMessage.setStyle("-fx-text-fill: #d54242");
            return;
        }

        if (from.isEmpty()) {
            lblErrorMessage.setText(StringUtils.translate("CreateRideGUI.FillDepartureCity"));
            time(5, lblErrorMessage);

            lblErrorMessage.setStyle("-fx-text-fill: #d54242");
            return;
        }

        if (to.isEmpty()) {
            lblErrorMessage.setStyle("-fx-text-fill: #d54242");
            lblErrorMessage.setText(StringUtils.translate("CreateRideGUI.FillArrivalCity"));
            time(5, lblErrorMessage);

            return;
        }

        if (txtNumberOfSeats.getText() == null) {
            lblErrorMessage.setText(StringUtils.translate("CreateRideGUI.FillNumberOfSeats"));
            lblErrorMessage.setStyle("-fx-text-fill: #d54242");
            time(5, lblErrorMessage);

            return;
        }

        int numPlaces;
        try {
            numPlaces = Integer.parseInt(txtNumberOfSeats.getText());
        } catch (NumberFormatException e1) {
            lblErrorMessage.setText(StringUtils.translate("CreateRideGUI.NumberOfSeatsMustBeANumber"));
            lblErrorMessage.setStyle("-fx-text-fill: #d54242");
            time(5, lblErrorMessage);

            return;
        }

        if (txtPrice.getText() == null) {
            lblErrorMessage.setText(StringUtils.translate("CreateRideGUI.FillPrice"));
            lblErrorMessage.setStyle("-fx-text-fill: #d54242");
            time(5, lblErrorMessage);

            return;
        }

        float price;
        try {
            price = Float.parseFloat(txtPrice.getText());
        } catch (NumberFormatException e1) {
            lblErrorMessage.setText(StringUtils.translate("CreateRideGUI.PriceMustBeANumber"));
            lblErrorMessage.setStyle("-fx-text-fill: #d54242");
            time(5, lblErrorMessage);

            return;
        }

        if (numPlaces > 5) {
            lblErrorMessage.setText(StringUtils.translate("CreateRideGUI.NumberOfSeatsMustBeLessThan5"));
            lblErrorMessage.setStyle("-fx-text-fill: #d54242");
            time(5, lblErrorMessage);

            return;
        }

        User user = businessLogic.getCurrentUser();
        businessLogic.createRideClick(from, to, Dates.convertToDate(date), numPlaces, price, user.getEmail());
        lblErrorMessage.setText(StringUtils.translate("CreateRideGUI.RideCreated"));
        time(5, lblErrorMessage);


        List<String> deptCities = businessLogic.getDepartCities();
        if (!deptCities.contains(from)) {
            businessLogic.addCitie(from);
        }

        List<String> destCities = businessLogic.getDestinationCities(from);
        if (!destCities.contains(to)) {
            businessLogic.addCitie(to);
        }


    }

    @FXML
    void initialize() {
        btnCreateRide.setStyle("-fx-background-color: #f85774");
        lblErrorMessage.setText("");

        datePicker.setOnMouseClicked(e -> {
            DatePickerSkin skin = (DatePickerSkin) datePicker.getSkin();
            skin.getPopupContent().lookupAll(".button").forEach(node -> {
                node.setOnMouseClicked(event -> {
                    List<Node> labels = skin.getPopupContent().lookupAll(".label").stream().toList();
                    String month = ((Label) (labels.get(0))).getText();
                    String year = ((Label) (labels.get(1))).getText();
                    YearMonth ym = Dates.getYearMonth(month + " " + year);
                });
            });
        });

        datePicker.setDayCellFactory(new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(DatePicker param) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!empty && item != null) {
                            if (holidays.contains(item)) {
                                this.setStyle("-fx-background-color: #f85774");
                            }
                        }
                    }
                };
            }
        });

        datePicker.setOnAction(actionEvent -> {
        });
    }

    @Override
    public void setMainApp(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
    }

    @Override
    public void changeLanguage() {
        btnCreateRide.setText(StringUtils.translate("CreateRide"));
        departCity.setText(StringUtils.translate("DepartCity"));
        nrSeats.setText(StringUtils.translate("NumberOfSeats"));
        price.setText(StringUtils.translate("Price"));
        rideDate.setText(StringUtils.translate("RideDate"));
        arrivalCity.setText(StringUtils.translate("ArrivalCity"));
    }

    @Override
    public void showHide() {
    }

    @Override
    public void time(int s, Label mssg) {
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(s * 1000);
                Platform.runLater(() -> mssg.setVisible(false));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    @Override
    public void getAlerts(User t) {

    }

    @Override
    public void getAllAlerts() {

    }

    @Override
    public TableView<Alerts> getTblAlerts() {
        return null;
    }
}
