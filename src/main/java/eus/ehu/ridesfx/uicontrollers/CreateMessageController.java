package eus.ehu.ridesfx.uicontrollers;

import java.net.URL;
import java.util.ResourceBundle;

import eus.ehu.ridesfx.ui.MainGUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class CreateMessageController implements Controller{

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Text errorMessage;

    @FXML
    private AnchorPane mainWrapper;

    @FXML
    private TextArea messageField;

    @FXML
    private Button sendMessageBttn;

    @FXML
    private TextField subjectBox;

    @FXML
    private TextField toBox;

    @FXML
    void onClickSendMessage(ActionEvent event) {

    }

    @FXML
    void initialize() {

    }

    @Override
    public void setMainApp(MainGUI mainGUI) {

    }

    @Override
    public void changeLanguage(ResourceBundle resources) {

    }
}
