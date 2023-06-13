package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;

/*public class Checkpoints extends FieldAction {


    private final int orderNo;

    public Checkpoints(int orderNo) {
        this.orderNo = orderNo;
    }*/





    public class Checkpoints extends FieldAction {

        private static int lastCheckpointNumber=1 ;
        private int checkpointNumber;


        public Checkpoints(int checkpointNumber, int lastCheckpoint) {
            Checkpoints.lastCheckpointNumber = lastCheckpoint;


        }

        public int getCheckpointNumber() {
            return checkpointNumber;
        }


        public static void setlastCheckpointNumber(int LastCheckpointNumber) {
            Checkpoints.lastCheckpointNumber = LastCheckpointNumber;
        }

        @Override
        public boolean doAction(GameController gameController, Space space) {

            if (space.getActions().size() > 0) {
                Checkpoints checkpointnumber = (Checkpoints) space.getActions().get(0);
                Player player = space.getPlayer();

                // Checks if the player step on the checkpoint in correct order.
                if (player != null && player.checkPoints + 1 == checkpointnumber.checkpointNumber) {
                    player.checkPoints++;

                    if (player.checkPoints == lastCheckpointNumber) {
                        //Show a massage when a player won the game
                        gameController.Winner_Massage(space);
                        lastCheckpointNumber = 0; // Needs because the static variable is never resat
                        gameController.board.gameOver = true;

                    }
                    return true;
                }
            }
            return false;
        }
    }