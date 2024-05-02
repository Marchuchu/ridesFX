package eus.ehu.ridesfx.uicontrollers;

import eus.ehu.ridesfx.businessLogic.BlFacade;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import eus.ehu.ridesfx.ui.MainGUI;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
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

      private BlFacade businessLogic;

    @FXML
    private BorderPane mainWrapper;

    MainGUI mGUI;

    MainGUI.Window createRideWin, queryRideWin, loginWin, signUWin;


    private Stage stage;
    private Scene scene;

    public void setMainWrapper(BorderPane mainWrapper) {
        this.mainWrapper = mainWrapper;
    }

    public MainGUIController() {
    }

    public MainGUIController(BlFacade blFacade) {
        businessLogic = blFacade;
    }

    public MainGUIController(BlFacade blFacade, MainGUI mGUI) {
        this.mGUI = mGUI;
        businessLogic = blFacade;
    }

    public BlFacade getBusinessLogic() {
        return businessLogic;
    }

//    Window load(String fxml) {
//
//        try {
//
//            FXMLLoader loader = new FXMLLoader(MainGUI.class.getResource(fxml), ResourceBundle.getBundle("Etiquetas", Locale.getDefault()));
//            loader.setControllerFactory(controllerClass -> {
//                try {
//                    return controllerClass
//                            .getConstructor(BlFacade.class)
//                            .newInstance(businessLogic);
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
//            });
//
//            Parent ui = loader.load();
//            Controller controller = loader.getController();
//
//            Window window = new Window();
//            window.controller = controller;
//            window.ui = ui;
//            return window;
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//
//        }
//
//    }



    void showName(String role) {

        if (role.equals("Driver")) {
            rolName.setText(businessLogic.getCurrentDriver().getName());
        } else if (role.equals("Traveler")) {
            rolName.setText(businessLogic.getCurrentTraveler().getName());
        } else {

            rolName.setText("Anonymous");

        }

    }

    public BorderPane getMainWrapper() {
        return mainWrapper;
    }

    @FXML
    void onClickSignUp(ActionEvent event) throws IOException {
        mGUI.showScene("Sign Up");
        showName(rolName.getText());
    }


    @FXML
    void onClickLogIn(ActionEvent event) throws IOException {
        mGUI.showScene("Log In");
        showName(rolName.getText());

    }

    @FXML
    void queryRides(ActionEvent event) throws IOException {

        mGUI.showScene("Query Rides");
    }

    @FXML
    void createRide(ActionEvent event) throws IOException {

        mGUI.showScene("Create Ride");
    }

    @FXML
    public void initialize() throws IOException {

        mainWrapper = new BorderPane();

        queryRideWin = mGUI.load("QueryRides.fxml");
        createRideWin = mGUI.load("CreateRide.fxml");
        signUWin = mGUI.load("SignUp.fxml");
        loginWin = mGUI.load("LogIn.fxml");

        mGUI.showScene("Query Rides");

        //mGUI = new MainGUI(businessLogic);

//        queryRideWin = mGUI.load("QueryRides.fxml");
//        createRideWin = mGUI.load("CreateRide.fxml");
//        loginWin = mGUI.load("LogIn.fxml");
//        signUWin = mGUI.load("SignUp.fxml");
//
//        queryRideWin.controller = new QueryRidesController(businessLogic);
//        createRideWin.controller = new CreateRideController(businessLogic);
//        loginWin.controller = new LoginController(businessLogic);
//        signUWin.controller = new SignUpController(businessLogic);
    }


    @Override
    public void setMainApp(MainGUI mainGUI) {
        this.mGUI = mainGUI;
    }

//    public void showScene(String scene) throws IOException {
//
//        switch (scene) {
//            case "Query Rides" -> mainWrapper.setCenter(queryRideWin.ui);
//            case "Create Ride" -> mainWrapper.setCenter(createRideWin.ui);
//            case "Log In" -> mainWrapper.setCenter(loginWin.ui);
//            case "Sign Up" -> mainWrapper.setCenter(signUWin.ui);
//
//        }
//
//    }

    public static class Window {
        private Controller controller;
        private Parent ui;

    }
}
