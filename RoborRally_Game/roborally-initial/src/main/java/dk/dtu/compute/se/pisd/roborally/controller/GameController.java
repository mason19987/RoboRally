/*
 *  This file is part of the initial project provided for the
 *  course "Project in Software Development (02362)" held at
 *  DTU Compute at the Technical University of Denmark.
 *
 *  Copyright (C) 2019, 2020: Ekkart Kindler, ekki@dtu.dk
 *
 *  This software is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; version 2 of the License.
 *
 *  This project is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this project; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.*;
import javafx.scene.control.Alert;

import org.jetbrains.annotations.NotNull;


/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 * @author Mohamad Anwar Meri, s215713@dtu.dk
 * @author Shaoib Zafar Mian, s200784@dtu.dk
 * @version $Id: $Id
 */
public class GameController {

    final public Board board;

    /**
     * <p>Constructor for GameController.</p>
     *
     * @param board a {@link dk.dtu.compute.se.pisd.roborally.model.Board} object.
     */
    public GameController(@NotNull Board board) {
        this.board = board;
    }

    /**
     * This is just some dummy controller operation to make a simple move to see something
     * happening on the board. This method should eventually be deleted!
     *
     * @param space the space to which the current player should move
     */
    public void moveCurrentPlayerToSpace(@NotNull Space space) {
        // TODO Assignment V1: method should be implemented by the students:
        //   - the current player should be moved to the given space
        //     (if it is free()
        //   - and the current player should be set to the player
        //     following the current player
        //   - the counter of moves in the game should be increased by one
        //     if the player is moved


         /**
         * In this method we change the current player's turn to move
         *
         * @author Mohamad Anwar Meri, s215713@dtu.dk
         * @author Safi Meissam, s224298@dtu.dk
         */

        Player currentPlayer = board.getCurrentPlayer(); // Get the current player from boar class
        if (currentPlayer != null) {  // if currentPlayer not null
            if (space.getPlayer() == null) { //if the place of the player is null
                currentPlayer.setSpace(space); // Move the player to space.
                int currentPlayerNumber = board.getPlayerNumber(currentPlayer);
                Player nextplayer = board.getPlayer((currentPlayerNumber + 1) % board.getPlayersNumber());
                board.setCurrentPlayer(nextplayer);

                board.setCount(board.getCount() + 1);

            }
        }
    }



    // XXX: V2
    public void startProgrammingPhase() {
        board.setPhase(Phase.PROGRAMMING);
        board.setCurrentPlayer(board.getPlayer(0));
        board.setStep(0);

        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player player = board.getPlayer(i);
            if (player != null) {
                for (int j = 0; j < Player.NO_REGISTERS; j++) {
                    CommandCardField field = player.getProgramField(j);
                    field.setCard(null);
                    field.setVisible(true);
                }
                for (int j = 0; j < Player.NO_CARDS; j++) {
                    CommandCardField field = player.getCardField(j);
                    field.setCard(generateRandomCommandCard());
                    field.setVisible(true);


                }

            }
        }
    }


    // XXX: V2
    private CommandCard generateRandomCommandCard() {
        Command[] commands = Command.values();
        int random = (int) (Math.random() * commands.length);
        return new CommandCard(commands[random]);
    }

    // XXX: V2
    public void finishProgrammingPhase() {
        makeProgramFieldsInvisible();
        makeProgramFieldsVisible(0);
        board.setPhase(Phase.ACTIVATION);
        board.setCurrentPlayer(board.getPlayer(0));
        board.setStep(0);
    }

    // XXX: V2
    private void makeProgramFieldsVisible(int register) {
        if (register >= 0 && register < Player.NO_REGISTERS) {
            for (int i = 0; i < board.getPlayersNumber(); i++) {
                Player player = board.getPlayer(i);
                CommandCardField field = player.getProgramField(register);
                field.setVisible(true);
            }
        }
    }

    // XXX: V2
    private void makeProgramFieldsInvisible() {
        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player player = board.getPlayer(i);
            for (int j = 0; j < Player.NO_REGISTERS; j++) {
                CommandCardField field = player.getProgramField(j);
                field.setVisible(false);
            }
        }
    }

    // XXX: V2
    public void executePrograms() {
        board.setStepMode(false);
        continuePrograms();
    }

    // XXX: V2
    public void executeStep() {
        board.setStepMode(true);
        continuePrograms();
    }

    // XXX: V2
    private void continuePrograms() {
        do {
            executeNextStep();
        } while (board.getPhase() == Phase.ACTIVATION && !board.isStepMode());

        AppController.smpl.turnCompleted();
    }

    // XXX: V2
    private void executeNextStep() {
        Player currentPlayer = board.getCurrentPlayer();
        if (board.getPhase() == Phase.ACTIVATION && currentPlayer != null) {
            int step = board.getStep();
            if (step >= 0 && step < Player.NO_REGISTERS) {
                CommandCard card = currentPlayer.getProgramField(step).getCard();
                if (card != null) {
                    Command command = card.command;

                    // TODO Assignment P3
                    /**
                     *
                     * @author Mohamad Anwar Meri
                     */
                    if (command.isInteractive()){
                        board.setPhase(Phase.PLAYER_INTERACTION);
                        return;
                    }

                    executeCommand(currentPlayer, command);
                }
                int nextPlayerNumber = board.getPlayerNumber(currentPlayer) + 1;
                if (nextPlayerNumber < board.getPlayersNumber()) {
                    board.setCurrentPlayer(board.getPlayer(nextPlayerNumber));
                } else {
                    for (int i = 0; i < board.getPlayersNumber(); i++){
                        Player player = board.getPlayer(i);
                        if (player != null && player.getSpace() != null && player.getSpace().getActions() !=null) {
                            for (FieldAction action : player.getSpace().getActions()) {
                                action.doAction(this, player.getSpace());
                            }
                        }
                    }
                    step++;
                    if (step < Player.NO_REGISTERS) {
                        makeProgramFieldsVisible(step);
                        board.setStep(step);
                        board.setCurrentPlayer(board.getPlayer(0));
                    } else {
                        startProgrammingPhase();
                    }
                }
            } else {
                // this should not happen
                assert false;
            }
        } else {
            // this should not happen
            assert false;
        }
    }


    // TODO Assignment P3
    /**
     *
     * @param options
     * How to implement the control if the player has chosen which option the player will execute,
     * so that the player can continue
     * @author Mohamad Anwar Meri, s215713@dtu.dk
     */

    public void executeCommandOptionsAndContinue(Command options) { //The options the player has chosen
        Player currentPlayer = board.getCurrentPlayer(); //Finds out what is the current player
        if (currentPlayer != null && // that means it must do something only if the current player is not null
                board.getPhase() == Phase.PLAYER_INTERACTION && //Finds out if the player's Phase is an active way where it makes sense to call the "executeOption" method.
                options != null) { //checks if the options are not null
            board.setPhase(Phase.ACTIVATION);//If we don't set it here, the player will be in the interaction phase, and then you can't continue playing.
            executeCommand(currentPlayer, options); //Execute the command

            int nextPlayerNumber = board.getPlayerNumber(currentPlayer) + 1;
            if (nextPlayerNumber < board.getPlayersNumber()) {
                board.setCurrentPlayer(board.getPlayer(nextPlayerNumber));

            } else {
                int step = board.getStep() + 1;
                if (step < Player.NO_REGISTERS) { // if this step is not suitable for the register we have, we have to make sure it becomes the new step.
                    makeProgramFieldsVisible(step);
                    board.setStep(step);// It's the new step
                    board.setCurrentPlayer(board.getPlayer(0)); //We start with player 1 again.

                } else {
                    startProgrammingPhase(); // we insert the program in the "ProgrammingPhase" if we have finished executing.
                }
            }
        } else {
            assert false;
        }
        if (!board.isStepMode()){// we make sure here that the execution of the games continues if the game is not in step mode
            // and the program was not finished.
            continuePrograms();
        }

        /**
         * @param Phase.PLAYER_INTERACTION
         * Finds out if the player's Phase is an active way where it makes sense to call the "executeOption" method.
         * @param Phase.ACTIVATION
         * If we don't set it here, the player will be in the interaction phase,
         * and then you can't continue playing.
         * @author Mohamad Anwar Meri, s215713@dtu.dk
         */
    }



    // XXX: V2
    private void executeCommand(@NotNull Player player, Command command) {
        if (player != null && player.board == board && command != null) {
            // XXX This is a very simplistic way of dealing with some basic cards and
            //     their execution. This should eventually be done in a more elegant way
            //     (this concerns the way cards are modelled as well as the way they are executed).

            switch (command) {
                case FORWARD:
                    this.moveForward(player);
                    break;
                case RIGHT:
                    this.turnRight(player);
                    break;
                case LEFT:
                    this.turnLeft(player);
                    break;
                case FAST_FORWARD:
                    this.fastForward(player);
                    break;
                case REARWARDS:
                    this.rearWards(player);
                    break;
                case THREE_FORWARD:
                    this.threeForward(player);
                    break;
                case HALF_ROTATION:
                    this.half_Rotation(player);
                    break;
                default:
                    // DO NOTHING (for now)
            }
        }
    }

    /**
     * This method make the robots push each other forward.
     * When the player lands on a field, where there is already a robot,
     * the robot will be pushed on forward.
     *
     * @param player a {@link dk.dtu.compute.se.pisd.roborally.model.Player} object.
     * @author Mohamad Anwar Meri, s215713@dtu.dk
     */

    // TODO Assignment V2
    public void moveForward(@NotNull Player player) {
        if (player.board == board) {
            Space space = player.getSpace();
            Heading heading = player.getHeading();

            Space target = board.getNeighbour(space, heading);
            if (target != null) {
                try {
                    moveToSpace(player, target, heading);
                } catch (ImpossibleMoveException e) {
                    // we don't do anything here  for now; we just catch the
                    // exception so that we do no pass it on to the caller
                    // (which would be very bad style).
                }
            }
        }
    }


    /**
     * As we can see at this method, we call the moveForward function twice.
     * That means the player should move two times forward.
     *
     * @param player a {@link dk.dtu.compute.se.pisd.roborally.model.Player} object.
     * @author Mohamad Anwar Meri, s215713@dtu.dk
     */
    // TODO Assignment V2
    public void fastForward(@NotNull Player player) {
        moveForward(player);
        moveForward(player);
    }

    /**
     * At this method we turn the player to the specific direction,
     * that the player choose from programming cards.
     * At this method the player turns to the right
     *
     * @param player a {@link dk.dtu.compute.se.pisd.roborally.model.Player} object.
     * @author Mohamad Anwar Meri, s215713@dtu.dk
     */
    // TODO Assignment V2
    public void turnRight(@NotNull Player player) {
        Heading playerHeading = player.getHeading();
        if (playerHeading != null) {
            Heading newPlayerHeading = playerHeading.next();
            player.setHeading(newPlayerHeading);
        }
    }

    /**
     * At this method we turn the player to the specific direction,
     * that the player choose from programming cards.
     * At this method the player turns to the left
     *
     * @param player a {@link dk.dtu.compute.se.pisd.roborally.model.Player} object.
     * @author Mohamad Anwar Meri, s215713@dtu.dk
     */

    // TODO Assignment V2
    public void turnLeft(@NotNull Player player) {
        Heading playerHeading = player.getHeading();
        if (playerHeading != null){
            Heading newPlayerHeading1 = playerHeading.prev();
            player.setHeading(newPlayerHeading1);
        }
    }


    /**
     * This method move the player a field towards the back
     *
     * @param player a {@link dk.dtu.compute.se.pisd.roborally.model.Player} object
     * @author Mohamad Anwar Meri, s215713@dtu.dk
     */
    public void rearWards(@NotNull Player player){
        Space currentSpace = player.getSpace();
        if (currentSpace != null && player.board == currentSpace.board){
            Space target = board.getBack(currentSpace, player.getHeading());
            if (target != null && target.getPlayer() == null) {//if target not null and if there is no player in that field yet
                player.setSpace(target); //here we make player.setSpace move to target

            }
        }
    }

    /**
     * This method calls the moveForward function thrice.
     * The player should move three times forward.
     *
     * @param player a {@link dk.dtu.compute.se.pisd.roborally.model.Player} object.
     * @author Mohamad Anwar Meri, s215713@dtu.dk
     */
    public void threeForward(@NotNull Player player){
        moveForward(player);
        moveForward(player);
        moveForward(player);
    }

    /**
     * This method turns the player 180 degrees
     * @param player a {@link dk.dtu.compute.se.pisd.roborally.model.Player} object.
     * @author Mohamad Anwar Meri, s215713@dtu.dk
     */
    public void half_Rotation(@NotNull Player player){
        Heading currentHeading = player.getHeading();
        if (currentHeading != null) {
            Heading newHeading = currentHeading.round();
            player.setHeading(newHeading);
        }
    }

    public boolean moveCards(@NotNull CommandCardField source, @NotNull CommandCardField target) {
        CommandCard sourceCard = source.getCard();
        CommandCard targetCard = target.getCard();
        if (sourceCard != null && targetCard == null) {
            target.setCard(sourceCard);
            source.setCard(null);
            return true;
        } else {
            return false;
        }
    }


    void moveToSpace(@NotNull Player player, @NotNull Space space, @NotNull Heading heading) throws ImpossibleMoveException {
        assert board.getNeighbour(player.getSpace(), heading) == space; // make sure the move to here is possible in principle
        Player other = space.getPlayer();
        if(!space.getWall().isEmpty()) {
            for (int i = 0; i < space.getWall().size(); i++) {
                if (space.getWall().get(i).next().next() == heading) {
                    throw new ImpossibleMoveException(player, space, heading);

                }
            }
        }

        else if (!player.getSpace().getWall().isEmpty()) {
            for (int i = 0; i < player.getSpace().getWall().size(); i++) {
                if (player.getSpace().getWall().get(i) == heading) {
                    throw new ImpossibleMoveException(player, space, heading);
                }
            }
        }
        if (other !=null){
            Space target = board.getNeighbour(space, heading);
            if (target != null) {
                // XXX Note that there might be additional problems with
                //     infinite recursion here (in some special cases)!
                //     We will come back to that!
                moveToSpace(other, target, heading);

                // Note that we do NOT embed the above statement in a try catch block, since
                // the thrown exception is supposed to be passed on to the caller

                assert target.getPlayer() == null : target; // make sure target is free now
            } else {
                throw new ImpossibleMoveException(player, space, heading);
            }
        }
        player.setSpace(space);
    }

    class ImpossibleMoveException extends Exception {

        private final Player player;
        private final Space space;
        private final Heading heading;

        public ImpossibleMoveException(Player player, Space space, Heading heading) {
            super("Move impossible");
            this.player = player;
            this.space = space;
            this.heading = heading;
        }
    }

    public void Winner_Massage(Space space){
        Alert winnerMassage = new Alert(Alert.AlertType.INFORMATION);
        winnerMassage.setTitle("Game Ended");
        winnerMassage.setHeaderText("The game has ended. " + " The winner is: " + space.getPlayer().getName());
        winnerMassage.showAndWait();

    }


}

