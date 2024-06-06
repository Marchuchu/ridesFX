package eus.ehu.ridesfx.uicontrollers;

import eus.ehu.ridesfx.businessLogic.BlFacade;
import eus.ehu.ridesfx.domain.User;
import eus.ehu.ridesfx.ui.MainGUI;
import eus.ehu.ridesfx.utils.StringUtils;
import javafx.application.Platform;
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
    private List<LocalDate> holidays = new ArrayList<>();

    public CreateRideController(BlFacade bl, MainGUI mGUI) {
        this.businessLogic = bl;
        this.mainGUI = mGUI;
    }


    public CreateRideController(BlFacade bl) {
        this.businessLogic = bl;
    }

    /**
     * @FXML void closeClick(ActionEvent event) {
     * clearErrorLabels();
     * mainGUI.showMain();
     * }
     */

    private void clearErrorLabels() {
        lblErrorMessage.setText("");
        lblErrorMinBet.setText("");
        lblErrorMinBet.getStyleClass().clear();
        lblErrorMessage.getStyleClass().clear();
    }

//    private String field_Errors() {
//
//        try {
//            if ((txtDepartCity.getText().length() == 0) || (txtArrivalCity.getText().length() == 0) || (txtNumberOfSeats.getText().length() == 0) || (txtPrice.getText().length() == 0))
//                return ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.ErrorQuery");
//            else {
//
//                // trigger an exception if the introduced string is not a number
//                int inputSeats = Integer.parseInt(txtNumberOfSeats.getText());
//
//                if (inputSeats <= 0) {
//                    return ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.SeatsMustBeGreaterThan0");
//                } else {
//                    float price = Float.parseFloat(txtPrice.getText());
//                    if (price <= 0)
//                        return ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.PriceMustBeGreaterThan0");
//
//                    else
//                        return null;
//
//                }
//            }
//        } catch (NumberFormatException e1) {
//
//            return ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.ErrorNumber");
//        } catch (Exception e1) {
//
//            e1.printStackTrace();
//            return null;
//
//        }
//    }

    void displayMessage(String message, String label) {
        lblErrorMessage.getStyleClass().clear();
        lblErrorMessage.getStyleClass().setAll("lbl", "lbl-" + label);
        lblErrorMessage.setText(message);
    }

    boolean addC(String c) {

        String from = txtDepartCity.getText();
        String to = txtArrivalCity.getText();
        boolean a, b = false;
        a = false;

        List<String> deptCities = businessLogic.getDepartCities();
        List<String> destCities = businessLogic.getDestinationCities(from);

        if (!deptCities.contains(from)) {
            businessLogic.addCitie(from);
            a = true;
        }

        if (!destCities.contains(to)) {
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

        if (date == null) {

            lblErrorMessage.setText(StringUtils.translate("CreateRideGUI.FillDate"));

            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(5000);
                    Platform.runLater(() -> lblErrorMessage.setVisible(false));
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            });
            thread.start();

            lblErrorMessage.setStyle("-fx-text-fill: #d54242");
            return;
        }

        if (date.isBefore(LocalDate.now())) {

            lblErrorMessage.setText(StringUtils.translate("CreateRideGUI.DateMustBeLaterThanToday"));
            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(5000);
                    Platform.runLater(() -> lblErrorMessage.setVisible(false));
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            });
            thread.start();
            lblErrorMessage.setStyle("-fx-text-fill: #d54242");
            return;

        }

        if (from.isEmpty()) {

            lblErrorMessage.setText(StringUtils.translate("CreateRideGUI.FillDepartureCity"));
            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(5000);
                    Platform.runLater(() -> lblErrorMessage.setVisible(false));
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            });
            thread.start();
            lblErrorMessage.setStyle("-fx-text-fill: #d54242");
            return;


        }

        if (to.isEmpty()) {

            lblErrorMessage.setStyle("-fx-text-fill: #d54242");
            lblErrorMessage.setText(StringUtils.translate("CreateRideGUI.FillArrivalCity"));
            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(5000);
                    Platform.runLater(() -> lblErrorMessage.setVisible(false));
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            });
            thread.start();
            return;

        }

        if (txtNumberOfSeats.getText() == null) {

            lblErrorMessage.setText(StringUtils.translate("CreateRideGUI.FillNumberOfSeats"));
            lblErrorMessage.setStyle("-fx-text-fill: #d54242");
            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(5000);
                    Platform.runLater(() -> lblErrorMessage.setVisible(false));
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            });
            thread.start();
            return;

        }

        int numPlaces = 0;

        try {
            numPlaces = Integer.parseInt(txtNumberOfSeats.getText());
        } catch (NumberFormatException e1) {

            lblErrorMessage.setText(StringUtils.translate("CreateRideGUI.NumberOfSeatsMustBeANumber"));
            lblErrorMessage.setStyle("-fx-text-fill: #d54242");
            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(5000);
                    Platform.runLater(() -> lblErrorMessage.setVisible(false));
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            });
            thread.start();
            return;
        }

        if (txtPrice.getText() == null) {

            lblErrorMessage.setText(StringUtils.translate("CreateRideGUI.FillPrice"));
            lblErrorMessage.setStyle("-fx-text-fill: #d54242");
            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(5000);
                    Platform.runLater(() -> lblErrorMessage.setVisible(false));
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            });
            thread.start();
            return;
        }

        float price = 0;
        try {
            price = Float.parseFloat(txtPrice.getText());
        } catch (NumberFormatException e1) {

            lblErrorMessage.setText(StringUtils.translate("CreateRideGUI.PriceMustBeANumber"));
            lblErrorMessage.setStyle("-fx-text-fill: #d54242");
            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(5000);
                    Platform.runLater(() -> lblErrorMessage.setVisible(false));
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            });
            thread.start();
            return;
        }

        if (numPlaces > 5) {

            lblErrorMessage.setText(StringUtils.translate("CreateRideGUI.NumberOfSeatsMustBeLessThan5"));
            lblErrorMessage.setStyle("-fx-text-fill: #d54242");
            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(5000);
                    Platform.runLater(() -> lblErrorMessage.setVisible(false));
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            });
            thread.start();
            return;
        }


        User user = businessLogic.getCurrentUser();
        businessLogic.createRideClick(from, to, Dates.convertToDate(date), numPlaces, price, user.getEmail());
        lblErrorMessage.setText(StringUtils.translate("CreateRideGUI.RideCreated"));

        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(5000);
                Platform.runLater(() -> lblErrorMessage.setVisible(false));
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        });

        // TODO: add new city to the combo box
        List<String> deptCities = businessLogic.getDepartCities();
        List<String> destCities = businessLogic.getDestinationCities(from);

        if (!deptCities.contains(from)) {
            businessLogic.addCitie(from);
        }

        if (!destCities.contains(to)) {
            businessLogic.addCitie(to);
        }


    }


    @FXML
    void initialize() {

        btnCreateRide.setStyle("-fx-background-color: #f85774");
        lblErrorMessage.setText("");

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
        });

    }

    @Override
    public void setMainApp(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
    }

    @Override
    public void changeLanguage(ResourceBundle resources) {

        btnCreateRide.setText(resources.getString("CreateRide"));

    }

    @Override
    public void showHide() {

    }
}