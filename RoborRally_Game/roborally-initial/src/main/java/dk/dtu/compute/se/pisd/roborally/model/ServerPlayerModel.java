package dk.dtu.compute.se.pisd.roborally.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;


/*
 * @author Shaoib Zafar Mian, s200784@dtu.dk
 */

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class ServerPlayerModel {
    private String name; // Use as ID
    private String color;

    private int positionX; // Set as Space
    private int positionY; // Set as Space

    private Heading heading;

    private CommandCardField[] cards;

    public ServerPlayerModel() {
    }

    public ServerPlayerModel(String name, String color, int positionX, int positionY, Heading heading,
            CommandCardField[] cards) {
        this.name = name;
        this.color = color;
        this.positionX = positionX;
        this.positionY = positionY;
        this.heading = heading;
        this.cards = cards;
    }

    public String GetName() {
        return this.name;
    }

    public String GetColor() {
        return this.color;
    }

    public int GetPositionX() {
        return this.positionX;
    }

    public int GetPositionY() {
        return this.positionY;
    }

    public Heading GetHeading() {
        return this.heading;
    }

    public CommandCardField[] GetCards() {
        return this.cards;
    }
}
