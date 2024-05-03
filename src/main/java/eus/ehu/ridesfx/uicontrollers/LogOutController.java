package eus.ehu.ridesfx.uicontrollers;

import eus.ehu.ridesfx.businessLogic.BlFacade;
import eus.ehu.ridesfx.ui.MainGUI;
import javafx.fxml.FXML;

public class LogOutController implements Controller {

    private final BlFacade businessLogic;
    private MainGUI mainGUI;

    public LogOutController(BlFacade bl) {
        this.businessLogic = bl;
    }

    public LogOutController(BlFacade bl, MainGUI mainGUI) {
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
}
