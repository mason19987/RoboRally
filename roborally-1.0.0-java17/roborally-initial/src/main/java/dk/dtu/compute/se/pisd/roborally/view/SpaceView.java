/*
 *  This file is part of the initial project provided for the
 *  course "Project in Software Development (02362)" held at
 *  DTU Compute at the Technical University of Denmark.
 *
 *  Copyright (C) 2019, 2020: Ekkart Kindler, ekki@dtu.dk
 *
 *  This software is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; version 2 of the License.
 *
 *  This project is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this project; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import org.jetbrains.annotations.NotNull;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class SpaceView extends StackPane implements ViewObserver {

    final public static int SPACE_HEIGHT = 60; // 60; // 75;
    final public static int SPACE_WIDTH = 60;  // 60; // 75;

    public final Space space;


    public SpaceView(@NotNull Space space) {
        this.space = space;

        // XXX the following styling should better be done with styles
        this.setPrefWidth(SPACE_WIDTH);
        this.setMinWidth(SPACE_WIDTH);
        this.setMaxWidth(SPACE_WIDTH);

        this.setPrefHeight(SPACE_HEIGHT);
        this.setMinHeight(SPACE_HEIGHT);
        this.setMaxHeight(SPACE_HEIGHT);

        if ((space.x + space.y) % 2 == 0) {
            this.setStyle("-fx-background-color: white;");
        } else {
            this.setStyle("-fx-background-color: black;");
        }

        // updatePlayer();

        // This space view should listen to changes of the space
        space.attach(this);
        update(space);
    }

    private void updatePlayer() {

        Player player = space.getPlayer();
        if (player != null) {
            Polygon arrow = new Polygon(0.0, 0.0,
                    10.0, 20.0,
                    20.0, 0.0);
            try {
                arrow.setFill(Color.valueOf(player.getColor()));
            } catch (Exception e) {
                arrow.setFill(Color.MEDIUMPURPLE);
            }

            arrow.setRotate((90 * player.getHeading().ordinal()) % 360);
            this.getChildren().add(arrow);
        }
    }

    /**
     * We draw here all the walls
     * @param subject
     * @author Mohamad Anwar Meri, s215713@dtu.dk
     */
    @Override
    public void updateView(Subject subject) {
        if (subject == this.space) {
            this.getChildren().clear();

            Pane pane = new Pane();
            Rectangle rectangles = new Rectangle(0.0, 0.0, SPACE_WIDTH, SPACE_HEIGHT);
            rectangles.setFill(Color.TRANSPARENT);
            pane.getChildren().add(rectangles);
            for (Heading wall : space.getWall()) {
                if (wall == Heading.SOUTH) {
                    Line line = new Line(2, SPACE_HEIGHT - 2, SPACE_WIDTH - 2, SPACE_HEIGHT - 2);
                    line.setStroke(Color.RED);
                    line.setStrokeWidth(5);
                    line.setStrokeLineCap(StrokeLineCap.ROUND);
                    pane.getChildren().add(line);
                }
                this.getChildren().add(pane);
            }
            Pane paneE = new Pane();
            Rectangle rectangle1 = new Rectangle(0.0, 0.0, SPACE_WIDTH, SPACE_HEIGHT);
            rectangle1.setFill(Color.TRANSPARENT);
            paneE.getChildren().add(rectangle1);
            for (Heading wall : space.getWall()) {
                if (wall == Heading.EAST) {
                    Line line = new Line(SPACE_WIDTH - 2, 2, SPACE_WIDTH - 2, SPACE_HEIGHT - 2);
                    line.setStroke(Color.RED);
                    line.setStrokeWidth(5);
                    line.setStrokeLineCap(StrokeLineCap.ROUND);
                    paneE.getChildren().add(line);
                }
                this.getChildren().add(paneE);
            }

            Pane paneN = new Pane();
            Rectangle rectangle = new Rectangle(0.0, 0.0, SPACE_WIDTH, SPACE_HEIGHT);
            rectangle.setFill(Color.TRANSPARENT);
            paneN.getChildren().add(rectangle);
            for (Heading wall : space.getWall()) {
                if (wall == Heading.NORTH) {
                    Line line = new Line(2, 2, SPACE_WIDTH - 2, 2);
                    line.setStroke(Color.RED);
                    line.setStrokeWidth(5);
                    line.setStrokeLineCap(StrokeLineCap.ROUND);
                    paneN.getChildren().add(line);
                }
                this.getChildren().add(paneN);
            }

            Pane paneW = new Pane();
            Rectangle rectangle2 = new Rectangle(0.0, 0.0, SPACE_WIDTH, SPACE_HEIGHT);
            rectangle2.setFill(Color.TRANSPARENT);
            paneW.getChildren().add(rectangle2);
            for (Heading wall : space.getWall()) {
                if (wall == Heading.WEST) {
                    Line line = new Line(2, 2, 2, SPACE_HEIGHT - 2);
                    line.setStroke(Color.RED);
                    line.setStrokeWidth(5);
                    line.setStrokeLineCap(StrokeLineCap.ROUND);
                    paneW.getChildren().add(line);
                }
                this.getChildren().add(paneW);
            }
        }
                //Her tegnes spilleren
                updatePlayer();

            }
        }






