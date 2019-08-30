package game;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.stream.Stream;

public class GameLevelOne implements GameLevel {
    public static final Paint SCENE_BACKGROUND = Color.DARKGRAY.darker().darker();

    private Scene myLevelScene;
    private Scene myNextScene;
    private Group myRoot;
    private ImageView myHarryPotter;

    public GameLevelOne() throws Exception {
        setUpScene();
        myLevelScene = new Scene(myRoot, SCENE_WIDTH, SCENE_HEIGHT, SCENE_BACKGROUND);
    }

    public void setNextScene(Scene nextScene) {
        myNextScene = nextScene;
    }

    public Scene getLevelScene() {
        return myLevelScene;
    }

    private void setUpScene() throws Exception {
        //TODO: Delete button and make it so it advances at end of scene.
        myRoot = new Group();
        Button button = new Button("Go to scene 2");
        button.setOnAction(e -> GameMain.getStage().setScene(myNextScene));

        setUpDoor();
        setUpPaddle();
        setUpBricks();
        myRoot.getChildren().add(button);
    }

    private void setUpPaddle() {
        myHarryPotter = getHarryPotter();
        myRoot.getChildren().add(myHarryPotter);
    }

    private void setUpDoor() {
        ImageView door = getDoor();
        myRoot.getChildren().add(door);
    }

    private void setUpBricks() throws FileNotFoundException {
        File level1BricksConfigurationText = new File("resources/level1.txt");
        Scanner scanner = new Scanner(level1BricksConfigurationText);
        int yCoordinateForRow = 0;

        while (scanner.hasNextLine()) {
            String rowConfiguration = scanner.nextLine();
            yCoordinateForRow += BRICK_HEIGHT;
            setRow(rowConfiguration, yCoordinateForRow);
        }
        scanner.close();
    }

    private void setRow(String rowConfiguration, int yCoordinateForRow) {
        int[] rowConfigurationArray = Stream.of(rowConfiguration.split(" " )).mapToInt(Integer::parseInt).toArray();
        int xCoordinateForBrick = 0;
        for (int i=0; i<rowConfigurationArray.length; i++) {
            ImageView brick = createBrickFromId(rowConfigurationArray[i]);
            brick.setX(xCoordinateForBrick);
            brick.setY(yCoordinateForRow);
            xCoordinateForBrick += BRICK_WIDTH;
            myRoot.getChildren().add(brick);
        }
    }
}

