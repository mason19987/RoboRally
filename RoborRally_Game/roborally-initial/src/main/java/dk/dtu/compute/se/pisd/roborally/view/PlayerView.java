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
package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.clients.MultiplayerClient;
import dk.dtu.compute.se.pisd.roborally.controller.AppController;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.gamelogic.ServerMultiplayerLogic;
import dk.dtu.compute.se.pisd.roborally.model.*;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 * @author Mohamad Anwar Meri, s215713@dtu.dk
 *
 */
public class PlayerView extends Tab implements ViewObserver {

    private final Player player;

    private final VBox top;

    private final Label programLabel;
    private final GridPane programPane;
    private final Label cardsLabel;
    private final GridPane cardsPane;

    private final CardFieldView[] programCardViews;
    private final CardFieldView[] cardViews;

    private final VBox buttonPanel;

    private final Button finishButton;
    private final Button executeButton;
    private final Button stepButton;

    private final VBox playerInteractionPanel;

    private final GameController gameController;

    public PlayerView(@NotNull GameController gameController, @NotNull Player player) {
        super(player.getName());
        int playerId = AppController.playerId;
        var playTurnId = ServerMultiplayerLogic.currentPlayerTurn;
        int tabPlayerId = Integer.parseInt(player.getName().split("-")[0]);
        boolean disableItems = playerId != tabPlayerId;

        this.setStyle("-fx-text-base-color: " + player.getColor() + ";");

        top = new VBox();
        this.setContent(top);

        this.gameController = gameController;
        this.player = player;

        if (tabPlayerId != playTurnId) {
            programLabel = new Label("Program (Wait for your turn)");
        } else {
            programLabel = new Label("Program");
        }

        programPane = new GridPane();
        programPane.setVgap(2.0);
        programPane.setHgap(2.0);
        programCardViews = new CardFieldView[Player.NO_REGISTERS];
        for (int i = 0; i < Player.NO_REGISTERS; i++) {
            CommandCardField cardField = player.getProgramField(i);
            if (cardField != null) {
                programCardViews[i] = new CardFieldView(gameController, cardField);
                programPane.add(programCardViews[i], i, 0);
            }
        }

        // XXX the following buttons should actually not be on the tabs of the
        // individual
        // players, but on the PlayersView (view for all players). This should be
        // refactored.

        finishButton = new Button("Finish Programming");
        finishButton.setOnAction(e -> gameController.finishProgrammingPhase());
        finishButton.setDisable(disableItems);

        executeButton = new Button("Execute Program");
        executeButton.setOnAction(e -> gameController.executePrograms());
        executeButton.setDisable(disableItems);

        stepButton = new Button("Execute Current Register");
        stepButton.setOnAction(e -> gameController.executeStep());
        stepButton.setDisable(disableItems);

        buttonPanel = new VBox(finishButton, executeButton, stepButton);
        buttonPanel.setAlignment(Pos.CENTER_LEFT);
        buttonPanel.setSpacing(3.0);
        // programPane.add(buttonPanel, Player.NO_REGISTERS, 0); done in update now

        playerInteractionPanel = new VBox();
        playerInteractionPanel.setAlignment(Pos.CENTER_LEFT);
        playerInteractionPanel.setSpacing(3.0);

        cardsLabel = new Label("Command Cards");
        cardsPane = new GridPane();
        cardsPane.setVgap(2.0);
        cardsPane.setHgap(2.0);
        cardViews = new CardFieldView[Player.NO_CARDS];
        for (int i = 0; i < Player.NO_CARDS; i++) {
            CommandCardField cardField = player.getCardField(i);
            if (cardField != null) {
                cardViews[i] = new CardFieldView(gameController, cardField);
                cardsPane.add(cardViews[i], i, 0);
            }
        }

        top.getChildren().add(programLabel);
        top.getChildren().add(programPane);
        top.getChildren().add(cardsLabel);
        top.getChildren().add(cardsPane);

        if (player.board != null) {
            player.board.attach(this);
            update(player.board);
        }
    }

    @Override
    public void updateView(Subject subject) {

        int playerId = AppController.playerId;
        var playTurnId = ServerMultiplayerLogic.currentPlayerTurn;
        boolean disableItems = playerId != playTurnId;

        if (subject == player.board) {
            for (int i = 0; i < Player.NO_REGISTERS; i++) {
                CardFieldView cardFieldView = programCardViews[i];
                if (cardFieldView != null) {
                    if (player.board.getPhase() == Phase.PROGRAMMING) {
                        cardFieldView.setBackground(CardFieldView.BG_DEFAULT);
                    } else {
                        if (i < player.board.getStep()) {
                            cardFieldView.setBackground(CardFieldView.BG_DONE);
                        } else if (i == player.board.getStep()) {
                            if (player.board.getCurrentPlayer() == player) {
                                cardFieldView.setBackground(CardFieldView.BG_ACTIVE);
                            } else if (player.board.getPlayerNumber(player.board.getCurrentPlayer()) > player.board
                                    .getPlayerNumber(player)) {
                                cardFieldView.setBackground(CardFieldView.BG_DONE);
                            } else {
                                cardFieldView.setBackground(CardFieldView.BG_DEFAULT);
                            }
                        } else {
                            cardFieldView.setBackground(CardFieldView.BG_DEFAULT);
                        }
                    }
                }
            }

            if (player.board.getPhase() != Phase.PLAYER_INTERACTION) {
                if (!programPane.getChildren().contains(buttonPanel)) {
                    programPane.getChildren().remove(playerInteractionPanel);
                    programPane.add(buttonPanel, Player.NO_REGISTERS, 0);
                }
                if (disableItems) {
                    finishButton.setDisable(true);
                    executeButton.setDisable(true);
                    stepButton.setDisable(true);
                } else {
                    switch (player.board.getPhase()) {
                        case INITIALISATION:
                            finishButton.setDisable(true);
                            // XXX just to make sure that there is a way for the player to get
                            // from the initialization phase to the programming phase somehow!
                            executeButton.setDisable(false);
                            stepButton.setDisable(true);
                            break;

                        case PROGRAMMING:
                            finishButton.setDisable(false);
                            executeButton.setDisable(true);
                            stepButton.setDisable(true);
                            break;

                        case ACTIVATION:
                            finishButton.setDisable(true);
                            executeButton.setDisable(false);
                            stepButton.setDisable(false);
                            break;

                        default:
                            finishButton.setDisable(true);
                            executeButton.setDisable(true);
                            stepButton.setDisable(true);
                    }
                }

            } else {
                if (!programPane.getChildren().contains(playerInteractionPanel)) {
                    programPane.getChildren().remove(buttonPanel);
                    programPane.add(playerInteractionPanel, Player.NO_REGISTERS, 0);
                }
                playerInteractionPanel.getChildren().clear();

                if (player.board.getCurrentPlayer() == player) {

                    // TODO Assignment P3: these buttons should be shown only when there is
                    // an interactive command card, and the buttons should represent
                    // the player's choices of the interactive command card. The
                    // following is just a mockup showing two options

                    /**
                     * The reason we have labeled the below code with the comments is because we
                     * don't have to show them.
                     * We don't have to show them because the player instead of showing the fixed
                     * options "option 1 and option 2"
                     * should we instead show the options that are possible for this player.
                     */

                    // TODO Assignment P3
                    /**
                     * we program a loop in this blow code, that shows the real options of the
                     * robot's current interactive command card.
                     * Firstly access the current card, then access the list with the command
                     * options
                     * and program a loop that adds a button on the current panel to each option.
                     * 
                     * @author Mohamad Anwar Meri, s215713@dtu.dk
                     */

                    CommandCardField register = player.getProgramField(player.board.getStep());// Finding out what the
                                                                                               // player's cards are
                                                                                               // "current cards"
                    // and we need to find out what register we are playing right now, so that's the
                    // ProgramField the player has to play inside.
                    if (register != null) { // Checks if field is not null
                        CommandCard card = register.getCard(); // If field is not null, then we ask field which card you
                                                               // have, so give me a command card.
                        if (card != null) { // Checks if card is not null
                            for (Command option : card.command.getOptions()) {
                                Button optionButton = new Button(option.displayName);// Create a button
                                optionButton.setOnAction(e -> gameController.executeCommandOptionsAndContinue(option));// If
                                                                                                                       // the
                                                                                                                       // button
                                                                                                                       // is
                                                                                                                       // pressed,
                                                                                                                       // we
                                                                                                                       // call
                                                                                                                       // executeCommandOptionsAndContinue
                                                                                                                       // with
                                                                                                                       // the
                                                                                                                       // option
                                                                                                                       // that
                                                                                                                       // the
                                                                                                                       // button
                                                                                                                       // corresponds
                                                                                                                       // to.
                                optionButton.setDisable(false);
                                playerInteractionPanel.getChildren().add(optionButton);

                            }
                        }
                    }
                }

            }
        }
    }
}
