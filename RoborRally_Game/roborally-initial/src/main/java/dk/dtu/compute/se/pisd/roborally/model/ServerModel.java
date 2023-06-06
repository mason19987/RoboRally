package dk.dtu.compute.se.pisd.roborally.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

/*
 * @author Shaoib Zafar Mian, s200784@dtu.dk
 */

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class ServerModel  {
    
    private String name;
    private int boardWidth;
    private int boardHeight;
    private String currentPlayerName;
    private String gameName;
    private Phase phase;
    private int step;
    private List<ServerPlayerModel> players;


    public ServerModel() {
    }

    public ServerModel(String name, int boardWidth, int boardHeight, String currentPlayerName, String gameName, Phase phase, int step,
            List<ServerPlayerModel> players) {
        this.name = name;
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.currentPlayerName = currentPlayerName;
        this.gameName = gameName;
        this.phase = phase;
        this.step = step;
        this.players = players;
    }

    public String GetName() {
        return this.name;
    }

    public int GetBoardWidth() {
        return this.boardWidth;
    }

    public int GetBoardHeight() {
        return this.boardHeight;
    }

    public String GetCurrentPlayerName() {
        return this.currentPlayerName;
    }

    public String GetGameName() {
        return this.gameName;
    }

    public Phase GetPhase() {
        return this.phase;
    }

    public int GetStep() {
        return this.step;
    }

    public List<ServerPlayerModel> GetPlayers() {
        return this.players;
    }

}

