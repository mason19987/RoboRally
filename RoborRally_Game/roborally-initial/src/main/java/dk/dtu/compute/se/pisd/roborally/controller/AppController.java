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

import dk.dtu.compute.se.pisd.designpatterns.observer.Observer;
import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.RoboRally;
import dk.dtu.compute.se.pisd.roborally.clients.MultiplayerClient;
import dk.dtu.compute.se.pisd.roborally.fileaccess.LoadBoard;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.BoardTemplate;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.SpaceTemplate;
import dk.dtu.compute.se.pisd.roborally.gamelogic.ServerGameState;
import dk.dtu.compute.se.pisd.roborally.gamelogic.ServerJoinDialog;
import dk.dtu.compute.se.pisd.roborally.gamelogic.ServerJoinOrStartDialog;
import dk.dtu.compute.se.pisd.roborally.gamelogic.ServerMultiplayerLogic;
import dk.dtu.compute.se.pisd.roborally.gamelogic.ServerStartDialog;
import dk.dtu.compute.se.pisd.roborally.gamelogic.ServerWaitingDialog;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Command;
import dk.dtu.compute.se.pisd.roborally.model.CommandCard;
import dk.dtu.compute.se.pisd.roborally.model.MultiplayerPlayerModel;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.ServerModel;
import dk.dtu.compute.se.pisd.roborally.model.ServerPlayerModel;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;

import org.jetbrains.annotations.NotNull;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 * @author Mohamad Anwar Meri, s215713@dtu.dk
 * @author Shaoib Zafar Mian, s200784@dtu.dk
 */
public class AppController implements Observer {

    final private List<Integer> PLAYER_NUMBER_OPTIONS = Arrays.asList(2, 3, 4, 5, 6);
    final private List<String> PLAYER_COLORS = Arrays.asList("red", "green", "blue", "orange", "grey", "magenta");
    final private List<String> BOARD_OPTIONS = Arrays.asList("FirstBoard", "SecondBoard");

    public static RoboRally roboRally;

    public static GameController gameController;
    public static String serverIP = "http://localhost:8080";
    public static ServerMultiplayerLogic smpl;
    public static int playerId = 0;

    /**
     * Making 2 different boards
     * 
     * @param roboRally
     * @author Mohamad Anwar Meri, s215713@dtu.dk
     */

    public AppController(@NotNull RoboRally roboRally) {
        this.roboRally = roboRally;
    }

    public void newGame() {

        // multiplayerClient.join(new MultiplayerPlayerModel(0, serverIP, serverIP));

        if (gameController != null) {
            if (!stopGame()) {
                return;
            }
        }

        String hostname = "Unknown";
        String ipAddress = "Unknown";

        try {
            InetAddress addr;
            addr = InetAddress.getLocalHost();
            hostname = addr.getHostName();
            ipAddress = addr.getHostAddress();
        } catch (UnknownHostException ex) {
            System.out.println("Hostname can not be resolved");
        }

        MultiplayerClient multiplayerClient = new MultiplayerClient(serverIP);
        ServerJoinOrStartDialog serverJoin = new ServerJoinOrStartDialog();
        Optional<ServerJoinOrStartDialog.DialogOption> serverJoinResult = serverJoin.displayStartOrJoinDialog();

        boolean isServer = false;
        int playerNumber = 0;

        if (serverJoinResult.isPresent()) {
            switch (serverJoinResult.get()) {
                case START:
                    isServer = true;
                    ServerStartDialog serverStart = new ServerStartDialog();
                    Optional<ServerStartDialog.DialogOption> serverStartResult = serverStart.displayStartDialog();
                    if (serverStartResult.isPresent()) {
                        playerNumber = serverStartResult.get().getNumberOfPlayers();

                        multiplayerClient.setTotalPlayers(playerNumber);
                        multiplayerClient
                                .join(new MultiplayerPlayerModel(playerId, playerId + "-" + hostname, ipAddress));

                        ServerWaitingDialog waitingDialog = OptionStartWaitForPlayers(serverStartResult);
                        waitingDialog.open("Waiting for players to join, total players: "
                                + multiplayerClient.getPlayers().size() + " / " + playerNumber + "");
                    }
                    break;
                case JOIN:
                    isServer = false;
                    ServerJoinDialog serverJoinDialog = new ServerJoinDialog();
                    Optional<ButtonType> joinResult = serverJoinDialog.showAndWait();
                    if (joinResult.isPresent() && joinResult.get() == ButtonType.OK) {

                        serverIP = serverJoinDialog.getIpAddress();
                        multiplayerClient = new MultiplayerClient(serverIP);
                        var serverPlayers = multiplayerClient.getPlayers();
                        playerId = serverPlayers.size();
                        multiplayerClient.join(new MultiplayerPlayerModel(playerId, playerId + "-" + hostname, ipAddress));

                        ServerWaitingDialog waitingDialog = OptionJoinWaitForPlayers();
                        waitingDialog.open("Waiting for players to join, total players: "
                                + multiplayerClient.getPlayers().size() + " / " + playerNumber + "");

                        // Now you should connect to the server with the new IP address
                        // and wait for the game to start. This involves a server-side implementation.
                        // Once the game starts, you should get the board state from the server.
                    }
                    break;
            }
        }

        Board board = null;

        if (isServer) {
            ChoiceDialog<String> choiceDialog = new ChoiceDialog<>(BOARD_OPTIONS.get(0), BOARD_OPTIONS);
            choiceDialog.setTitle("Board");
            choiceDialog.setHeaderText("Choose please a board");
            Optional<String> resultBoardSelection = choiceDialog.showAndWait();
            if (resultBoardSelection.isPresent()) {
                board = LoadBoard.loadBoard(resultBoardSelection.get());
            }


            if (gameController != null) {
                if (!stopGame()) {
                    return;
                }
            }

            var serverPlayers = multiplayerClient.getPlayers();
            for (int i = 0; i < serverPlayers.size(); i++) {
                Player player = new Player(board, PLAYER_COLORS.get(i), serverPlayers.get(i).GetName());
                board.addPlayer(player);
                player.setSpace(board.getSpace(i % board.width, i));
            }

            gameController = new GameController(board);


            gameController.startProgrammingPhase();
            roboRally.createBoardView(gameController);
            multiplayerClient.start();
        }

        smpl = new ServerMultiplayerLogic(
                ipAddress,
                serverIP,
                playerId,
                roboRally,
                gameController,
                isServer);
        smpl.Start();
    }

    private ServerWaitingDialog OptionStartWaitForPlayers(Optional<ServerStartDialog.DialogOption> serverStartResult) {
        ServerWaitingDialog waitingDialog = new ServerWaitingDialog();
        Task<Void> waitingTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                var multiplayerClient = new MultiplayerClient(serverIP);
                var playerNumber = serverStartResult.get().getNumberOfPlayers();
                var actualPlayerCount = multiplayerClient.getPlayers().size();
                while (playerNumber != actualPlayerCount) {
                    actualPlayerCount = multiplayerClient.getPlayers().size();
                    System.out.println("Waiting for players to join");
                    updateMessage("Waiting for players to join, total players: "
                            + actualPlayerCount + " / " + playerNumber);

                }
                return null;
            }

            @Override
            protected void updateMessage(String message) {
                super.updateMessage(message);
                Platform.runLater(() -> waitingDialog.setMessage(message));
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                Platform.runLater(waitingDialog::close);
            }
        };
        new Thread(waitingTask).start();
        return waitingDialog;
    }

    private ServerWaitingDialog OptionJoinWaitForPlayers() {
        ServerWaitingDialog waitingDialog = new ServerWaitingDialog();
        Task<Void> waitingTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                var multiplayerClient = new MultiplayerClient(serverIP);

                var playerNumber = multiplayerClient.getTotalPlayers();
                var actualPlayerCount = multiplayerClient.getPlayers().size();
                while (playerNumber != actualPlayerCount) {
                    actualPlayerCount = multiplayerClient.getPlayers().size();
                    System.out.println("Waiting for players to join");
                    updateMessage("Waiting for players to join, total players: "
                            + actualPlayerCount + " / " + playerNumber);
                }
                return null;
            }

            @Override
            protected void updateMessage(String message) {
                super.updateMessage(message);
                Platform.runLater(() -> waitingDialog.setMessage(message));
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                Platform.runLater(waitingDialog::close);
            }
        };
        new Thread(waitingTask).start();
        return waitingDialog;
    }

    public void saveGame() throws IOException, InterruptedException {

        TextInputDialog saveGameDialog = new TextInputDialog();
        saveGameDialog.setTitle("Save Game");
        saveGameDialog.setHeaderText("Provide save-point name:");
        Optional<String> saveGameNameValue = saveGameDialog.showAndWait();

        if (saveGameNameValue.isPresent() && saveGameNameValue.get() != "") {

            HttpClient client = HttpClient.newHttpClient();
            ObjectMapper objectMapper = new ObjectMapper();

            try {

                List<ServerPlayerModel> players = List.of(
                        gameController.board.players.stream().map(player -> new ServerPlayerModel(
                                player.getName(),
                                player.getColor(),
                                player.getSpace().x,
                                player.getSpace().y,
                                player.getHeading(),
                                Arrays.stream(player.GetCards())
                                        .map(card -> new CommandCard(card.getCard().command).getName())
                                        .collect(Collectors.toList())))
                                .toArray(ServerPlayerModel[]::new));

                ServerModel serverModel = new ServerModel(
                        saveGameNameValue.get(),
                        gameController.board.boardName,
                        gameController.board.getCurrentPlayer().getName(),
                        saveGameNameValue.get(),
                        gameController.board.getPhase(),
                        gameController.board.getStep(),
                        players);

                String jsonPostData = objectMapper
                        .writeValueAsString(serverModel);
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8080/savegame"))
                        .header("Content-Type", "application/json")
                        .POST(BodyPublishers.ofString(jsonPostData))
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                int statusCode = response.statusCode();

                if (statusCode == 200 || statusCode == 201) {
                    // 200 = OK
                    // 201 = Created
                } else if (statusCode == 409) {
                    // 409 = Conflict, a file with given name already exists
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Dialog");
                    alert.setHeaderText("An Error Occurred");
                    alert.setContentText("A save-point with that name already exists.");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Dialog");
                    alert.setHeaderText("An Error Occurred");
                    alert.setContentText("Unknown error occured, try again later.");
                    alert.showAndWait();
                }

            } catch (Exception e) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("An Error Occurred");
                alert.setContentText("Unknown error occured, try again later.");
                alert.showAndWait();
            }

        }

    }

    public void loadGame() {
        HttpClient client = HttpClient.newHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/savegame"))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response != null && response.statusCode() == 200) {
                List<String> savePoints = objectMapper.readValue(response.body(), new TypeReference<List<String>>() {
                });

                ChoiceDialog<String> choiceDialog = new ChoiceDialog<>("", savePoints);
                choiceDialog.setTitle("Load game");
                choiceDialog.setHeaderText("Choose a save-point");
                Optional<String> result = choiceDialog.showAndWait();

                if (result.isPresent() && result.get() != "") {
                    String name = result.get();

                    HttpRequest request2 = HttpRequest.newBuilder()
                            .uri(URI.create("http://localhost:8080/savegame/" + name))
                            .header("Content-Type", "application/json")
                            .GET()
                            .build();
                    HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
                    if (response2 != null && response2.statusCode() == 200) {
                        ServerModel savedPoint = objectMapper.readValue(response2.body(), ServerModel.class);

                        BoardTemplate template = LoadBoard.GetBoardTemplate(savedPoint.GetBoardName());
                        Board board = new Board(template.width, template.height,
                                savedPoint.GetBoardName());
                        for (SpaceTemplate spaceTemplate : template.spaces) {
                            Space space = board.getSpace(spaceTemplate.x, spaceTemplate.y);
                            if (space != null) {
                                space.getActions().addAll(spaceTemplate.actions);
                                space.getWall().addAll(spaceTemplate.walls);
                            }
                        }
                        board.setPhase(savedPoint.GetPhase());
                        board.setStep(savedPoint.GetStep());

                        // Convert ServerPlayerModel list to Player list
                        List<Player> players = savedPoint.GetPlayers().stream().map(player -> new Player(
                                board,
                                player.GetColor(),
                                player.GetName())).collect(Collectors.toList());

                        for (int i = 0; i < players.size(); i++) {
                            Player player = players.get(i);
                            ServerPlayerModel serverPlayerModel = savedPoint.GetPlayers().get(i);
                            player.setSpace(
                                    board.getSpace(serverPlayerModel.GetPositionX(), serverPlayerModel.GetPositionY()));
                            player.setHeading(serverPlayerModel.GetHeading());

                            List<String> cardCommands = serverPlayerModel.GetCommands();
                            List<Command> commands = cardCommands.stream()
                                    .map(commandString -> Command.fromString(commandString))
                                    .collect(Collectors.toList());

                            List<CommandCard> commandCards = commands.stream()
                                    .map(command -> new CommandCard(command)).collect(Collectors.toList());

                            player.SetCards(commandCards);
                        }
                        board.setPlayers(players);

                        // set current player using name from save-point
                        String currentPlayerName = savedPoint.GetCurrentPlayerName();
                        Player currentPlayer = board.players.stream()
                                .filter(player -> player.getName().equals(currentPlayerName)).findFirst().orElse(null);
                        board.setCurrentPlayer(currentPlayer);

                        // Update gameController
                        this.gameController = new GameController(board);
                        roboRally.createBoardView(gameController);

                    }
                }
            }
        } catch (IOException | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Stop playing the current game, giving the user the option to save
     * the game or to cancel stopping the game. The method returns true
     * if the game was successfully stopped (with or without saving the
     * game); returns false, if the current game was not stopped. In case
     * there is no current game, false is returned.
     *
     * @return true if the current game was stopped, false otherwise
     */
    public boolean stopGame() {
        if (gameController != null) {

            // here we save the game (without asking the user).
            try {
                saveGame();
            } catch (IOException | InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            gameController = null;
            roboRally.createBoardView(null);
            return true;
        }
        return false;
    }

    public void exit() {
        if (gameController != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Exit RoboRally?");
            alert.setContentText("Are you sure you want to exit RoboRally?");
            Optional<ButtonType> result = alert.showAndWait();

            if (!result.isPresent() || result.get() != ButtonType.OK) {
                return; // return without exiting the application
            }
        }

        // If the user did not cancel, the RoboRally application will exit
        // after the option to save the game
        if (gameController == null || stopGame()) {
            Platform.exit();
        }
    }

    public boolean isGameRunning() {
        return gameController != null;
    }

    @Override
    public void update(Subject subject) {
        // XXX do nothing for now
    }

}
