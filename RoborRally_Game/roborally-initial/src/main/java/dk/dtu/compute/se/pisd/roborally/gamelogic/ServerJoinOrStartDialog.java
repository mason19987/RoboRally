package dk.dtu.compute.se.pisd.roborally.gamelogic;

import javafx.scene.control.ChoiceDialog;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ServerJoinOrStartDialog {

    public enum DialogOption {
        START("Start"),
        JOIN("Join");

        private final String label;

        DialogOption(String label) {
            this.label = label;
        }

        public String getLabel() {
            return this.label;
        }

        @Override
        public String toString() {
            return this.label;
        }
    }

    public Optional<DialogOption> displayStartOrJoinDialog() {
        List<DialogOption> Dialog_Option = Arrays.asList(DialogOption.values());
        ChoiceDialog<DialogOption> choiceDialog = new ChoiceDialog<>(Dialog_Option.get(0), Dialog_Option);
        choiceDialog.setTitle("Join or start a game");
        choiceDialog.setHeaderText("please choose an option");
        Optional<DialogOption> result = choiceDialog.showAndWait();

        return result;
    }
}
