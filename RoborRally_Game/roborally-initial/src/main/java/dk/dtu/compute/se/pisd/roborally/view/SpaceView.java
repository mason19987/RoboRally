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
import dk.dtu.compute.se.pisd.roborally.controller.*;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.shape.Polygon;
import org.jetbrains.annotations.NotNull;


/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 * @author Mohamad Anwar Meri, s215713@dtu.dk
 *
 */
public class SpaceView extends StackPane implements ViewObserver {

    final public static int SPACE_HEIGHT = 60; // 60; // 75;
    final public static int SPACE_WIDTH = 60;  // 60; // 75;

    public final Space space;
    public Player player;


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
     * We draw here the wall and some fieldActions like, conveyorBelt and Gears
     *
     * @param subject
     * @author Mohamad Anwar Meri, s215713@dtu.dk
     */
    @Override
    public void updateView(Subject subject) {
        if (subject == this.space) {
            this.getChildren().clear();

            Pane pane = new Pane();
            for (Heading walls : space.getWall()) {
                Polygon rectangles = switch (walls) {
                    case SOUTH -> new Polygon(0, SPACE_HEIGHT - 2, SPACE_WIDTH, SPACE_HEIGHT - 2,
                            SPACE_WIDTH, SPACE_HEIGHT, 0, SPACE_HEIGHT);
                    case WEST -> new Polygon(0, 0, 2, 0, 2, SPACE_HEIGHT, 0, SPACE_HEIGHT);
                    case NORTH -> new Polygon(0, 0, SPACE_WIDTH, 0, SPACE_WIDTH, 2, 0, 2);
                    case EAST -> new Polygon(SPACE_WIDTH - 2, 0, SPACE_WIDTH, 0, SPACE_WIDTH, SPACE_HEIGHT,
                            SPACE_WIDTH - 2, SPACE_HEIGHT);
                    default -> null;
                };
                if (rectangles != null) {
                    rectangles.setStroke(Color.ORANGERED);
                    rectangles.setStrokeWidth(3);
                    rectangles.setStrokeLineCap(StrokeLineCap.BUTT);

                    pane.getChildren().add(rectangles);
                }
            }
            this.getChildren().add(pane);

            for (FieldAction actions : space.getActions()) {
                if (actions instanceof ConveyorBelt) {
                    ConveyorBelt conveyorBelt = (ConveyorBelt) actions;
                    Polygon arrow = new Polygon(0.0, 0.0,
                            25.0, 50.0,
                            50.0, 0.0);
                    arrow.setFill(Color.LIGHTBLUE);
                    arrow.setRotate((90 * conveyorBelt.getHeading().ordinal()) % 360);
                    this.getChildren().add(arrow);

                } else if (actions instanceof Gears) {
                    Gears gears = (Gears) actions;
                    Circle circle = new Circle(20);
                    if (gears.isTurnRight()) {
                        circle.setFill(Color.YELLOW);
                    } else {
                        circle.setFill(Color.DARKRED);
                    }
                    getChildren().add(circle);

                } else if (space.getActions().get(0) instanceof Checkpoint checkpoint) {
                    ImageView SpaceView;
                    switch (checkpoint.getCheckpointNumber()) {
                        case 1 -> SpaceView = new ImageView(new Image("Images/checkPoint1.png"));
                        case 2 -> SpaceView = new ImageView(new Image("Images/checkPoint2.png"));
                        case 3 -> SpaceView = new ImageView(new Image("Images/checkPoint3.png"));
                        default -> throw new IllegalStateException("Unexepted value: " + checkpoint.getCheckpointNumber());
                    }
                    this.getChildren().add(SpaceView);

                } else if (space.getActions().get(0) instanceof StartSpaces) {
                    ImageView SpaceView;
                    SpaceView = new ImageView(new Image("Images/StartSpaces.png"));
                    this.getChildren().add(SpaceView);
                    updatePlayer();
                }
            }
            updatePlayer();
        }
    }
}
