package dk.dtu.compute.se.pisd.roborally.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class ServerModel  {
    
    private String name;
    private GameController gameController;
    private Space space;


    public ServerModel (String name, GameController gameController, Space space){
        this.name = name;
        this.gameController = gameController;
        this.space = space;
    }

    public String GetName(){
        return this.name;
    }
    public GameController GetGameController(){
        return this.gameController;
    }
    public Space GetSpace(){
        return this.space;
    }

}
