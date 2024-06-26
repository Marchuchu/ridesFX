package eus.ehu.ridesfx.uicontrollers;

import eus.ehu.ridesfx.businessLogic.BlFacade;
import eus.ehu.ridesfx.domain.*;
import eus.ehu.ridesfx.domain.Alert;
import eus.ehu.ridesfx.utils.StringUtils;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.util.Callback;
import eus.ehu.ridesfx.ui.MainGUI;
import eus.ehu.ridesfx.utils.Dates;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class QueryRidesController implements Controller {

    @FXML
    private DatePicker datepicker;
    @FXML
    private Button bookButtn;
    @FXML
    private TableColumn<Ride, String> qc1;
    @FXML
    private TableColumn<Ride, Integer> qc2;
    @FXML
    private TableColumn<Ride, Float> qc3;
    @FXML
    private ComboBox<String> comboArrivalCity;
    @FXML
    private ComboBox<String> comboDepartCity;
    @FXML
    private Label ArrivalCity;
    @FXML
    private Label DepartCity;
    @FXML
    private Label EventDate;
    @FXML
    private TableView<Ride> tblRides;
    @FXML
    private Button createAlertBut;
    @FXML
    private Label rideDate;
    @FXML
    private Label reservationMessage;

    private MainGUI mainGUI;
    private List<LocalDate> datesWithBooking = new ArrayList<>();
    private BlFacade businessLogic;

    public QueryRidesController(BlFacade bl) {
        businessLogic = bl;
    }

    public QueryRidesController(BlFacade bl, MainGUI mGUI) {
        businessLogic = bl;
        this.mainGUI = mGUI;
    }

    @Override
    public void setMainApp(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
    }

    @FXML
    void initialize() {

        bookButtn.setStyle("-fx-background-color: #f85774");
        createAlertBut.setStyle("-fx-background-color: #f85774");

        changeLanguage();

        reservationMessage.setVisible(false);

        LocalDate lD = LocalDate.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = lD.format(formatter);

        rideDate.setText(formattedDate);

        // Update DatePicker cells when ComboBox value changes
        comboArrivalCity.valueProperty().addListener((obs, oldVal, newVal) -> updateDatePickerCellFactory(datepicker));

        ObservableList<String> departureCities = FXCollections.observableArrayList(new ArrayList<>());
        departureCities.setAll(businessLogic.getDepartCities());

        ObservableList<String> arrivalCities = FXCollections.observableArrayList(new ArrayList<>());
        arrivalCities.setAll(businessLogic.getArrivalCities(comboDepartCity.getValue()));

        comboArrivalCity.setEditable(true);
        comboDepartCity.setEditable(true);

        comboDepartCity.getItems().addAll(departureCities);
        comboArrivalCity.getItems().addAll(arrivalCities);

        comboDepartCity.setItems(departureCities);
        comboArrivalCity.setItems(arrivalCities);

        // when the user selects a departure city, update the arrival cities
        comboDepartCity.setOnAction(e -> {
            arrivalCities.clear();
            arrivalCities.setAll(businessLogic.getDestinationCities(comboDepartCity.getValue()));
        });

        // a date has been chosen, update the combobox of Rides
        datepicker.setOnAction(actionEvent -> {

            if (datepicker.getValue().compareTo(LocalDate.now()) < 0) {

                showErrorMessage("QueryRidesController.PleaseSelectFutureDate", reservationMessage, "-fx-text-fill: #d54242", 5);

            } else {

                tblRides.getItems().clear();
                List<Ride> rides = businessLogic.getRides(comboDepartCity.getValue(), comboArrivalCity.getValue(), Dates.convertToDate(datepicker.getValue()));
                for (Ride ride : rides) {
                    tblRides.getItems().add(ride);
                }

            }
        });

        datepicker.setOnMouseClicked(e -> {
            // get a reference to datepicker inner content
            // attach a listener to the  << and >> buttons
            // mark events for the (prev, current, next) month and year shown
            DatePickerSkin skin = (DatePickerSkin) datepicker.getSkin();
            skin.getPopupContent().lookupAll(".button").forEach(node -> {
                node.setOnMouseClicked(event -> {

                    List<Node> labels = skin.getPopupContent().lookupAll(".label").stream().toList();

                    String month = ((Label) (labels.get(0))).getText();
                    String year = ((Label) (labels.get(1))).getText();
                    YearMonth ym = Dates.getYearMonth(month + " " + year);

                    System.out.println("Month:" + ym.getMonthValue());

                });
            });
        });

        qc1.setCellValueFactory(new Callback<>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Ride, String> data) {
                Driver driver = data.getValue().getDriver();
                return new SimpleStringProperty(driver != null ? driver.getName() : "<no name>");
            }
        });

        qc2.setCellValueFactory(new PropertyValueFactory<>("numPlaces"));
        qc3.setCellValueFactory(new PropertyValueFactory<>("price"));

    }

    //Buttons methods

    @FXML
    public void onClickCreateAlert(ActionEvent event) {
        String from = comboDepartCity.getValue();
        String to = comboArrivalCity.getValue();
        LocalDate localDate = datepicker.getValue();
        Date date = Dates.convertToDate(localDate);
        User user = mainGUI.getBusinessLogic().getCurrentUser();

        if (user instanceof Traveler) {
            if (from != null && to != null && date != null) {

                businessLogic.createAlert(from, to, date, user.getEmail());
                addAlertToTable(new Alert(from, to, date, user));

                showErrorMessage("QueryRidesController.AlertCreated", reservationMessage, "-fx-text-fill: #188a2e", 5);

            } else {

                showErrorMessage("QueryRidesController.PleaseFillInAllFields", reservationMessage, "-fx-text-fill: #d54242", 5);
            }

        } else {

            showErrorMessage("QueryRidesController.PleaseLogInOrSignUp", reservationMessage, "-fx-text-fill: #d54242", 5);

        }
    }

    @FXML

    public void onClickBook(ActionEvent event) {

        Ride ride = tblRides.getSelectionModel().getSelectedItem();
        User user = mainGUI.getBusinessLogic().getCurrentUser();

        mainGUI.mGUIC.getSeeAlertsBttn().setVisible(true);

        if (user instanceof Traveler) {

            if (ride != null) {

                showErrorMessage("QueryRidesController.RideBooked", reservationMessage, "-fx-text-fill: #188a2e", 5);

                if (ride.getNumPlaces() == 0) {

                    showErrorMessage("QueryRidesController.NoPlacesLeft", reservationMessage, "-fx-text-fill: #d54242", 5);

                } else {

                    ride.setNumPlaces(ride.getNumPlaces() - 1);

                }

            } else {

                showErrorMessage("QueryRidesController.PleaseSelectRideToBook", reservationMessage, "-fx-text-fill: #d54242", 5);

            }

        } else {

            showErrorMessage("QueryRidesController.PleaseLogInOrSignUp", reservationMessage, "-fx-text-fill: #d54242",5);
            mainGUI.mGUIC.getSeeAlertsBttn().setVisible(false);

        }

    }

    //Auxiliar methods

    private void addAlertToTable(Alert alert) {

        ObservableList<Alert> alerts = mainGUI.alertWin.controller.getTblAlerts().getItems();
        alerts.add(alert);
        mainGUI.alertWin.controller.getTblAlerts().setItems(alerts);

    }

    public void clearData() {

        comboDepartCity.setValue(null);
        comboArrivalCity.setValue(null);
        datepicker.getEditor().clear();

    }

    public ComboBox<String> getComboArrivalCity() {
        return comboArrivalCity;
    }

    public ComboBox<String> getComboDepartCity() {
        return comboDepartCity;
    }

    public void updateComboBoxes(String originCity) {
        List<String> departCities = businessLogic.getDepartCities();
        List<String> arrivalCities = businessLogic.getArrivalCities(originCity);

        ObservableList<String> departCitiesObservable = FXCollections.observableArrayList(departCities);
        comboDepartCity.setItems(departCitiesObservable);

        ObservableList<String> arrivalCitiesObservable = FXCollections.observableArrayList(arrivalCities);
        comboArrivalCity.setItems(arrivalCitiesObservable);
    }

    private void setEvents(int year, int month) {
        Date date = Dates.toDate(year, month);

        for (Date day : businessLogic.getEventsMonth(date)) {
            datesWithBooking.add(Dates.convertToLocalDateViaInstant(day));
        }
    }

    private void setEventsPrePost(int year, int month) {
        LocalDate date = LocalDate.of(year, month, 1);
        setEvents(date.getYear(), date.getMonth().getValue());
        setEvents(date.plusMonths(1).getYear(), date.plusMonths(1).getMonth().getValue());
        setEvents(date.plusMonths(-1).getYear(), date.plusMonths(-1).getMonth().getValue());
    }


    private void updateDatePickerCellFactory(DatePicker datePicker) {

        List<Date> dates = businessLogic.getDatesWithRides(comboDepartCity.getValue(), comboArrivalCity.getValue());

        // extract datesWithBooking from rides
        datesWithBooking.clear();
        for (Date day : dates) {
            datesWithBooking.add(Dates.convertToLocalDateViaInstant(day));
        }

        datePicker.setDayCellFactory(new Callback<>() {
            @Override
            public DateCell call(DatePicker param) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        if (!empty && item != null) {
                            if (datesWithBooking.contains(item)) {
                                this.setStyle("-fx-background-color: pink");
                            } else {
                                this.setStyle("-fx-background-color: rgb(235, 235, 235)");
                            }
                        }
                    }
                };
            }
        });
    }

    @Override
    public void changeLanguage() {

        bookButtn.setText(StringUtils.translate("Book"));
        createAlertBut.setText(StringUtils.translate("CreateAlert"));
        DepartCity.setText(StringUtils.translate("DepartCity"));
        ArrivalCity.setText(StringUtils.translate("ArrivalCity"));
        EventDate.setText(StringUtils.translate("EventDate"));

        qc1.setText(StringUtils.translate("Driver"));
        qc2.setText(StringUtils.translate("Seats"));
        qc3.setText(StringUtils.translate("Price"));

    }


    @Override
    public void time(int s, Label msg) {

        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(s * 1000L);
                Platform.runLater(() -> msg.setVisible(false));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        thread.start();

    }

    @Override
    public void showErrorMessage(String txt, Label label, String style, int t) {

        label.setText(StringUtils.translate(txt));
        label.setStyle(style);
        label.setVisible(true);
        time(t, label);

    }

    //Unused methods

    @Override
    public void getAlerts(User t) {

    }

    @Override
    public void getAllAlerts() {

    }

    @Override
    public void showHide() {

    }

    @Override
    public TableView<Alert> getTblAlerts() {
        return null;
    }

    @Override
    public void loadMessages() {

    }

    @Override
    public void loadMessages(User u) {

    }
}
