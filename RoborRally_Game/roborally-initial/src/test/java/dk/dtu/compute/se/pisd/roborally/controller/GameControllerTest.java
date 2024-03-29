package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

/**
 * We are going to test GameController at this class to ensure that it works.
 * @author Mohamad Anwar Meri, s215713@dtu.dk
 */
class GameControllerTest {

    private final int TEST_WIDTH = 8;
    private final int TEST_HEIGHT = 8;

    private GameController gameController;

    @BeforeEach
    void setUp() {
        Board board = new Board(TEST_WIDTH, TEST_HEIGHT);
        gameController = new GameController(board);
        for (int i = 0; i < 6; i++) {
            Player player = new Player(board, null, "Player " + i, new ArrayList<>());
            board.addPlayer(player);
            player.setSpace(board.getSpace(i, i));
            player.setHeading(Heading.values()[i % Heading.values().length]);
        }
        board.setCurrentPlayer(board.getPlayer(0));
    }

    @AfterEach
    void tearDown() {
        gameController = null;
    }

    @Test
    void moveCurrentPlayerToSpace() {
        Board board = gameController.board;
        Player player1 = board.getPlayer(0);
        Player player2 = board.getPlayer(1);

        gameController.moveCurrentPlayerToSpace(board.getSpace(0, 4));

        assertEquals(player1, board.getSpace(0, 4).getPlayer(), "Player " + player1.getName() + " should beSpace (0,4)!");
        Assertions.assertNull(board.getSpace(0, 0).getPlayer(), "Space (0,0) should be empty!");
        assertEquals(player2, board.getCurrentPlayer(), "Current player should be " + player2.getName() + "!");
    }

    @Test
    void moveForward() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        gameController.moveForward(current);

        assertEquals(current, board.getSpace(0, 1).getPlayer(), "Player " + current.getName() + " should beSpace (0,1)!");
        assertEquals(Heading.SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
        Assertions.assertNull(board.getSpace(0, 0).getPlayer(), "Space (0,0) should be empty!");
    }

    /**
     * Here we're testing the method turnRight to check if the player turn to the specific direction.
     *
     * @author Mohamad Anwar Meri, s215713@dtu.dk
     */
    @Test
    void turnRight() {
        Board board = gameController.board;
        Player player = board.getPlayer(3);
        player.setHeading(Heading.SOUTH);

        gameController.turnRight(player);
        assertEquals(player.getHeading(), Heading.WEST, " " + player.getName() + " should be heading " + player.getHeading());

    }

    /**
     * Here we're testing the method turnLeft to check if the player turn to the specific direction.
     *
     * @author Mohamad Anwar Meri, s215713@dtu.dk
     */

    @Test
    void turnLeft() {
        Board board = gameController.board;
        Player player = board.getPlayer(4);
        player.setHeading(Heading.SOUTH);

        gameController.turnLeft(player);
        assertEquals(player.getHeading(), Heading.EAST, " " + player.getName() + " should be heading " + player.getHeading());
    }

    /**
     * We're testing the method fastForward to check if the player move two times forward.
     *
     * @author Mohamad Anwar Meri, s215713@dtu.dk
     */
    @Test
    void fastForward() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        gameController.fastForward(current);

        assertEquals(current, board.getSpace(0, 2).getPlayer(), current.getName() + " should beSpace (0,2)!");
        assertEquals(Heading.SOUTH, current.getHeading(), "Player 1 should be heading South!");
        Assertions.assertNull(board.getSpace(0, 0).getPlayer(), "Space (0,0) should be empty!");
    }

    @Test
    void moveThreeForward() {
            Board board = gameController.board;
            Player current = board.getCurrentPlayer();

            gameController.threeForward(current);
            assertEquals(current, board.getSpace(0, 3).getPlayer(), " Player 1" + " should beSpace (0,3)!" );
            assertEquals(Heading.SOUTH, current.getHeading(), " Player 1 " + " should be heading SOUTH!");
            Assertions.assertNull(board.getSpace(0, 0).getPlayer(), "Space (0,0) should be empty!");
        }

    @Test
    void moveOneBack() {
            Board board = gameController.board;
            Player current = board.getCurrentPlayer();

            gameController.rearWards(current);
            assertEquals(current, board.getSpace(0, 0).getPlayer(), " Player 1" + " should beSpace (0,1)!" );
            assertEquals(Heading.SOUTH, current.getHeading(), " Player 1 " + " should be heading SOUTH!");
            Assertions.assertNull(board.getSpace(0, 1).getPlayer(), "Space (0,0) should be empty!");
        }


    @Test
    void half_Rotation() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        current.setHeading(Heading.NORTH);
        gameController.half_Rotation(current);
        Assertions.assertEquals(Heading.SOUTH, current.getHeading());
    }
}






