package eus.ehu.ridesfx.uicontrollers;

import eus.ehu.ridesfx.businessLogic.BlFacade;
import eus.ehu.ridesfx.domain.Driver;
import eus.ehu.ridesfx.domain.Traveler;
import eus.ehu.ridesfx.domain.User;
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
    private Button eng;

    @FXML
    private Button es;

    @FXML
    private Button eus;

    @FXML
    private Label rolName;


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

//    void showName(String role) {
//
//        if (businessLogic.getCurrentUser().getClass().equals(Driver.class) || businessLogic.getCurrentUser().getClass().equals(Traveler.class)) {
//            rolName.setText(businessLogic.getCurrentUser().getName());
//        } else {
//            rolName.setText("Anonymous");
//        }
//
//    }

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


    public BorderPane getMainWrapper1() {
        return mainWrapper;
    }

//    public void setBusinessLogic(BlFacade businessLogic) {
//        this.businessLogic = businessLogic;
//    }
//
//    public void setMainGUI(MainGUI mainGUI) {
//        this.mGUI = mainGUI;
//    }

    @FXML
    void onClickExit(ActionEvent event) throws IOException {

        mGUI.showScene("Log Out");
        signUpButton.setVisible(true);
        logInButton.setVisible(true);
        queryRidesBtn.setVisible(true);
        exitBttn.setVisible(false);
        createRidesBtn.setVisible(false);
        seeAlertsBttn.setVisible(false);
        seeAlertsBttn.setVisible(false);

        businessLogic.setCurrentUser(new User());
        rolName.setText("Guest");


    }

    @FXML
    void onClickSignUp(ActionEvent event) throws IOException {
        mGUI.showScene("Sign Up");

        signUpButton.setVisible(false);
        logInButton.setVisible(false);
        queryRidesBtn.setVisible(true);
        exitBttn.setVisible(true);

        if (businessLogic.getCurrentUser().getClass().equals(Traveler.class) || businessLogic.getCurrentUser().getClass().equals(Driver.class)) {

            rolName.setText(businessLogic.getCurrentUser().getName());

        } else {

            rolName.setText("Guest");

        }


    }


    @FXML
    void onClickLogIn(ActionEvent event) throws IOException {

        mGUI.showScene("Log In");

        createRidesBtn.setVisible(false);
        logInButton.setVisible(false);
        signUpButton.setVisible(false);
        exitBttn.setVisible(true);

        if (businessLogic.getCurrentUser().getClass().equals(Traveler.class) || businessLogic.getCurrentUser().getClass().equals(Driver.class)) {

            rolName.setText(businessLogic.getCurrentUser().getName());

        } else {

            rolName.setText("Guest");

        }


    }

    @FXML
    void queryRides(ActionEvent event) {

        mGUI.showScene("Query Rides");

        if (mGUI.getBusinessLogic().getCurrentUser().getClass().equals(Driver.class)) {
            createRidesBtn.setVisible(true);
            queryRidesBtn.setVisible(false);
            exitBttn.setVisible(true);
            logInButton.setVisible(false);
            signUpButton.setVisible(false);
            seeAlertsBttn.setVisible(true);

        } else if (mGUI.getBusinessLogic().getCurrentUser().getClass().equals(Traveler.class)) {
            queryRidesBtn.setVisible(true);
            createRidesBtn.setVisible(false);
            exitBttn.setVisible(true);
            createRidesBtn.setVisible(false);
            seeAlertsBttn.setVisible(true);

        } else {

            logInButton.setVisible(true);
            signUpButton.setVisible(true);
            queryRidesBtn.setVisible(true);
            exitBttn.setVisible(false);
            createRidesBtn.setVisible(false);
            seeAlertsBttn.setVisible(false);

        }


    }

    @FXML
    void createRide(ActionEvent event){

        mGUI.showScene("Create Ride");
        queryRidesBtn.setVisible(false);
        exitBttn.setVisible(true);
        logInButton.setVisible(false);
        signUpButton.setVisible(false);

    }

    @FXML
    void seeAlerts(ActionEvent event)  {
        mGUI.showScene("See Alerts");

        if (mGUI.getBusinessLogic().getCurrentUser().getClass().equals(Driver.class)) {
            queryRidesBtn.setVisible(false);
        } else if (mGUI.getBusinessLogic().getCurrentUser().getClass().equals(Traveler.class)) {
            createRidesBtn.setVisible(false);

        }
    }

    @FXML
    public void initialize() throws IOException {

        createRidesBtn.setVisible(false);

        exitBttn.setVisible(false);
        seeAlertsBttn.setVisible(false);


        exitBttn.setStyle("-fx-background-color: #f85774");
        logInButton.setStyle("-fx-background-color: #f85774");
        signUpButton.setStyle("-fx-background-color: #f85774");
        queryRidesBtn.setStyle("-fx-background-color: #f85774");
        createRidesBtn.setStyle("-fx-background-color: #f85774");
        seeAlertsBttn.setStyle("-fx-background-color: #f85774");
        es.setStyle("-fx-background-color: #f85774");
        eng.setStyle("-fx-background-color: #f85774");
        eus.setStyle("-fx-background-color: #f85774");


        businessLogic.setCurrentUser(new User());
        rolName.setText(ResourceBundle.getBundle("Etiquetas", Locale.getDefault()).getString("Guest"));


    }


    @Override
    public void setMainApp(MainGUI mainGUI) {
        this.mGUI = mainGUI;
    }

    @FXML
    public void changeLanguage(ActionEvent event) {

        Button b = (Button) event.getSource();
        Locale loc = new Locale(b.getId());

        ResourceBundle resources = ResourceBundle.getBundle("Etiquetas", loc);

        signUpButton.setText(resources.getString("SignUp"));
        exitBttn.setText(resources.getString("LogIn"));
        createRidesBtn.setText(resources.getString("CreateRide"));
        queryRidesBtn.setText(resources.getString("QueryRides"));
        logInButton.setText(resources.getString("LogIn"));
        seeAlertsBttn.setText(resources.getString("SeeAlerts"));
        exitBttn.setText(resources.getString("Exit"));
        rolName.setText(resources.getString("Guest"));

    }

}
