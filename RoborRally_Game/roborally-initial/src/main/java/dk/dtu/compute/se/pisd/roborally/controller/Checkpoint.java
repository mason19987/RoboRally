package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;

public class Checkpoint extends FieldAction {
    private int checkpointNumber;

    public Checkpoint(int checkpointNumber) {
        this.checkpointNumber = checkpointNumber;
    }

    public int getCheckpointNumber() {
        return checkpointNumber;
    }

    @Override
    public boolean doAction(GameController gameController, Space space) {
        if (space.getActions().size() > 0) {
            Player player = space.getPlayer();
            player.setCheckPoint(checkpointNumber);

            Boolean gameFinished = player.getCheckPoints().contains(1)
                    && player.getCheckPoints().contains(2)
                    && player.getCheckPoints().indexOf(1) < player.getCheckPoints().indexOf(2);

            if (gameFinished) {
                gameController.Winner_Massage(space);
                gameController.board.gameOver = true;
                return true;
            }
        }
        return false;
    }
}