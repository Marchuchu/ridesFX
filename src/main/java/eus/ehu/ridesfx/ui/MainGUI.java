package eus.ehu.ridesfx.ui;

import eus.ehu.ridesfx.businessLogic.*;
import eus.ehu.ridesfx.uicontrollers.*;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import eus.ehu.ridesfx.uicontrollers.Controller;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainGUI {

    public MainGUIController mGUIC;
    private BlFacade businessLogic;
    private Window createRideWin, queryRideWin, loginWin, signUWin, mainWin, logoutWin, alertWin, seeMessageWin, createMessageWin;
    private Stage stage;
    private Scene scene;

    public MainGUI(BlFacade bl) {
        Platform.startup(() -> {
            try {
                setBusinessLogic(bl);
                init(new Stage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public BlFacade getBusinessLogic() {
        return businessLogic;
    }

    public void setBusinessLogic(BlFacade afi) {
        businessLogic = afi;
    }

    public Window load(String file) throws IOException {
        Window window = new Window();
        FXMLLoader loader = new FXMLLoader(MainGUI.class.getResource(file), ResourceBundle.getBundle("Etiquetas", Locale.getDefault()));
        loader.setControllerFactory(controllerClass -> {
            try {
                if (controllerClass == SignUpController.class || controllerClass == LoginController.class) {
                    return controllerClass
                            .getConstructor(BlFacade.class, MainGUI.class)
                            .newInstance(businessLogic, this);
                }
                return controllerClass
                        .getConstructor(BlFacade.class)
                        .newInstance(businessLogic);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        window.ui = loader.load();
        ((Controller) loader.getController()).setMainApp(this);
        window.controller = loader.getController();
        return window;
    }

    public void showScene(String scene) {

        switch (scene) {

            case "Query Rides" -> mGUIC.getMainWrapper().setCenter(queryRideWin.ui);
            case "Create Ride" -> mGUIC.getMainWrapper().setCenter(createRideWin.ui);
            case "Sign Up" -> mGUIC.getMainWrapper().setCenter(signUWin.ui);
            case "Log In" -> mGUIC.getMainWrapper().setCenter(loginWin.ui);
            case "Log Out" -> mGUIC.getMainWrapper().setCenter(logoutWin.ui);
            case "See Alerts" -> mGUIC.getMainWrapper().setCenter(alertWin.ui);
            case "See Messages" -> mGUIC.getMainWrapper().setCenter(seeMessageWin.ui);
            case "Send Message" -> mGUIC.getMainWrapper().setCenter(createMessageWin.ui);

        }
    }

    public void init(Stage stage) throws IOException {

        this.stage = stage;

        mainWin = load("MainGUI.fxml");
        mGUIC = (MainGUIController) mainWin.controller;

        queryRideWin = load("QueryRides.fxml");
        createRideWin = load("CreateRide.fxml");
        signUWin = load("SignUp.fxml");
        loginWin = load("LogIn.fxml");
        logoutWin = load("Exit.fxml");
        alertWin = load("AlertTable.fxml");
        seeMessageWin = load("Messages.fxml");
        createMessageWin = load("CreateMessage.fxml");

        Scene scene = new Scene(mainWin.ui);
        stage.setTitle("ShareTrip BorderLayout");
        stage.setScene(scene);
        stage.setWidth(1150);
        stage.setHeight(740);
        stage.show();
    }

    public void alertsShowHideButtons() {
        (alertWin.controller).showHide();
    }


    public static class Window {

        public Controller controller;
        public Parent ui;

    }
}
