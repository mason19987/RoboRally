package dk.dtu.compute.se.pisd.roborally.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 * Testing the heading methods to ensure that it works
 * @author Mohamad Anwar Meri, s215713@dtu.dk
 */
public class HeadingTest {

    @Test
    void next() {
        Heading headingTest = Heading.SOUTH;
        Heading heading = headingTest.next();

        Assertions.assertEquals (heading, Heading.WEST, "The player should be heading WEST!");
    }

    @Test
    void prev() {
        Heading headingTest = Heading.SOUTH;
        Heading heading = headingTest.prev();

        Assertions.assertEquals (heading, Heading.EAST, "The player should be heading EAST!");
    }


    @Test
    void round() {
        Heading headingTest = Heading.SOUTH;
        Heading heading = headingTest.round();

        Assertions.assertEquals (heading, Heading.NORTH, "The player should be heading NORTH!");
    }
}
