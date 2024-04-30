package eus.ehu.ridesfx.ui;

import eus.ehu.ridesfx.businessLogic.BlFacade;
import eus.ehu.ridesfx.domain.Ride;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;
import eus.ehu.ridesfx.uicontrollers.Controller;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainGUI {

    //private Window mainWin, createRideWin, queryRidesWin;
    //private Window loginWin, gradingWin;

    private BlFacade businessLogic;
    private Stage stage;
    private Scene scene;

    class Window {
        Controller c;
        Parent ui;
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

    private Window load(String fxmlfile) throws IOException {
//        Window window = new Window();
//        FXMLLoader loader = new FXMLLoader(MainGUI.class.getResource(fxmlfile), ResourceBundle.getBundle("Etiquetas", Locale.getDefault()));
//        loader.setControllerFactory(controllerClass -> {
//            try {
//                return controllerClass
//                        .getConstructor(BlFacade.class)
//                        .newInstance(businessLogic);
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        });
//        window.ui = loader.load();
//        ((Controller) loader.getController()).setMainApp(this);
//        window.c = loader.getController();
//        return window;

        return null;

    }



    public void init(Stage stage) throws IOException {

        this.stage = stage;

        FXMLLoader loader = new FXMLLoader(MainGUI.class.getResource("MainGUI.fxml"), ResourceBundle.getBundle("Etiquetas", Locale.getDefault()));
        loader.setControllerFactory(controllerClass -> {
            try {
                return controllerClass
                        .getConstructor(BlFacade.class)
                        .newInstance(businessLogic);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        Scene scene = new Scene(loader.load());
        stage.setTitle("ShareTrip BorderLayout");
        stage.setScene(scene);
        stage.setHeight(700);
        stage.setWidth(1100);
        stage.show();

    }


/*
    public void showMain() {
        setupScene(mainWin.ui, "MainTitle", 320, 250);
    }

//  public void start(Stage stage) throws IOException {
//      init(stage);
//  }

    public void showQueryRides() {
        setupScene(queryRidesWin.ui, "QueryRides", 1000, 500);
    }

    public void showCreateRide() {
        setupScene(createRideWin.ui, "CreateRide", 550, 400);
    }

    private void setupScene(Parent ui, String title, int width, int height) {
        if (scene == null) {
            scene = new Scene(ui, width, height);
            scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
            stage.setScene(scene);
        }
        stage.setWidth(width);
        stage.setHeight(height);
        stage.setTitle(ResourceBundle.getBundle("Etiquetas", Locale.getDefault()).getString(title));
        scene.setRoot(ui);
        stage.show();
    }
*/


//  public static void main(String[] args) {
//    launch();
//  }
}
