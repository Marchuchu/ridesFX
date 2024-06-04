package eus.ehu.ridesfx.uicontrollers;

import eus.ehu.ridesfx.businessLogic.BlFacade;
import eus.ehu.ridesfx.ui.MainGUI;
import javafx.fxml.FXML;

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
    public void changeLanguage(ResourceBundle resources) {

    }


}
