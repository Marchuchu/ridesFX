package eus.ehu.ridesfx.uicontrollers;

import eus.ehu.ridesfx.businessLogic.BlFacade;
import eus.ehu.ridesfx.domain.Alerts;
import eus.ehu.ridesfx.domain.Traveler;
import eus.ehu.ridesfx.domain.User;
import eus.ehu.ridesfx.ui.MainGUI;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

import java.util.ResourceBundle;

public class ExitController implements Controller {

    private final BlFacade businessLogic;
    private MainGUI mainGUI;

    public ExitController(BlFacade bl) {
        this.businessLogic = bl;
    }

    public ExitController(BlFacade bl, MainGUI mainGUI) {
        this.businessLogic = bl;
        this.mainGUI = mainGUI;
    }


    @FXML
    void initialize() {

    }
    @Override
    public void setMainApp(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
    }

    @Override
    public void changeLanguage() {

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

    @Override
    public void updateComboBoxes(String from) {

    }

}
