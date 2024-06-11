package eus.ehu.ridesfx.uicontrollers;

import eus.ehu.ridesfx.businessLogic.BlFacade;
import eus.ehu.ridesfx.domain.Alert;
import eus.ehu.ridesfx.domain.User;
import eus.ehu.ridesfx.ui.MainGUI;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

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
    public void getAlerts(User t) {

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
    public void changeLanguage() {

    }

    @Override
    public void showHide() {

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
