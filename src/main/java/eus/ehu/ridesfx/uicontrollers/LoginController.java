package eus.ehu.ridesfx.uicontrollers;

import eus.ehu.ridesfx.businessLogic.BlFacade;
import eus.ehu.ridesfx.domain.Alert;
import eus.ehu.ridesfx.domain.Traveler;
import eus.ehu.ridesfx.domain.User;
import eus.ehu.ridesfx.exceptions.UnknownUser;
import eus.ehu.ridesfx.ui.MainGUI;
import eus.ehu.ridesfx.utils.StringUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

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

    //Buttons methods

    @FXML
    void onClickLogIn(ActionEvent event) {

        if (logInButt.getText() == null || password.getText() == null) {

            mGUI.mGUIC.getSeeAlertsBttn().setVisible(false);
            showErrorMessage("LoginController.LoginFailed", hasLogin, "-fx-text-fill: #d54242", 5);

        } else {

            try {

               if ((businessLogic.logIn(email.getText(), password.getText()))) {

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
                        mGUI.mGUIC.getCreateRidesBtn().setVisible(true);
                        mGUI.mGUIC.getLogInButton().setVisible(false);
                        mGUI.mGUIC.getSignUpButton().setVisible(false);
                        mGUI.mGUIC.getExitBttn().setVisible(true);
                        mGUI.mGUIC.getSeeMessagesBttn().setVisible(true);

                        mGUI.showScene("Create Ride");

                    }

                }


            } catch (UnknownUser unknownUser) {

                showErrorMessage("LoginController.LoginFailed", hasLogin, "-fx-text-fill: #d54242", 5);

            }

        }


    }

    //Auxiliar methods

    @Override
    public void changeLanguage() {

        logInButt.setText(StringUtils.translate("LoginController.LogIn"));
        hasLogin.setText(StringUtils.translate("LoginController.LoginFailed"));
        EmailTXT.setText(StringUtils.translate("Email"));
        PasswordTXT.setText(StringUtils.translate("Password"));

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

    @Override
    public void showErrorMessage(String txt, Label label, String style, int t){

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
    public void showHide() {

    }

    @Override
    public void getAllAlerts() {

    }

    @Override
    public TableView<Alert> getTblAlerts() {
        return null;
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
