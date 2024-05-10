package eus.ehu.ridesfx.uicontrollers;

import eus.ehu.ridesfx.businessLogic.BlFacade;
import eus.ehu.ridesfx.domain.User;
import eus.ehu.ridesfx.ui.MainGUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.util.Callback;
import eus.ehu.ridesfx.utils.Dates;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CreateRideController implements Controller {

    public CreateRideController(BlFacade bl, MainGUI mGUI) {
        this.businessLogic = bl;
        this.mainGUI = mGUI;
    }

    public CreateRideController(BlFacade bl) {
        this.businessLogic = bl;
    }

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


    /**@FXML
    void closeClick(ActionEvent event) {
    clearErrorLabels();
    mainGUI.showMain();
    }*/

    private void clearErrorLabels() {
        lblErrorMessage.setText("");
        lblErrorMinBet.setText("");
        lblErrorMinBet.getStyleClass().clear();
        lblErrorMessage.getStyleClass().clear();
    }


    private String field_Errors() {

        try {
            if ((txtDepartCity.getText().length() == 0) || (txtArrivalCity.getText().length() == 0) || (txtNumberOfSeats.getText().length() == 0) || (txtPrice.getText().length() == 0))
                return ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.ErrorQuery");
            else {

                // trigger an exception if the introduced string is not a number
                int inputSeats = Integer.parseInt(txtNumberOfSeats.getText());

                if (inputSeats <= 0) {
                    return ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.SeatsMustBeGreaterThan0");
                } else {
                    float price = Float.parseFloat(txtPrice.getText());
                    if (price <= 0)
                        return ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.PriceMustBeGreaterThan0");

                    else
                        return null;

                }
            }
        } catch (NumberFormatException e1) {

            return ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.ErrorNumber");
        } catch (Exception e1) {

            e1.printStackTrace();
            return null;

        }
    }

    void displayMessage(String message, String label){
        lblErrorMessage.getStyleClass().clear();
        lblErrorMessage.getStyleClass().setAll("lbl", "lbl-"+label);
        lblErrorMessage.setText(message);
    }

    boolean addC(String c){

        String from = txtDepartCity.getText();
        String to = txtArrivalCity.getText();
        boolean a, b = false;
        a = false;

        List<String> deptCities = businessLogic.getDepartCities();
        List<String> destCities = businessLogic.getDestinationCities(from);

        if(!deptCities.contains(from) ){
            businessLogic.addCitie(from);
            a = true;
        }

        if(!destCities.contains(to)){
            businessLogic.addCitie(to);
            b = true;
        }

        return (a && b);

    }

    @FXML
    void createRideClick(ActionEvent e) {

        clearErrorLabels();

        String from = txtDepartCity.getText();
        String to = txtArrivalCity.getText();
        LocalDate date = datePicker.getValue();

        mainGUI.mGUIC.getSeeAlertsBttn().setVisible(true);

        if(from != null && to != null && date != null){

            try{
                int numPlaces = Integer.parseInt(txtNumberOfSeats.getText());
            } catch (NumberFormatException e1){
                lblErrorMessage.setText("The number of seats must be a number");
                lblErrorMessage.setStyle("-fx-text-fill: #d54242");
            }

            int numPlaces = Integer.parseInt(txtNumberOfSeats.getText());

            try{
                float price = Float.parseFloat(txtPrice.getText());
            } catch (NumberFormatException e1){
                lblErrorMessage.setText("The price must be a number");
                lblErrorMessage.setStyle("-fx-text-fill: #d54242");
            }

            float price = Float.parseFloat(txtPrice.getText());

            if(numPlaces >= 4){

                lblErrorMessage.setText("The number of seats must be less than 4");
                lblErrorMessage.setStyle("-fx-text-fill: #d54242");


            } else {

                User user = businessLogic.getCurrentUser();

                businessLogic.createRideClick(from, to, Dates.convertToDate(date), numPlaces, price, user.getEmail());
                displayMessage(ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.RideCreated"), "success");

                List<String> deptCities = businessLogic.getDepartCities();
                List<String> destCities = businessLogic.getDestinationCities(from);

            }

        } else {

            lblErrorMinBet.setText("Please fill all the fields");
            lblErrorMessage.setStyle("-fx-text-fill: #d54242");
            lblErrorMinBet.getStyleClass().setAll("lbl", "lbl-danger");

        }




//
//        clearErrorLabels();
//
//        //  Event event = comboEvents.getSelectionModel().getSelectedItem();
//        String errors = field_Errors();
//
//        if (errors != null) {
//            // businessLogic.createQuestion(event, inputQuestion, inputPrice);
//            displayMessage(errors, "danger");
//
//        } else {
//            try {
//
//                int inputSeats = Integer.parseInt(txtNumberOfSeats.getText());
//                float price = Float.parseFloat(txtPrice.getText());
//                User user = businessLogic.getCurrentUser();
//                Ride r = businessLogic.createRide(txtDepartCity.getText(), txtArrivalCity.getText(), Dates.convertToDate(datePicker.getValue()), inputSeats, price, user.getEmail());
//                displayMessage(ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.RideCreated"), "success");
//
//
//            } catch (RideMustBeLaterThanTodayException e1) {
//                displayMessage(e1.getMessage(), "danger");
//            } catch (RideAlreadyExistException e1) {
//                displayMessage(e1.getMessage(), "danger");
//            }
//        }

/*
    if (lblErrorMinBet.getText().length() > 0 && showErrors) {
      lblErrorMinBet.getStyleClass().setAll("lbl", "lbl-danger");
    }
    if (lblErrorQuestion.getText().length() > 0 && showErrors) {
      lblErrorQuestion.getStyleClass().setAll("lbl", "lbl-danger");
    }
 */
    }

    private List<LocalDate> holidays = new ArrayList<>();

  /*private void setEventsPrePost(int year, int month) {
    LocalDate date = LocalDate.of(year, month, 1);
    setEvents(date.getYear(), date.getMonth().getValue());
    setEvents(date.plusMonths(1).getYear(), date.plusMonths(1).getMonth().getValue());
    setEvents(date.plusMonths(-1).getYear(), date.plusMonths(-1).getMonth().getValue());
  }*/

 /* private void setEvents(int year, int month) {

    Date date = Dates.toDate(year, month);

    for (Date day : businessLogic.getEventsMonth(date)) {
      holidays.add(Dates.convertToLocalDateViaInstant(day));
    }
  }*/

    @FXML
    void initialize() {

        btnCreateRide.setStyle("-fx-background-color: #f85774");

/*
    Callback<ListView<Event>, ListCell<Event>> factory = lv -> new ListCell<>() {
      @Override
      protected void updateItem(Event item, boolean empty) {
        super.updateItem(item, empty);
        setText(empty ? "" : item.getDescription());
      }
    };


     comboEvents.setCellFactory(factory);
    comboEvents.setButtonCell(factory.call(null));

 */


        // setEventsPrePost(LocalDate.now().getYear(), LocalDate.now().getMonth().getValue());

        // get a reference to datepicker inner content
        // attach a listener to the  << and >> buttons
        // mark events for the (prev, current, next) month and year shown
        datePicker.setOnMouseClicked(e -> {
            DatePickerSkin skin = (DatePickerSkin) datePicker.getSkin();
            skin.getPopupContent().lookupAll(".button").forEach(node -> {
                node.setOnMouseClicked(event -> {
                    List<Node> labels = skin.getPopupContent().lookupAll(".label").stream().toList();
                    String month = ((Label) (labels.get(0))).getText();
                    String year = ((Label) (labels.get(1))).getText();
                    YearMonth ym = Dates.getYearMonth(month + " " + year);
                    // setEventsPrePost(ym.getYear(), ym.getMonthValue());
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

        // when a date is selected...
        datePicker.setOnAction(actionEvent -> {
     /* comboEvents.getItems().clear();

      oListEvents = FXCollections.observableArrayList(new ArrayList<>());
      oListEvents.setAll(businessLogic.getEvents(Dates.convertToDate(datePicker.getValue())));

      comboEvents.setItems(oListEvents);

      if (comboEvents.getItems().size() == 0)
        btnCreateRide.setDisable(true);
      else {
         btnCreateRide.setDisable(false);
        // select first option
        comboEvents.getSelectionModel().select(0);
      }
*/
        });

    }

    @Override
    public void setMainApp(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
    }
}