package eus.ehu.ridesfx.uicontrollers;

import eus.ehu.ridesfx.businessLogic.BlFacade;
import eus.ehu.ridesfx.domain.Alert;
import eus.ehu.ridesfx.domain.User;
import eus.ehu.ridesfx.ui.MainGUI;
import eus.ehu.ridesfx.utils.StringUtils;
import eus.ehu.ridesfx.utils.Dates;
import javafx.application.Platform;
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

    @FXML
    void initialize() {
        btnCreateRide.setStyle("-fx-background-color: #f85774");
        lblErrorMessage.setText("");
        changeLanguage();

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

    private void clearErrorLabels() {
        lblErrorMessage.setText("");
        lblErrorMessage.getStyleClass().clear();
    }

    void displayMessage(String message, String label) {
        lblErrorMessage.getStyleClass().clear();
        lblErrorMessage.getStyleClass().setAll("lbl", "lbl-" + label);
        lblErrorMessage.setText(message);
    }

    //Buttons methods

    @FXML
    void createRideClick(ActionEvent e) {

        String from = txtDepartCity.getText();
        String to = txtArrivalCity.getText();
        LocalDate date = datePicker.getValue();

        if (date == null) {

            showErrorMessage("CreateRideGUI.FillDate", lblErrorMessage, "-fx-text-fill: #d54242", 5);

            return;
        }

        if (date.isBefore(LocalDate.now())) {

            showErrorMessage("CreateRideGUI.DateMustBeLaterThanToday", lblErrorMessage, "-fx-text-fill: #d54242", 5);

            return;
        }

        if (from.isEmpty()) {

            showErrorMessage("CreateRideGUI.FillDepartureCity", lblErrorMessage, "-fx-text-fill: #d54242", 5);

            return;
        }

        if (to.isEmpty()) {

            showErrorMessage("CreateRideGUI.FillArrivalCity", lblErrorMessage,"-fx-text-fill: #d54242", 5);

            return;
        }

        if (txtNumberOfSeats.getText() == null) {

            showErrorMessage("CreateRideGUI.FillNumberOfSeats", lblErrorMessage,"-fx-text-fill: #d54242", 5);


            return;
        }

        int numPlaces;
        try {
            numPlaces = Integer.parseInt(txtNumberOfSeats.getText());
        } catch (NumberFormatException e1) {

            showErrorMessage("CreateRideGUI.NumberOfSeatsMustBeANumber", lblErrorMessage,"-fx-text-fill: #d54242", 5);

            return;
        }

        if (txtPrice.getText() == null) {

            showErrorMessage("CreateRideGUI.FillPrice", lblErrorMessage, "-fx-text-fill: #d54242", 5);

            return;
        }

        float price;
        try {
            price = Float.parseFloat(txtPrice.getText());
        } catch (NumberFormatException e1) {
            showErrorMessage("CreateRideGUI.PriceMustBeANumber", lblErrorMessage,"-fx-text-fill: #d54242", 5);

            return;
        }

        if (numPlaces > 5) {

            showErrorMessage("CreateRideGUI.NumberOfSeatsMustBeLessThan5", lblErrorMessage,"-fx-text-fill: #d54242", 5);
            return;
        }

        User user = businessLogic.getCurrentUser();
        businessLogic.createRideClick(from, to, Dates.convertToDate(date), numPlaces, price, user.getEmail());

        showErrorMessage("CreateRideGUI.RideCreated", lblErrorMessage,"-fx-text-fill: #188a2e", 5);

        List<String> deptCities = businessLogic.getDepartCities();
        if (!deptCities.contains(from)) {
            businessLogic.addCitie(from);
        }

        List<String> destCities = businessLogic.getDestinationCities(from);
        if (!destCities.contains(to)) {
            businessLogic.addCitie(to);
        }

        mainGUI.queryRideWin.controller.updateComboBoxes(from);


    }


    //Auxiliar methods

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
    public void showErrorMessage(String txt, Label label, String style, int t){

        label.setText(StringUtils.translate(txt));
        label.setStyle(style);
        label.setVisible(true);
        time(t, label);

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

    //Unused methods

    @Override
    public void getAlerts(User t) {

    }

    @Override
    public void getAllAlerts() {

    }

    @Override
    public TableView<Alert> getTblAlerts() {
        return null;
    }

    @Override
    public void showHide() {

    }

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