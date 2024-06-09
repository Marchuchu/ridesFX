package eus.ehu.ridesfx.uicontrollers;

import eus.ehu.ridesfx.ui.MainGUI;
import javafx.scene.control.Label;

import java.util.ResourceBundle;

public interface Controller {
    void setMainApp(MainGUI mainGUI);

    void changeLanguage();

    void showHide();

//    void time(String txt, int s, Label mssg);
    void time(int s, Label mssg);
}
