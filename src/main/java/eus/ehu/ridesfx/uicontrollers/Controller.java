package eus.ehu.ridesfx.uicontrollers;

import eus.ehu.ridesfx.domain.Alert;
import eus.ehu.ridesfx.domain.User;
import eus.ehu.ridesfx.ui.MainGUI;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

public interface Controller {
    void setMainApp(MainGUI mainGUI);

    void changeLanguage();

    void showHide();

//    void time(String txt, int s, Label mssg);
    void time(int s, Label msg);

    void getAlerts(User t);

    void getAllAlerts();

    TableView<Alert> getTblAlerts();

    void updateComboBoxes(String from);
}
