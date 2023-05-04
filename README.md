# RoboRally-Project  - Group 8

This is our RoboRally project from the course [02324](https://kurser.dtu.dk/course/02324) at [DTU](https://www.dtu.dk/uddannelse/diplomingenioer/uddannelsesretninger/softwareteknologi).

##Our team consists of 4 students:
1. S215713 - Mohamad Anwar Meri
2. S224298 - Safi Meissam
3. S200784 - Shoaib Zafar Milan
4. S205592 - Josef Karlsson

## How to run the Application (Through IntelliJ)
1. Firstly, install Java and IntelliJ on your computer, if you don't have it. 


2. Secondly, clone or install the RoboRally initial project from the [Github](https://github.com/mason19987/RoboRally/tree/master) in a place at your computer.


3. Download [JavaFX](https://openjfx.io/openjfx-docs/) and set it up. The software uses JavaFX UI framework. The RoboRally initial project includes Maven setup to work with JavaFX, but before you can run the project you will have to change the run configuration:
- start IntelliJ and open this project (with "File-->Open..."). You should Load Maven to the project.
- Edit the configuration with "Run --> Edit Configurations... ",  select "RoboRally" configuration, and set a module path to your local folder where JavFX is installed ( --module-path "\path\to\javafx-sdk-17\lib" --add-modules javafx.controls,javafx.fxml)
- Try to build and start the project (the main class is RoboRally in the package dk.dtu.compute.se.pisd.roborally.


4. If you have problem with building the software and maven’s JavaFX installation, here you can find detailed information how to install it manually and set up the project properly, by following this tutorial: [FXIJ](https://openjfx.io/openjfx-docs/#IDE-Intellij).


5. You have to make sure that the project runs with version 17.0.1, (with "File-->Project Structure-->Project Settings-->Project-->Project SDK-->17) 


6. Start the program in directory `RoboRally_Game` --> `roborally-initial` --> `src` --> `main` --> `java` --> `roborally` --> and in the class `StartRoboRally`,  this class will start the program. 


7. Click 'File', and then 'New Game'.


8. Choose a board and then the numbers of players.


9. Now you're ready to play.


## Which current features our program have?
- Walls - You won't be able to walk through walls.
- Several kinds of programming cards like `Turn Right`, `Turn Left`, `Forward`, `Fast_Forward`, `Move three forward`, `Left or Right`, `Move one back` and `Turn 180 degrees`
- 2 different boards `FirstBoard` and `SecondBoard`, created by JSON. 
- That a robot can push one forward other robots in front of it
- A counter to the Board which is counted up every time a player makes a move. The status line show the current player and the number of the current moves. 
- FieldAction:
    - ConveyorBelt
    - Gears 
    - Checkpoints - Still in progress.

##We are currently working on:
- Saving a game
- Loading a saved game.
- Checkpoints
- Other FieldActions like `Laser` and `Pits`

##Plans for the future:
Creating a database and storing a game in it, where you can load the game from the database.
1. Create a game and save it in database.
2. Load the game from the database
3. Working on more FieldActions. 
