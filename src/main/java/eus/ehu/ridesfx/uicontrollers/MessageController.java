package eus.ehu.ridesfx.uicontrollers;

import eus.ehu.ridesfx.businessLogic.BlFacade;
import eus.ehu.ridesfx.domain.*;
import eus.ehu.ridesfx.domain.Alert;
import eus.ehu.ridesfx.ui.MainGUI;
import eus.ehu.ridesfx.utils.StringUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class MessageController implements Controller {

    BlFacade businessLogic;
    MainGUI mainGUI;

    @FXML
    private Label errorMessage;
    @FXML
    private TextArea messageTXT;
    @FXML
    private TableColumn<Message, Integer> idColumn;
    @FXML
    private TableView<Message> messageTable;
    @FXML
    private TableColumn<Message, String> fromColumn;
    @FXML
    private TableColumn<Message, String> subjectColumn;
    @FXML
    private TableColumn<Message, String> toColumn;
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
    void initialize() {

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        fromColumn.setCellValueFactory(new PropertyValueFactory<>("from"));
        subjectColumn.setCellValueFactory(new PropertyValueFactory<>("subject"));
        toColumn.setCellValueFactory(new PropertyValueFactory<>("to"));

        seeMessageBttn.setStyle("-fx-background-color: #f85774");
        sendNewMessageBttn.setStyle("-fx-background-color: #f85774");
        errorMessage.setVisible(false);

        messageTXT.setEditable(false);
        messageTXT.setText(" ");

        loadMessages();

    }

    @Override
    public void loadMessages() {

        List<Message> messages = businessLogic.getAllMessages();
        messageTable.getItems().setAll(messages);
    }

    @Override
    public void loadMessages(User u) {

        List<Message> messages = businessLogic.getAllMessagesFromUser(u);
        messages.addAll(businessLogic.getAllMessagesToUser(u));
        messageTable.getItems().setAll(messages);

    }

    @Override
    public void setMainApp(MainGUI mainGUI) {

        this.mainGUI = mainGUI;

    }

    //Buttons methods

    @FXML
    void onClickSeeMessage(ActionEvent event) {

        mainGUI.mGUIC.getSeeMessagesBttn().setVisible(true);
        mainGUI.mGUIC.getSeeAlertsBttn().setVisible(true);
        mainGUI.mGUIC.getQueryRidesBtn().setVisible(!(businessLogic.getCurrentUser() instanceof Driver));
        mainGUI.mGUIC.getCreateRidesBtn().setVisible(!(businessLogic.getCurrentUser() instanceof Traveler));

        Message selectedMessage = messageTable.getSelectionModel().getSelectedItem();
        if (selectedMessage == null) {

            showErrorMessage("MessageController.selectMessage", errorMessage, "-fx-text-fill: #d54242;", 5);

        } else {
            messageTXT.setText(selectedMessage.getMessage());
        }
    }


    @FXML
    void onClickSendNewMessage(ActionEvent event) {

        mainGUI.showScene("Send Message");

        mainGUI.mGUIC.getSeeMessagesBttn().setVisible(true);
        mainGUI.mGUIC.getCreateRidesBtn().setVisible(false);
        mainGUI.mGUIC.getSeeAlertsBttn().setVisible(false);
        mainGUI.mGUIC.getQueryRidesBtn().setVisible(false);

        messageTXT.setText(" ");

    }

    //Auxiliar methods

    @Override
    public void changeLanguage() {

        seeMessageBttn.setText(StringUtils.translate("SeeMessage"));
        sendNewMessageBttn.setText(StringUtils.translate("SendNewMessage"));
        errorMessage.setText(StringUtils.translate("MessageController.selectMessage"));
        fromColumn.setText(StringUtils.translate("From"));
        subjectColumn.setText(StringUtils.translate("Subject"));
        toColumn.setText(StringUtils.translate("To"));

    }


    @Override
    public void time(int s, Label msg) {

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
    public void showErrorMessage(String txt, Label label, String style, int t) {

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
    public void updateComboBoxes(String from) {

    }

    @Override
    public void clearData() {

    }

    @Override
    public void showHide() {

    }

}
