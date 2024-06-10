package eus.ehu.ridesfx.uicontrollers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import eus.ehu.ridesfx.businessLogic.BlFacade;
import eus.ehu.ridesfx.domain.Alerts;
import eus.ehu.ridesfx.domain.Driver;
import eus.ehu.ridesfx.domain.Traveler;
import eus.ehu.ridesfx.domain.User;
import eus.ehu.ridesfx.exceptions.UnknownUser;
import eus.ehu.ridesfx.ui.MainGUI;
import eus.ehu.ridesfx.utils.StringUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class SignUpController implements Controller {

    String r;
    BlFacade businessLogic;
    MainGUI mainGUI;

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

    @FXML
    private Text emailTXT;

    @FXML
    private Text nameTXT;

    @FXML
    private Text passTXT;

    @FXML
    private Text repPassTXT;

    @FXML
    private Text rolTXT;

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


    @FXML
    void onClickSignUp(ActionEvent event) {

        if (name.getText() == null || email.getText() == null || password.getText() == null || repPas.getText() == null || role.getValue() == null) {

            mainGUI.mGUIC.getSeeAlertsBttn().setVisible(false);
            hasLogin.setStyle("-fx-text-fill: #d54242");
            hasLogin.setText(StringUtils.translate("SignUpController.EmptyFields"));
            hasLogin.setVisible(true);


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

                        if (user instanceof Traveler) {
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
                        mainGUI.mGUIC.getLogInButton().setVisible(false);
                        mainGUI.mGUIC.getSignUpButton().setVisible(false);
                        mainGUI.mGUIC.getExitBttn().setVisible(true);
                        mainGUI.mGUIC.getSeeMessagesBttn().setVisible(true);


                    }


                } else if (!email.getText().contains("@")) {

                    hasLogin.setText(StringUtils.translate("SignUpController.ValidEmail"));
                    hasLogin.setStyle("-fx-text-fill: #d54242");
                    hasLogin.setVisible(true);

                    time(5, hasLogin);

                } else {

                    hasLogin.setText(StringUtils.translate("SignUpController.UserExists"));
                    hasLogin.setStyle("-fx-text-fill: #d54242");
                    hasLogin.setVisible(true);

                    time(5, hasLogin);

                }

            } else {

                hasLogin.setText(StringUtils.translate("SignUpController.PasswordsDontMatch"));

                hasLogin.setStyle("-fx-text-fill: #d54242");
                hasLogin.setVisible(true);

                time(5, hasLogin);

            }

        }
    }

    @FXML
    void initialize() {

        hasLogin.setVisible(false);

        role.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            r = newValue;

        });

        role.setItems(FXCollections.observableArrayList("Driver", "Traveler"));

        signUpButt.setStyle("-fx-background-color: #f85774");
        signUpButt.setText(StringUtils.translate("SignUpController.SignUp"));


    }

    @Override
    public void changeLanguage() {

        signUpButt.setText(StringUtils.translate("SignUpController.SignUp"));

        emailTXT.setText(StringUtils.translate("Email"));
        nameTXT.setText(StringUtils.translate("Name"));
        passTXT.setText(StringUtils.translate("Password"));
        repPassTXT.setText(StringUtils.translate("SignUpController.RepeatPassword"));
        rolTXT.setText(StringUtils.translate("Role"));

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

    public class Window {
        private Controller controller;
        private Parent ui;
    }

}
