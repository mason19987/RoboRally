package dk.dtu.compute.se.pisd.roborally.gamelogic;

import dk.dtu.compute.se.pisd.roborally.RoboRally;
import dk.dtu.compute.se.pisd.roborally.clients.MultiplayerClient;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import javafx.application.Platform;
import javafx.concurrent.Task;

/**
 * The ServerMultiplayerLogic class manages the multiplayer logic for the game. It uses a ServerGameState
 * to interact with the game server and determines when it's the turn of the local player.
 * @author Shoaib Zafar Mian, s200784@dtu.dk
 */

public class ServerMultiplayerLogic {
    private MultiplayerClient multiplayerClient;
    private ServerGameState serverGameState;
    private String localIP;
    private String serverIP;
    private int playerId;
    private RoboRally roboRally;
    private GameController gameController;
    public static int currentPlayerTurn = 0;
    public static boolean isMyTurn = false;
    private boolean myTurnCompleted = false;
    private boolean isGameInitiator = false;

    public static boolean newGameLoaded = false;

/**
     * Initializes a new ServerMultiplayerLogic instance with the specified parameters.
     *
     * @param localIP the local IP address.
     * @param serverIP the server IP address.
     * @param playerId the player ID.
     * @param roboRally the RoboRally instance.
     * @param gameController the GameController instance.
     * @param isGameInitiator a boolean indicating if the player initiated the game.
     * 
     * @author Shoaib Zafar Mian, s200784@dtu.dk
     */

    public ServerMultiplayerLogic(String localIP, String serverIP, int playerId,
            RoboRally roboRally, GameController gameController, boolean isGameInitiator) {
        this.multiplayerClient = new MultiplayerClient(serverIP);
        this.localIP = localIP;
        this.serverIP = serverIP;
        this.playerId = playerId;
        this.roboRally = roboRally;
        this.gameController = gameController;
        this.isGameInitiator = isGameInitiator;
        this.serverGameState = new ServerGameState(serverIP);

    }

/**
     * Starts the multiplayer logic. It creates a new task that continuously checks if it's the turn of the local player. 
     * If it is, it invokes the myTurn() method. Otherwise, it keeps loading the server model.
     * @author Shoaib Zafar Mian, s200784@dtu.dk
     */

    public void Start() {
        Task<Void> waitingTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    var serverGameState = new ServerGameState(serverIP);
                    var multiplayerClient = new MultiplayerClient(serverIP);

                    if (isGameInitiator) {
                        serverGameState.setServerModel();
                    } else {
                        Thread.sleep(10000);
                        Platform.runLater(() -> {
                            serverGameState.loadServerModel();
                        });
                    }

                    while (true) {
                        if (!isMyTurn) {
                            currentPlayerTurn = multiplayerClient.getPlayerTurn();
                            isMyTurn = playerId == currentPlayerTurn ? true : false;
                            if (isMyTurn) {
                                myTurn();
                            } else {
                                Platform.runLater(() -> {
                                    serverGameState.loadServerModel();
                                });
                            }
                        }
                        Thread.sleep(4000);
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    var dd = 0;
                }
                return null;
            }
        };
        new Thread(waitingTask).start();
    }

/**
     * Executes the actions for the local player's turn. It loads the server model and waits until the turn is completed. 
     * Once the turn is completed, it sets the server model and notifies the server to proceed to the next player's turn.
     * @author Shoaib Zafar Mian, s200784@dtu.dk
     */

    private void myTurn() {
        Task<Void> waitingTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    var serverGameState = new ServerGameState(serverIP);
                    var multiplayerClient = new MultiplayerClient(serverIP);

                    isMyTurn = true;
                    Platform.runLater(() -> {
                        serverGameState.loadServerModel();
                    });

                    while (!myTurnCompleted) {
                        Thread.sleep(4000);
                        if (ServerMultiplayerLogic.newGameLoaded) {
                            myTurnCompleted = true;
                        }
                    }

                    if (!ServerMultiplayerLogic.newGameLoaded) {
                        Thread.sleep(1000);
                        serverGameState.setServerModel();
                        multiplayerClient.nextPlayerTurn();
                        myTurnCompleted = false;
                        isMyTurn = false;
                    }
                    else{
                        Thread.sleep(5000);
                        ServerMultiplayerLogic.newGameLoaded = false;
                    }
                    return null;
                } catch (Exception e) {
                    var dd = 0;
                }
                return null;
            }
        };
        new Thread(waitingTask).start();
    }

/**
     * Marks the local player's turn as completed.
     * @author Shoaib Zafar Mian, s200784@dtu.dk
     */
    public void turnCompleted() {
        myTurnCompleted = true;
    }

}
