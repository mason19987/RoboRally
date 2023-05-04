package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Space;

public class Checkpoints extends FieldAction{


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
    @Override
    public boolean doAction(GameController gameController, Space space) {
        // needs to be implemented
        return false;
    }
}
