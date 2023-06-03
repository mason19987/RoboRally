package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;

public class Checkpoints extends FieldAction {


    private final int orderNo;

    public Checkpoints(int orderNo) {
        this.orderNo = orderNo;
    }

    /**
     * Executes the field action for a given space. In order to be able to do
     * that the GameController associated with the game is passed to this method.
     *
     * @param gameController the gameController of the respective game
     * @param space          the space this action should be executed for
     * @return whether the action was successfully executed
     */
    //@Override
   /* public boolean doAction(GameController gameController, Space space) {
        // needs to be implemented


        return false;
    }
}*/
    @Override
    public boolean doAction(GameController gameController, Space space) {
        Board board = space.board; // Get the board associated with the space
        if (board != null && gameController.board == board) {
            Player player = space.getPlayer(); // Get the player associated with the space
            if (player != null) {
                // Perform the specific action for the checkpoint based on its order number
                switch (orderNo) {
                    case 1:
                        // Perform action for checkpoint 1
                        // ...
                        break;
                    case 2:
                        // Perform action for checkpoint 2
                        // ...
                        break;
                    // Add more cases for additional checkpoints as needed
                    default:
                        // Unknown checkpoint order number
                        break;
                }
                return true; // Action executed successfully
            }
        }
        return false; // Action not executed
    }

}