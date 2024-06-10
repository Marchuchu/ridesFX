package eus.ehu.ridesfx.uicontrollers;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

import eus.ehu.ridesfx.businessLogic.BlFacade;
import eus.ehu.ridesfx.domain.Alerts;
import eus.ehu.ridesfx.domain.Message;
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

public class MessageController implements Controller {

    BlFacade businessLogic;
    MainGUI mainGUI;
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private AnchorPane mainWrapper;
    @FXML
    private Text errorM;
    @FXML
    private Label errorMessage;
    @FXML
    private TextArea messageTXT;
    @FXML
    private TableView<Message> messageTable;
    @FXML
    private TableColumn<Message, String> From;
    @FXML
    private TableColumn<Message, String> Message;
    @FXML
    private TableColumn<Message, String> Subject;
    @FXML
    private Button seeMessageBttn;
    @FXML
    private Button sendNewMessageBttn;

    public MessageController(BlFacade bl) {
        this.businessLogic = bl;
    }


    public MessageController(BlFacade bl, MainGUI mainGUI) {
        this.businessLogic = bl;
        this.mainGUI = mainGUI;
    }

    @FXML
    void onClickSeeMessage(ActionEvent event) {

        Message message = messageTable.getSelectionModel().getSelectedItem();

        if (message == null) {
            errorMessage.setText(StringUtils.translate("MessageController.selectMessage"));

            time(5,errorMessage);
//
//            Thread thread = new Thread(() -> {
//                try {
//                    Thread.sleep(5000);
//                    Platform.runLater(() -> errorMessage.setVisible(false));
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            });
//            thread.start();


            errorMessage.setVisible(true);
            return;
        } else {

            messageTXT.setText(message.getMessage());

        }

    }

    @FXML
    void onClickSendNewMessage(ActionEvent event) {

        mainGUI.showScene("Send Message");

    }

    @FXML
    void initialize() {

        seeMessageBttn.setStyle("-fx-background-color: #f85774");
        sendNewMessageBttn.setStyle("-fx-background-color: #f85774");
        errorMessage.setVisible(false);


    }

    @Override
    public void setMainApp(MainGUI mainGUI) {

        this.mainGUI = mainGUI;

    }

    @Override
    public void changeLanguage() {

        seeMessageBttn.setText(StringUtils.translate("SeeMessage"));
        sendNewMessageBttn.setText(StringUtils.translate("SendNewMessage"));
        errorMessage.setText(StringUtils.translate("MessageController.selectMessage"));
        From.setText(StringUtils.translate("From"));
        Message.setText(StringUtils.translate("Message"));
        Subject.setText(StringUtils.translate("Subject"));

    }

    @Override
    public void showHide() {

    }

    @Override    public void time(int s, Label msg) {

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
    public TableView<Alerts> getTblAlerts() {
        return null;
    }

    @Override
    public void updateComboBoxes(String from) {

    }
}
