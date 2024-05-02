package eus.ehu.ridesfx.ui;

import eus.ehu.ridesfx.businessLogic.BlFacade;
import eus.ehu.ridesfx.domain.Ride;
import eus.ehu.ridesfx.uicontrollers.LoginController;
import eus.ehu.ridesfx.uicontrollers.MainGUIController;
import eus.ehu.ridesfx.uicontrollers.SignUpController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;
import eus.ehu.ridesfx.uicontrollers.Controller;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainGUI {

    private BlFacade businessLogic;

    private Window createRideWin, queryRideWin, loginWin, signUWin;

    private MainGUIController mGUIC;


    private Stage stage;
    private Scene scene;

    public static class Window {

        public Controller controller;
        public Parent ui;

    }

    public MainGUI(BlFacade bl, MainGUIController mGUIC) {
        this.mGUIC = mGUIC;
        Platform.startup(() -> {
            try {
                setBusinessLogic(bl);
                init(new Stage());
                mGUIC.setMainWrapper(new BorderPane());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

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


    public void showScene(String scene) throws IOException {

        switch (scene) {

            case "Query Rides" -> mGUIC.getMainWrapper().setCenter(queryRideWin.ui);
            case "Create Ride" -> mGUIC.getMainWrapper().setCenter(createRideWin.ui);
            case "Sign Up" -> mGUIC.getMainWrapper().setCenter(signUWin.ui);
            case "Log In" ->    mGUIC.getMainWrapper().setCenter(loginWin.ui);

        }
    }
    public void init(Stage stage) throws IOException {

        this.stage= stage;

        mGUIC = new MainGUIController(businessLogic);
        mGUIC.setMainWrapper(new BorderPane());

        FXMLLoader loader = new FXMLLoader(MainGUI.class.getResource("MainGUI.fxml"), ResourceBundle.getBundle("Etiquetas", Locale.getDefault()));
        loader.setControllerFactory(controllerClass -> {
            try {
                MainGUIController controller = new MainGUIController(businessLogic);
                controller.setMainApp(this);
                return controller;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        queryRideWin = load("QueryRides.fxml");
        createRideWin = load("CreateRide.fxml");
        signUWin = load("SignUp.fxml");
        loginWin = load("LogIn.fxml");

        Scene scene = new Scene(loader.load());
        stage.setTitle("ShareTrip BorderLayout");
        stage.setScene(scene);
        stage.setHeight(700);
        stage.setWidth(1100);
        stage.show();
    }
}
