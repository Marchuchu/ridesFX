package eus.ehu.ridesfx.uicontrollers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import eus.ehu.ridesfx.businessLogic.BlFacade;
import eus.ehu.ridesfx.domain.Traveler;
import eus.ehu.ridesfx.exceptions.UnknownUser;
import eus.ehu.ridesfx.ui.MainGUI;
import eus.ehu.ridesfx.utils.StringUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class LoginController implements Controller {

    BlFacade businessLogic;
    MainGUI mGUI;

    @FXML
    private TextField email;
    @FXML
    private Label hasLogin;
    @FXML
    private Button logInButt;

    @FXML
    private TextField password;

    @FXML
    private Text EmailTXT;

    @FXML
    private Text PasswordTXT;

    public LoginController(BlFacade bl, MainGUI mainGUIC) {
        this.businessLogic = bl;
        this.mGUI = mainGUIC;

    }

    public LoginController(BlFacade bl) {
        this.businessLogic = bl;

    }

    @FXML
    void onClickLogIn(ActionEvent event) {

        if (logInButt.getText() == null || password.getText() == null) {

            mGUI.mGUIC.getSeeAlertsBttn().setVisible(false);
            hasLogin.setText(StringUtils.translate("LoginController.LoginFailed"));
            hasLogin.setVisible(true);

            return;
        } else {

            try {

                if ((businessLogic.logIn(email.getText(), password.getText()))) {

                    //hasLogin.setText("Login successful");

                    email.setText("");
                    password.setText("");

                    mGUI.showScene("Query Rides");
                    mGUI.mGUIC.setRolName(businessLogic.getCurrentUser().getName());

                    if (businessLogic.getCurrentUser() instanceof Traveler) {

                        mGUI.mGUIC.getCreateRidesBtn().setVisible(false);
                        mGUI.mGUIC.getQueryRidesBtn().setVisible(true);
                        mGUI.mGUIC.getSeeAlertsBttn().setVisible(true);
                        mGUI.mGUIC.getLogInButton().setVisible(false);
                        mGUI.mGUIC.getSignUpButton().setVisible(false);
                        mGUI.mGUIC.getExitBttn().setVisible(true);
                        mGUI.mGUIC.getSeeMessagesBttn().setVisible(true);

                        mGUI.showScene("Query Rides");


                    } else {

                        mGUI.mGUIC.getQueryRidesBtn().setVisible(false);
                        mGUI.mGUIC.getSeeAlertsBttn().setVisible(true);
                        mGUI.mGUIC.getCreateRidesBtn().setVisible(false);
                        mGUI.mGUIC.getLogInButton().setVisible(false);
                        mGUI.mGUIC.getSignUpButton().setVisible(false);
                        mGUI.mGUIC.getExitBttn().setVisible(true);
                        mGUI.mGUIC.getSeeMessagesBttn().setVisible(true);

                        mGUI.showScene("Create Ride");

                    }

                }


            } catch (UnknownUser unknownUser) {

                hasLogin.setStyle("-fx-text-fill: #d54242");
                hasLogin.setVisible(true);
                Thread thread = new Thread(() -> {
                    try {
                        Thread.sleep(5000);
                        Platform.runLater(() -> hasLogin.setVisible(false));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                thread.start();

            }

        }


    }


    @FXML
    void initialize() {

        logInButt.setStyle("-fx-background-color: #f85774;");
        hasLogin.setVisible(false);
        EmailTXT.setText(StringUtils.translate("Email"));
        PasswordTXT.setText(StringUtils.translate("Password"));

    }


    @Override
    public void setMainApp(MainGUI mainGUI) {
        this.mGUI = mainGUI;
    }

    @Override
    public void changeLanguage(ResourceBundle resources) {

        logInButt.setText(resources.getString("LoginController.LogIn"));
        hasLogin.setText(resources.getString("LoginController.LoginFailed"));
        EmailTXT.setText(resources.getString("Email"));
        PasswordTXT.setText(resources.getString("Password"));

    }

    @Override
    public void showHide() {

    }


}
