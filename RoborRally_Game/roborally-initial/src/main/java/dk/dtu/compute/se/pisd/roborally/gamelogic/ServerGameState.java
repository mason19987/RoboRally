package dk.dtu.compute.se.pisd.roborally.gamelogic;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import dk.dtu.compute.se.pisd.roborally.clients.MultiplayerClient;
import dk.dtu.compute.se.pisd.roborally.controller.AppController;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.BoardTemplate;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.SpaceTemplate;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Command;
import dk.dtu.compute.se.pisd.roborally.model.CommandCard;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.ServerModel;
import dk.dtu.compute.se.pisd.roborally.model.ServerPlayerModel;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import dk.dtu.compute.se.pisd.roborally.fileaccess.LoadBoard;

/**
 * The ServerGameState class is responsible for interacting with the game server. It loads and sets the server model 
 * for the game state. This class is utilized for multiplayer gaming sessions and communicates with the server 
 * using a MultiplayerClient instance.
 * @author Shoaib Zafar Mian, s200784@dtu.dk
 */

public class ServerGameState {

    private MultiplayerClient multiplayerClient;
    private String serverIP;

  /**
     * Creates a ServerGameState instance with the provided server IP address.
     *
     * @param serverIP the IP address of the server.
     * @author Shoaib Zafar Mian, s200784@dtu.dk
     */

    public ServerGameState(String serverIP) {
        this.serverIP = serverIP;
        multiplayerClient = new MultiplayerClient(this.serverIP);
    }

/**
     * Loads the game state from the server. It retrieves a saved point from the server and constructs 
     * a new game state based on that. The board, players, and their respective attributes are 
     * recreated based on the information from the server.
     * @author Shoaib Zafar Mian, s200784@dtu.dk
     */

    public void loadServerModel() {
        ServerModel savedPoint = multiplayerClient.getSaveState();
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
                player.GetName(),
                player.GetCheckPoints())).collect(Collectors.toList());

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
        AppController.gameController = new GameController(board);
        AppController.roboRally.createBoardView(AppController.gameController);
        AppController.gameController.board.notifyAll();
    }

 /**
     * Sets the game state on the server. It constructs a ServerModel from the current game state
     * and sends it to the server. The server uses this information to update its state.
     * @author Shoaib Zafar Mian, s200784@dtu.dk
     */

    public void setServerModel() {
        try {
            var gameController = AppController.gameController;
            List<ServerPlayerModel> players = List.of(
                    gameController.board.players.stream().map(player -> new ServerPlayerModel(
                            player.getName(),
                            player.getColor(),
                            player.getSpace().x,
                            player.getSpace().y,
                            player.getHeading(),
                            Arrays.stream(player.GetCards())
                                    .map(card -> new CommandCard(card.getCard().command).getName())
                                    .collect(Collectors.toList()),
                            player.getCheckPoints()))
                            .toArray(ServerPlayerModel[]::new));

            ServerModel serverModel = new ServerModel(
                    "MultiplayerGame",
                    gameController.board.boardName,
                    gameController.board.getCurrentPlayer().getName(),
                    "MultiplayerGame",
                    gameController.board.getPhase(),
                    gameController.board.getStep(),
                    players, null);

            multiplayerClient.postSaveState(serverModel);
        } catch (Exception e) {
            // TODO: Error dialog?
        }
    }
}
