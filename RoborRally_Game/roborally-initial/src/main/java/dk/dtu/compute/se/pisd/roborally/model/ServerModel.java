package dk.dtu.compute.se.pisd.roborally.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class ServerModel  {
    
    private int orderNo;
    private GameController gameController;
    private Space space;


    public ServerModel (int orderNo, GameController gameController, Space space){
        this.orderNo = orderNo;
        this.gameController = gameController;
        this.space = space;
    }

    public int GetOrderNo(){
        return this.orderNo;
    }
    public GameController GetGameController(){
        return this.gameController;
    }
    public Space GetSpace(){
        return this.space;
    }

}
