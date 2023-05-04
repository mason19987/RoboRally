package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;


/**
 * Gears are circular objects that rotate clockwise or counterclockwise.
 * When a robot lands on a gear, it rotates with the gear in the same direction.
 * Here we have 2 different gears, one rotate to the right "rotate clockwise" when it's true, and it has a yellow color
 * The other one rotate to the left "rotate counterclockwise" when it's false, and it's darkred color.
 *
 * @author Mohamad Anwar Meri, s215713@dtu.dk
 */
public class Gears extends FieldAction {

private final boolean turnRight;

public Gears(boolean turnRight){
    this.turnRight = turnRight;
}

    @Override
    public boolean doAction(GameController gameController, Space space) {
        Player player = space.getPlayer();
        if(player != null) {
            if (turnRight) {
                player.setHeading(player.getHeading().next());
            } else {
                player.setHeading(player.getHeading().prev());
                }
            return true;
            }
        return false;
        }

    public boolean isTurnRight() {
        return turnRight;
    }
}
