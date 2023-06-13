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
public class MultiplayerPlayerModel {

    private int id;
    private String name;
    private String ipAddress;
    private List<Integer> checkPoints;

    public MultiplayerPlayerModel() {
        // Needed to convert from json file to object
    }

    public MultiplayerPlayerModel(int id, String name, String ipAddress, List<Integer> checkPoints) {
        this.id = id;
        this.name = name;
        this.ipAddress = ipAddress;
        this.checkPoints = checkPoints;
    }

    public int GetId() {
        return id;
    }

    public String GetName() {
        return name;
    }

    public List<Integer> GetCheckPoints() {
        return checkPoints;
    }
}