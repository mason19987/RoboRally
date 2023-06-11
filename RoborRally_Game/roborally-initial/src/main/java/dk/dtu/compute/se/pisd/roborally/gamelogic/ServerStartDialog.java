package dk.dtu.compute.se.pisd.roborally.gamelogic;

import javafx.scene.control.ChoiceDialog;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ServerStartDialog {

    public enum DialogOption {
        
        O2("2"),
        O3("3"),
        O4("4"),
        O5("5"),
        O6("6");

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

        public int getNumberOfPlayers() {
            return Integer.parseInt(this.label);
        }
    }

    public Optional<DialogOption> displayStartDialog() {
        List<DialogOption> Dialog_Option = Arrays.asList(DialogOption.values());
        ChoiceDialog<DialogOption> choiceDialog = new ChoiceDialog<>(Dialog_Option.get(0), Dialog_Option);
        choiceDialog.setTitle("start a game");
        choiceDialog.setHeaderText("select the number of players");
        Optional<DialogOption> result = choiceDialog.showAndWait();

        return result;
    }
}
