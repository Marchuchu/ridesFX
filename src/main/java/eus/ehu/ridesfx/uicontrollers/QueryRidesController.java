package eus.ehu.ridesfx.uicontrollers;

import eus.ehu.ridesfx.businessLogic.BlFacade;
import eus.ehu.ridesfx.domain.*;
import javafx.animation.PauseTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import eus.ehu.ridesfx.ui.MainGUI;
import eus.ehu.ridesfx.utils.Dates;
import javafx.util.Duration;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class QueryRidesController implements Controller {

    @FXML
    private Button btnClose;

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
    private AnchorPane mainAncor;

    private BlFacade bl;

    @FXML
    private TableView<Event> tblEvents;

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


    public ComboBox<String> getComboArrivalCity() {
        return comboArrivalCity;
    }

    public ComboBox<String> getComboDepartCity() {
        return comboDepartCity;
    }


    public void addArrivalCity(String c) {

        comboArrivalCity.getItems().add(c);

    }

    public void addDepartCity(String c) {

        comboDepartCity.getItems().add(c);

    }

    private void setEvents(int year, int month) {
        Date date = Dates.toDate(year, month);

        for (Date day : businessLogic.getEventsMonth(date)) {
            datesWithBooking.add(Dates.convertToLocalDateViaInstant(day));
        }
    }

    // we need to mark (highlight in pink) the events for the previous, current and next month
    // this method will be called when the user clicks on the << or >> buttons
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


    @FXML

    public void onClickCreateAlert(ActionEvent event) {


        Alerts ride = new Alerts(comboDepartCity.getValue(), comboArrivalCity.getValue(), Dates.convertToDate(datepicker.getValue()));
        User user = mainGUI.getBusinessLogic().getCurrentUser();

        if (user.getClass().equals(Traveler.class)) {

            if (ride.getFrom() != null && ride.getTo() != null && ride.getDate() != null) {

                businessLogic.createAlert(ride.getFrom(), ride.getTo(), ride.getDate(), user.getEmail());
                reservationMessage.setText(translate("AlertCreated"));
                reservationMessage.setStyle("-fx-text-fill: #188a2e");

//                Thread t = new Thread(() -> {
//                    try {
//                        Thread.sleep(3);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    reservationMessage.setText("");
//                });
//
//                t.start();


                PauseTransition pause = new PauseTransition(Duration.seconds(3));
                pause.setOnFinished(e -> {
                    reservationMessage.setText("");
                });
                pause.play();

            } else {

                reservationMessage.setText(translate("QueryRidesController.PleaseFillInAllFields"));
                reservationMessage.setStyle("-fx-text-fill: #d54242");
                PauseTransition pause = new PauseTransition(Duration.seconds(5));
                pause.setOnFinished(e -> {
                    reservationMessage.setText("");
                });
                pause.play();

            }


        } else {

            reservationMessage.setText(translate("QueryRidesController.PleaseLogInOrSignUp"));
            reservationMessage.setStyle("-fx-text-fill: #d54242");

        }
    }

    String translate(String txt) {

        return ResourceBundle.getBundle("Etiquetas").getString(txt);

    }

    @FXML
    void initialize() {

        bookButtn.setStyle("-fx-background-color: #f85774");
        createAlertBut.setStyle("-fx-background-color: #f85774");

        bookButtn.setText(translate("Book"));
        createAlertBut.setText(translate("CreateAlert"));
        DepartCity.setText(translate("DepartCity"));
        ArrivalCity.setText(translate("ArrivalCity"));
        EventDate.setText(translate("EventDate"));

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

                reservationMessage.setText(translate("QueryRidesController.PleaseSelectFutureDate"));
                reservationMessage.setStyle("-fx-text-fill: #d54242");
                PauseTransition pause = new PauseTransition(Duration.seconds(5));
                pause.setOnFinished(e -> {
                    reservationMessage.setText("");
                });
                pause.play();
            } else {

                tblRides.getItems().clear();
                // Vector<domain.Ride> events = businessLogic.getEvents(Dates.convertToDate(datepicker.getValue()));
                List<Ride> rides = businessLogic.getRides(comboDepartCity.getValue(), comboArrivalCity.getValue(), Dates.convertToDate(datepicker.getValue()));
                // List<Ride> rides = Arrays.asList(new Ride("Bilbao", "Donostia", Dates.convertToDate(datepicker.getValue()), 3, 3.5f, new Driver("pepe@pepe.com", "pepe")));
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

                    // print month value
                    System.out.println("Month:" + ym.getMonthValue());

                });
            });
        });

        // show just the driver's name in column1
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

    @FXML

    public void onClickBook(ActionEvent event) {

        Ride ride = tblRides.getSelectionModel().getSelectedItem();
        User user = mainGUI.getBusinessLogic().getCurrentUser();

        mainGUI.mGUIC.getSeeAlertsBttn().setVisible(true);

        if (user.getClass().equals(Traveler.class)) {

            if (ride != null) {


                reservationMessage.setText(translate("QueryRidesController.RideBooked"));
                reservationMessage.setStyle("-fx-text-fill: #188a2e");

                PauseTransition pause = new PauseTransition(Duration.seconds(3));
                pause.setOnFinished(e -> {
                    reservationMessage.setText("");
                });
                pause.play();

                ride.setNumPlaces(ride.getNumPlaces() - 1);

            } else {

                reservationMessage.setText(translate("QueryRidesController.PleaseSelectRideToBook"));
                reservationMessage.setStyle("-fx-text-fill: #d54242");
                PauseTransition pause = new PauseTransition(Duration.seconds(5));
                pause.setOnFinished(e -> {
                    reservationMessage.setText("");
                });
                pause.play();

            }

        } else {

            reservationMessage.setText(translate("QueryRidesController.PleaseLogInOrSignUp"));
            reservationMessage.setStyle("-fx-text-fill: #d54242");
            PauseTransition pause = new PauseTransition(Duration.seconds(7));
            pause.setOnFinished(e -> {
                reservationMessage.setText("");
            });
            pause.play();

            mainGUI.mGUIC.getSeeAlertsBttn().setVisible(false);

        }

    }


    @Override
    public void changeLanguage(ResourceBundle resources) {
        bookButtn.setText(resources.getString("Book"));
        createAlertBut.setText(resources.getString("CreateAlert"));
        DepartCity.setText(resources.getString("DepartCity"));
        ArrivalCity.setText(resources.getString("ArrivalCity"));
        EventDate.setText(resources.getString("EventDate"));
    }
}
