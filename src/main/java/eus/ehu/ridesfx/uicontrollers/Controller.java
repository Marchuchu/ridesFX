package eus.ehu.ridesfx.uicontrollers;

import eus.ehu.ridesfx.ui.MainGUI;

import java.util.ResourceBundle;

public interface Controller {
    void setMainApp(MainGUI mainGUI);

    void changeLanguage(ResourceBundle resources);
}
