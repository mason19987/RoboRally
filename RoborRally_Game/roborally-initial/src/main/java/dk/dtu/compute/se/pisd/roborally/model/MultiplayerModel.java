package dk.dtu.compute.se.pisd.roborally.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

/**
 * ...
 *
 * @author Shaoib Zafar Mian, s200784@dtu.dk
 */

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class MultiplayerModel {

    private int playerTurn; // if 0 then player 0 turn, if 1 then player 1 turn, based on player id
    private List<MultiplayerPlayerModel> players;

    public MultiplayerModel() {
        // Needed to convert from json file to object
    }

    public MultiplayerModel(int playerTurn, List<MultiplayerPlayerModel> players) {
        this.playerTurn = playerTurn;
        this.players = players;
    }

    public int GetPlayerTurn() {
        return playerTurn;
    }

    public List<MultiplayerPlayerModel> GetPlayers() {
        return players;
    }

    public void SetPlayerTurn(int playerTurn) {
        this.playerTurn = playerTurn;
    }
}
