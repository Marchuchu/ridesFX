package eus.ehu.ridesfx.uicontrollers;

import eus.ehu.ridesfx.businessLogic.BlFacade;
import eus.ehu.ridesfx.domain.Driver;
import eus.ehu.ridesfx.domain.Traveler;
import eus.ehu.ridesfx.domain.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import eus.ehu.ridesfx.ui.MainGUI;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainGUIController implements Controller {

    @FXML
    private Label selectOptionLbl;

    @FXML
    private Label lblDriver;

    @FXML
    private Button en;

    @FXML
    private Button es;

    @FXML
    private Button eus;

    @FXML
    private Label rolName;

    @FXML
    private Button seeMessagesBttn;

    @FXML
    private Button signUpButton;

    @FXML
    private Button exitBttn;

    @FXML
    private Button createRidesBtn;

    @FXML
    private Button queryRidesBtn;

    @FXML
    private Button logInButton;

    @FXML
    private Button seeAlertsBttn;


    @FXML
    private Text userNames;

    private BlFacade businessLogic;

    @FXML
    private BorderPane mainWrapper;

    private MainGUI mGUI;


    public MainGUIController() {
    }

    public MainGUIController(BlFacade blFacade) {
        businessLogic = blFacade;
    }

    public BlFacade getBusinessLogic() {
        return businessLogic;
    }

    public void setBusinessLogic(BlFacade businessLogic) {
        this.businessLogic = businessLogic;
    }

    public Label getRolName() {
        return rolName;
    }

    public void setRolName(String name) {
        this.rolName.setText(name);
    }

    public Button getSignUpButton() {
        return signUpButton;
    }

    public Button getExitBttn() {
        return exitBttn;
    }

    public Button getCreateRidesBtn() {
        return createRidesBtn;
    }

    public Button getQueryRidesBtn() {
        return queryRidesBtn;
    }

    public Button getLogInButton() {
        return logInButton;
    }

    public Button getSeeAlertsBttn() {
        return seeAlertsBttn;
    }

    public BorderPane getMainWrapper() {
        return mainWrapper;
    }

    public Button getSeeMessagesBttn() {
        return seeMessagesBttn;
    }

    public void setMainGUI(MainGUI mainGUI) {
        this.mGUI = mainGUI;
    }


    public BorderPane getMainWrapper1() {
        return mainWrapper;
    }

    @FXML
    void logIn(ActionEvent event) {

        mGUI.showScene("Log In");

        createRidesBtn.setVisible(false);
        logInButton.setVisible(true);
        signUpButton.setVisible(true);
        queryRidesBtn.setVisible(true);
        seeMessagesBttn.setVisible(false);
        seeAlertsBttn.setVisible(false);

        if (businessLogic.getCurrentUser().getClass().equals(Traveler.class) || businessLogic.getCurrentUser().getClass().equals(Driver.class)) {

            rolName.setText(businessLogic.getCurrentUser().getName());

        } else {

            rolName.setText("Guest");

        }


    }

    @FXML
    void signUp(ActionEvent event) {
        mGUI.showScene("Sign Up");

        signUpButton.setVisible(true);
        logInButton.setVisible(true);
        queryRidesBtn.setVisible(true);
        exitBttn.setVisible(false);
        seeMessagesBttn.setVisible(false);
        seeAlertsBttn.setVisible(false);


        if (businessLogic.getCurrentUser().getClass().equals(Traveler.class) || businessLogic.getCurrentUser().getClass().equals(Driver.class)) {

            rolName.setText(businessLogic.getCurrentUser().getName());

        } else {

            rolName.setText("Guest");

        }


    }

    @FXML
    void exit(ActionEvent event) {

        mGUI.showScene("Log Out");
        signUpButton.setVisible(true);
        logInButton.setVisible(true);
        queryRidesBtn.setVisible(true);
        exitBttn.setVisible(false);
        createRidesBtn.setVisible(false);
        seeAlertsBttn.setVisible(false);
        seeMessagesBttn.setVisible(false);

        businessLogic.setCurrentUser(new User());
        rolName.setText("Guest");


    }


    @FXML
    void queryRides(ActionEvent event) {

        mGUI.showScene("Query Rides");
        queryRidesBtn.setVisible(false);

        if (mGUI.getBusinessLogic().getCurrentUser() instanceof Driver ) {
            createRidesBtn.setVisible(true);
            queryRidesBtn.setVisible(true);
            exitBttn.setVisible(true);
            logInButton.setVisible(false);
            signUpButton.setVisible(false);
            seeAlertsBttn.setVisible(true);
            seeMessagesBttn.setVisible(true);

        } else if (mGUI.getBusinessLogic().getCurrentUser() instanceof Traveler ) {
            queryRidesBtn.setVisible(true);
            createRidesBtn.setVisible(false);
            exitBttn.setVisible(true);
            seeAlertsBttn.setVisible(true);
            seeMessagesBttn.setVisible(true);
            logInButton.setVisible(false);
            signUpButton.setVisible(false);

        } else {

            logInButton.setVisible(true);
            signUpButton.setVisible(true);
            queryRidesBtn.setVisible(true);
            exitBttn.setVisible(false);
            createRidesBtn.setVisible(false);
            seeAlertsBttn.setVisible(false);
            seeMessagesBttn.setVisible(false);

        }


    }

    @FXML
    void createRide(ActionEvent event) {

        mGUI.showScene("Create Ride");

        exitBttn.setVisible(true);
        logInButton.setVisible(false);
        signUpButton.setVisible(false);
        createRidesBtn.setVisible(true);
        seeMessagesBttn.setVisible(true);
        seeAlertsBttn.setVisible(true);


    }

    @FXML
    void seeAlerts(ActionEvent event) {

        mGUI.showScene("See Alerts");

        if (mGUI.getBusinessLogic().getCurrentUser() instanceof Driver) {
            queryRidesBtn.setVisible(false);
            createRidesBtn.setVisible(true);
            seeMessagesBttn.setVisible(true);
            seeAlertsBttn.setVisible(true);

        } else if (mGUI.getBusinessLogic().getCurrentUser() instanceof Traveler ) {
            createRidesBtn.setVisible(false);
            seeMessagesBttn.setVisible(true);
            seeAlertsBttn.setVisible(true);

        }


        mGUI.alertsShowHideButtons();
    }

    @FXML
    void seeMessages(ActionEvent event) {

        mGUI.showScene("See Messages");

        if (mGUI.getBusinessLogic().getCurrentUser() instanceof Driver) {
            queryRidesBtn.setVisible(false);
            createRidesBtn.setVisible(true);
            seeMessagesBttn.setVisible(true);
            seeAlertsBttn.setVisible(true);
            logInButton.setVisible(false);
            signUpButton.setVisible(false);
            exitBttn.setVisible(true);

        } else if (mGUI.getBusinessLogic().getCurrentUser() instanceof Traveler) {
            createRidesBtn.setVisible(false);
            seeMessagesBttn.setVisible(true);
            seeAlertsBttn.setVisible(true);
            queryRidesBtn.setVisible(true);
            logInButton.setVisible(false);
            signUpButton.setVisible(false);
            exitBttn.setVisible(true);

        }

    }

    @FXML
    public void initialize() throws IOException {

        createRidesBtn.setVisible(false);

        exitBttn.setVisible(false);
        seeAlertsBttn.setVisible(false);
        seeMessagesBttn.setVisible(false);


        exitBttn.setStyle("-fx-background-color: #f85774");
        logInButton.setStyle("-fx-background-color: #f85774");
        signUpButton.setStyle("-fx-background-color: #f85774");
        queryRidesBtn.setStyle("-fx-background-color: #f85774");
        createRidesBtn.setStyle("-fx-background-color: #f85774");
        seeAlertsBttn.setStyle("-fx-background-color: #f85774");
        seeMessagesBttn.setStyle("-fx-background-color: #f85774");
        es.setStyle("-fx-background-color: #f85774");
        en.setStyle("-fx-background-color: #f85774");
        eus.setStyle("-fx-background-color: #f85774");

        businessLogic.setCurrentUser(new User());
        rolName.setText(ResourceBundle.getBundle("Etiquetas", Locale.getDefault()).getString("Guest"));

    }


    @Override
    public void setMainApp(MainGUI mainGUI) {
        this.mGUI = mainGUI;
    }

    @Override
    public void changeLanguage(ResourceBundle resources) {

        signUpButton.setText(resources.getString("SignUp"));
        exitBttn.setText(resources.getString("Exit"));
        createRidesBtn.setText(resources.getString("CreateRide"));
        queryRidesBtn.setText(resources.getString("QueryRides"));
        logInButton.setText(resources.getString("LogIn"));
        seeAlertsBttn.setText(resources.getString("SeeAlerts"));
        seeMessagesBttn.setText(resources.getString("SeeMessages"));
        exitBttn.setText(resources.getString("Exit"));

    }

    @Override
    public void showHide() {

    }

    @FXML
    public void changeLanguage(ActionEvent event) {

        Button b = (Button) event.getSource();
        Locale.setDefault(new Locale(b.getId()));

    }

    @Override
    public void time(String txt, int s, Label mssg) {

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

}
