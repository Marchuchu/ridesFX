package eus.ehu.ridesfx.uicontrollers;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

import eus.ehu.ridesfx.businessLogic.BlFacade;
import eus.ehu.ridesfx.domain.Alerts;
import eus.ehu.ridesfx.domain.Traveler;
import eus.ehu.ridesfx.domain.User;
import eus.ehu.ridesfx.ui.MainGUI;
import eus.ehu.ridesfx.utils.StringUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class CreateMessageController implements Controller {

    BlFacade businessLogic;
    MainGUI mainGUI;

    @FXML
    private Label errorMessage;
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
    private Text subTXT;

    @FXML
    private Text msgTXT;

    @FXML
    private Text toTXT;

    public CreateMessageController(BlFacade bl) {
        this.businessLogic = bl;
    }


    public CreateMessageController(BlFacade bl, MainGUI mainGUI) {
        this.businessLogic = bl;
        this.mainGUI = mainGUI;
    }


    @FXML
    void onClickSendMessage(ActionEvent event) {


        if (toBox.getText().isEmpty() || subjectBox.getText().isEmpty() || messageField.getText().isEmpty()) {
            errorMessage.setVisible(true);
            errorMessage.setText(StringUtils.translate("CreateMessageController.EmptyFields"));

            time(5, errorMessage);


            return;
        } else if (!toBox.getText().contains("@")) {

            errorMessage.setText(StringUtils.translate("CreateMessageController.InvalidEmail"));

            time(5, errorMessage);


        } else {


            businessLogic.sendMessage(toBox.getText(), subjectBox.getText(), messageField.getText());
            errorMessage.setText(StringUtils.translate("CreateMessageController.MessageSent"));

            toBox.setText("");
            subjectBox.setText("");
            messageField.setText("");

            time(5, errorMessage);

        }


    }

    @FXML
    void initialize() {

        sendMessageBttn.setStyle("-fx-background-color: #f85774");
        errorMessage.setVisible(false);

    }

    @Override
    public void setMainApp(MainGUI mainGUI) {

        this.mainGUI = mainGUI;

    }


    @Override
    public void changeLanguage() {

        sendMessageBttn.setText(StringUtils.translate("SendMessage"));
        errorMessage.setText(StringUtils.translate("CreateMessageController.EmptyFields"));

        subTXT.setText(StringUtils.translate("Subject"));
        msgTXT.setText(StringUtils.translate("Message"));
        toTXT.setText(StringUtils.translate("CreateMessageController.to"));


    }

    @Override
    public void showHide() {

    }

    @Override
    public void time(int s, Label msg) {

        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(s * 1000L);
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
    public TableView<Alerts> getTblAlerts() {
        return null;
    }

    @Override
    public void updateComboBoxes(String from) {

    }
}
