package eus.ehu.ridesfx.uicontrollers;

import java.net.URL;
import java.util.ResourceBundle;

import eus.ehu.ridesfx.businessLogic.BlFacade;
import eus.ehu.ridesfx.ui.MainGUI;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class CreateMessageController implements Controller {

    BlFacade businessLogic;
    MainGUI mainGUI;
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
            errorMessage.setText(translate("CreateMessageController.EmptyFields"));

            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(5000);
                    Platform.runLater(() -> errorMessage.setVisible(false));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            thread.start();

            return;
        } else if (!toBox.getText().contains("@")) {

            errorMessage.setText(translate("CreateMessageController.InvalidEmail"));

            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(5000);
                    Platform.runLater(() -> errorMessage.setVisible(false));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            thread.start();

        } else {

            //TODO: el ID va a ser el ultimo elemento +1 , no el 1
            businessLogic.sendMessage(1, toBox.getText(), subjectBox.getText(), messageField.getText());
            errorMessage.setText(translate("CreateMessageController.MessageSent"));

            toBox.setText("");
            subjectBox.setText("");
            messageField.setText("");

            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(5000);
                    Platform.runLater(() -> errorMessage.setVisible(false));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            thread.start();

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

    String translate(String txt) {
        return ResourceBundle.getBundle("Etiquetas").getString(txt);
    }

    @Override
    public void changeLanguage(ResourceBundle resources) {

        sendMessageBttn.setText(resources.getString("SendMessage"));
        errorMessage.setText(resources.getString("CreateMessageController.EmptyFields"));

    }
}