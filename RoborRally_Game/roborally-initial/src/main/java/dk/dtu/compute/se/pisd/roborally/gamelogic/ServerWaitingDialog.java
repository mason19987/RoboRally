package dk.dtu.compute.se.pisd.roborally.gamelogic;

import javafx.application.Platform;
import javafx.scene.control.*;

public class ServerWaitingDialog {
    private Dialog<ButtonType> dialog;
    private TextArea messageTextArea;
    private Button okButton; // This is the OK button

    public ServerWaitingDialog() {
        this.dialog = new Dialog<>();
        this.dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Get a reference to the OK button and disable it by default
        this.okButton = (Button) this.dialog.getDialogPane().lookupButton(ButtonType.OK);
        this.okButton.setDisable(true);

        this.messageTextArea = new TextArea();
        this.messageTextArea.setEditable(false); // disable editing
        this.dialog.getDialogPane().setContent(this.messageTextArea);
    }

    public void open(String message) {
        Platform.runLater(() -> this.messageTextArea.setText(message));
        if (!this.dialog.isShowing()) {
            this.dialog.showAndWait();
            if (this.dialog.getResult() == ButtonType.OK) {
                this.close();
            }
            
        }
    }

    public void close() {
        if (this.dialog.isShowing()) {
            this.dialog.close();
        }
    }

    public void setMessage(String message) {
        Platform.runLater(() -> this.messageTextArea.setText(message));
    }

    // Method to enable the OK button
    public void enableOkButton() {
        Platform.runLater(() -> this.okButton.setDisable(false));
    }

    // Method to disable the OK button
    public void disableOkButton() {
        Platform.runLater(() -> this.okButton.setDisable(true));
    }
}
