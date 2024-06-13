package eus.ehu.ridesfx.uicontrollers;

import eus.ehu.ridesfx.businessLogic.BlFacade;
import eus.ehu.ridesfx.domain.Alert;
import eus.ehu.ridesfx.domain.User;
import eus.ehu.ridesfx.ui.MainGUI;
import eus.ehu.ridesfx.utils.StringUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

public class CreateMessageController implements Controller {

    BlFacade businessLogic;
    MainGUI mainGUI;

    @FXML
    private Label errorMessage;
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

    @Override
    public void setMainApp(MainGUI mainGUI) {

        this.mainGUI = mainGUI;

    }

    @FXML
    void initialize() {

        sendMessageBttn.setStyle("-fx-background-color: #f85774");
        errorMessage.setVisible(false);

        toBox.setText("");
        subjectBox.setText("");
        messageField.setText("");

    }

    //Buttons methods

    @FXML
    void onClickSendMessage(ActionEvent event) {

        if (toBox.getText().isEmpty() || subjectBox.getText().isEmpty() || messageField.getText().isEmpty()) {

            showErrorMessage("CreateMessageController.EmptyFields", errorMessage, "-fx-text-fill: #d54242;", 5);

        } else if(businessLogic.getUserByEmail(toBox.getText()) == null){

            showErrorMessage("CreateMessageController.UserDoesntExist", errorMessage, "-fx-text-fill: #d54242;", 5);


        }else if (!toBox.getText().contains("@")) {

            showErrorMessage("CreateMessageController.InvalidEmail", errorMessage, "-fx-text-fill: #d54242;", 5);

        } else {

            businessLogic.sendMessage(businessLogic.getCurrentUser().getEmail(), toBox.getText(), subjectBox.getText(), messageField.getText());
            showErrorMessage("CreateMessageController.MessageSent", errorMessage, "-fx-text-fill: #188a2e", 5);

            toBox.setText("");
            subjectBox.setText("");
            messageField.setText("");

        }

    }

    //Other methods

    @Override
    public void changeLanguage() {

        sendMessageBttn.setText(StringUtils.translate("SendMessage"));
        errorMessage.setText(StringUtils.translate("CreateMessageController.EmptyFields"));

        subTXT.setText(StringUtils.translate("Subject"));
        msgTXT.setText(StringUtils.translate("Message"));
        toTXT.setText(StringUtils.translate("CreateMessageController.to"));

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
    public void showErrorMessage(String txt, Label label, String style, int t){

        label.setText(StringUtils.translate(txt));
        label.setStyle(style);
        label.setVisible(true);
        time(t, label);

    }

    //Unused methods

    @Override
    public void getAlerts(User t) {

    }

    @Override
    public void getAllAlerts() {

    }

    @Override
    public TableView<Alert> getTblAlerts() {
        return null;
    }

    @Override
    public void showHide() {

    }

    @Override
    public void updateComboBoxes(String from) {

    }

    @Override
    public void clearData() {

    }

    @Override
    public void loadMessages() {

    }

    @Override
    public void loadMessages(User u) {

    }
}
