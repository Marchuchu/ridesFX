package eus.ehu.ridesfx.uicontrollers;

import java.net.URL;
import java.util.ResourceBundle;

import eus.ehu.ridesfx.ui.MainGUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class MessageController implements  Controller{

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane mainWrapper;

    @FXML
    private Text errorM;

    @FXML
    private Text errorMessage;

    @FXML
    private TextArea messageTXT;

    @FXML
    private TableView<?> messageTable;

    @FXML
    private Button seeMessageBttn;

    @FXML
    private Button sendNewMessageBttn;

    @FXML
    void onClickSeeMessage(ActionEvent event) {

    }

    @FXML
    void onClickSendNewMessage(ActionEvent event) {

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
