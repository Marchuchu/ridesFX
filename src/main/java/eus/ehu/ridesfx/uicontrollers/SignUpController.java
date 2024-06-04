package eus.ehu.ridesfx.uicontrollers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import eus.ehu.ridesfx.businessLogic.BlFacade;
import eus.ehu.ridesfx.domain.Driver;
import eus.ehu.ridesfx.domain.Traveler;
import eus.ehu.ridesfx.domain.User;
import eus.ehu.ridesfx.exceptions.UnknownUser;
import eus.ehu.ridesfx.ui.MainGUI;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class SignUpController implements Controller {

    String r;
    BlFacade businessLogic;
    MainGUI mainGUI;
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private TextField email;
    @FXML
    private Label hasLogin;
    @FXML
    private AnchorPane mainWrapper;
    @FXML
    private TextField name;
    @FXML
    private TextField password;
    @FXML
    private TextField repPas;
    @FXML
    private Button signUpButt;
    @FXML
    private ComboBox<String> role;

    private MainGUI.Window queryRideWin;

    public SignUpController(BlFacade bl) {
        this.businessLogic = bl;
    }


    public SignUpController(BlFacade bl, MainGUI mainGUI) {
        this.businessLogic = bl;
        this.mainGUI = mainGUI;
    }

    @Override
    public void setMainApp(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
    }

    @Override
    public void changeLanguage(ResourceBundle resources) {

    }

    @FXML
    void onClickSignUp(ActionEvent event) {

        if (name.getText() == null || email.getText() == null || password.getText() == null || repPas.getText() == null || role.getValue() == null) {

            hasLogin.setText(translate("SignUpController.EmptyFields"));
            mainGUI.mGUIC.getSeeAlertsBttn().setVisible(false);
            hasLogin.setStyle("-fx-text-fill: #d54242");

        } else {

            User user = new User(email.getText(), name.getText(), password.getText());

            if (role.getValue().equals("Driver")) {
                user = new Driver(email.getText(), name.getText(), password.getText(), repPas.getText());
            } else if (role.getValue().equals("Traveler")) {
                user = new Traveler(email.getText(), name.getText(), password.getText(), repPas.getText());
            }

            if (password.getText().equals(repPas.getText())) {

                if (businessLogic.signUp(name.getText(), email.getText(), password.getText(), repPas.getText(), r)) {

                    if (mainGUI != null) {

                        mainGUI.mGUIC.setRolName(user.getName());
                        businessLogic.setCurrentUser(user);

                        if (user.getClass().equals(Traveler.class)) {
                            mainGUI.showScene("Query Rides");
                            mainGUI.mGUIC.getCreateRidesBtn().setVisible(false);
                            mainGUI.mGUIC.getQueryRidesBtn().setVisible(true);

                        } else {
                            mainGUI.showScene("Create Ride");
                            mainGUI.mGUIC.getQueryRidesBtn().setVisible(false);
                            mainGUI.mGUIC.getCreateRidesBtn().setVisible(true);
                        }

                        name.setText("");
                        email.setText("");
                        password.setText("");
                        repPas.setText("");
                        role.setValue(null);

                        mainGUI.mGUIC.getSeeAlertsBttn().setVisible(true);


                    }


                } else if (!email.getText().contains("@")) {

                    hasLogin.setText(translate("SignUpController.ValidEmail"));
                    hasLogin.setStyle("-fx-text-fill: #d54242");

                    PauseTransition pause = new PauseTransition(Duration.seconds(3));
                    pause.setOnFinished(e -> {
                        hasLogin.setText("");
                    });
                    pause.play();

                } else {

                    hasLogin.setText(translate("SignUpController.UserExists"));
                    hasLogin.setStyle("-fx-text-fill: #d54242");

                    PauseTransition pause = new PauseTransition(Duration.seconds(3));
                    pause.setOnFinished(e -> {
                        hasLogin.setText("");
                    });
                    pause.play();

                }

            } else {

                hasLogin.setText(translate("SignUpController.PasswordsDontMatch"));
                hasLogin.setStyle("-fx-text-fill: #d54242");

                PauseTransition pause = new PauseTransition(Duration.seconds(3));
                pause.setOnFinished(e -> {
                    hasLogin.setText("");
                });
                pause.play();

            }

        }
    }

    @FXML
    void initialize() {

        role.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            r = newValue;
        });

        role.setItems(FXCollections.observableArrayList("Driver", "Traveler"));

        signUpButt.setStyle("-fx-background-color: #f85774");
        signUpButt.setText("Sign Up");


    }

    String translate(String txt) {
        return ResourceBundle.getBundle("Etiquetas").getString(txt);
    }

    public class Window {
        private Controller controller;
        private Parent ui;
    }

}
