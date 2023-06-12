package dk.dtu.compute.se.pisd.roborally.gamelogic;

import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import java.util.Optional;

public class ServerJoinDialog {
    private Dialog<ButtonType> dialog;
    private TextField ipField;

    public ServerJoinDialog() {
        dialog = new Dialog<>();
        dialog.setTitle("Join a game");

        GridPane grid = new GridPane();
        ipField = new TextField();
        ipField.setText("Http://localhost:8080");
        grid.add(new Label("Server IP Address:"), 0, 0);
        grid.add(ipField, 1, 0);
        dialog.getDialogPane().setContent(grid);

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
    }

    public String getIpAddress() {
        return ipField.getText();
    }

    public Optional<ButtonType> showAndWait() {
        return dialog.showAndWait();
    }
}
