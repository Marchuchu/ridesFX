package eus.ehu.ridesfx.uicontrollers;

import eus.ehu.ridesfx.businessLogic.BlFacade;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import eus.ehu.ridesfx.ui.MainGUI;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainGUIController implements Controller {

    @FXML
    private Label selectOptionLbl;

    @FXML
    private Label lblDriver;

    @FXML
    private ResourceBundle resources;

    @FXML
    private Label rolName;

    @FXML
    private Button signUpButton;

    @FXML
    private Text userNames;

    private MainGUI mainGUI;

    private BlFacade businessLogic;

    @FXML
    private BorderPane mainWrapper;

    private Window createRideWin, queryRideWin, loginWin, signUWin;

    private LoginController lIController;
    private SignUpController sUController;
    private QueryRidesController qRController;
    private CreateRideController cRController;

    private Stage stage;
    private Scene scene;


    public MainGUIController() {
    }

    public MainGUIController(BlFacade blFacade) {
        businessLogic = blFacade;
    }

    public BlFacade getBusinessLogic() {
        return businessLogic;
    }

    Window load(String fxml) {

        try {

            FXMLLoader loader = new FXMLLoader(MainGUI.class.getResource(fxml), ResourceBundle.getBundle("Etiquetas", Locale.getDefault()));
            loader.setControllerFactory(controllerClass -> {
                try {
                    return controllerClass
                            .getConstructor(BlFacade.class)
                            .newInstance(businessLogic);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            Parent ui = loader.load();
            Controller controller = loader.getController();

            Window window = new Window();
            window.controller = controller;
            window.ui = ui;
            return window;
        } catch (IOException e) {
            throw new RuntimeException(e);

        }

    }

    void showScene(String scene) {
        switch (scene) {
            case "Query Rides" -> mainWrapper.setCenter(queryRideWin.ui);
            case "Create Ride" -> mainWrapper.setCenter(createRideWin.ui);
            case "Log In" -> mainWrapper.setCenter(loginWin.ui);
            case "Sign Up" -> mainWrapper.setCenter(signUWin.ui);
        }
    }

    void showName(String role) {

        if (role.equals("Driver")) {
            rolName.setText(businessLogic.getCurrentDriver().getName());
        } else if (role.equals("Traveler")) {
            rolName.setText(businessLogic.getCurrentTraveler().getName());
        } else {

            rolName.setText("Anonymous");

        }

    }

    @FXML
    void onClickSignUp(ActionEvent event) {
        showScene("Sign Up");
        showName(rolName.getText());
    }


    @FXML
    void onClickLogIn(ActionEvent event) {
        showScene("Log In");
        showName(rolName.getText());

    }

    @FXML
    void queryRides(ActionEvent event) {

        //mainGUI.showQueryRides();
        showScene("Query Rides");
    }

    @FXML
    void createRide(ActionEvent event) {

        //mainGUI.showCreateRide();
        showScene("Create Ride");
    }

    @FXML
    void initialize() {

        queryRideWin = load("QueryRides.fxml");
        createRideWin = load("CreateRide.fxml");
        signUWin = load("SignUp.fxml");
        loginWin = load("LogIn.fxml");

        showScene("Query Rides");

    }

    @Override
    public void setMainApp(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
    }

    public class Window {
        private Controller controller;
        private Parent ui;

    }
}
