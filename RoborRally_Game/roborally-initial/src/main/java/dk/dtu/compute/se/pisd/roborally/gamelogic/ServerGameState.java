package dk.dtu.compute.se.pisd.roborally.gamelogic;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import dk.dtu.compute.se.pisd.roborally.RoboRally;
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

public class ServerGameState {

    private MultiplayerClient multiplayerClient;
    private String serverIP;

    public ServerGameState(String serverIP) {
        this.serverIP = serverIP;
        multiplayerClient = new MultiplayerClient(this.serverIP);
    }

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
        AppController.gameController = new GameController(board);
        AppController.roboRally.createBoardView(AppController.gameController);
        AppController.gameController.board.notifyAll();
    }

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
                                    .collect(Collectors.toList())))
                            .toArray(ServerPlayerModel[]::new));

            ServerModel serverModel = new ServerModel(
                    "MultiplayerGame",
                    gameController.board.boardName,
                    gameController.board.getCurrentPlayer().getName(),
                    "MultiplayerGame",
                    gameController.board.getPhase(),
                    gameController.board.getStep(),
                    players);

            multiplayerClient.postSaveState(serverModel);
        } catch (Exception e) {
            // TODO: Error dialog?
        }
    }
}
