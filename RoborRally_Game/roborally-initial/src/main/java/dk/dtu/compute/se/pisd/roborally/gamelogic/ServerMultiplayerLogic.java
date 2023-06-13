package dk.dtu.compute.se.pisd.roborally.gamelogic;

import dk.dtu.compute.se.pisd.roborally.RoboRally;
import dk.dtu.compute.se.pisd.roborally.clients.MultiplayerClient;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import javafx.application.Platform;
import javafx.concurrent.Task;

public class ServerMultiplayerLogic {
    private MultiplayerClient multiplayerClient;
    private ServerGameState serverGameState;
    private String localIP;
    private String serverIP;
    private int playerId;
    private RoboRally roboRally;
    private GameController gameController;
    public static int currentPlayerTurn = 0;
    private static boolean isMyTurn = false;
    private boolean myTurnCompleted = false;
    private boolean isGameInitiator = false;

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
                    }
                    Thread.sleep(1000);
                    serverGameState.setServerModel();
                    multiplayerClient.nextPlayerTurn();
                    myTurnCompleted = false;
                    isMyTurn = false;
                    return null;
                } catch (Exception e) {
                    var dd = 0;
                }
                return null;
            }
        };
        new Thread(waitingTask).start();
    }

    public void turnCompleted() {
        myTurnCompleted = true;
    }

}
