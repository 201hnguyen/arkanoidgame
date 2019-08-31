package game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.Scanner;
import java.util.stream.Stream;

public class GameMain extends Application {
    public static final Paint LEVEL_ONE_BACKGROUND = Color.DARKGRAY.darker().darker();
    public static final Paint LEVEL_TWO_BACKGROUND = Color.BLACK;
    public static final Paint LEVEL_THREE_BACKGROUND = Color.DARKBLUE.darker().darker();
    public static final String LEVEL_ONE_BRICKS_CONFIG_PATH = "resources/level1.txt";
    public static final String LEVEL_TWO_BRICKS_CONFIG_PATH = "resources/level2.txt";
    public static final String LEVEL_THREE_BRICKS_CONFIG_PATH = "resources/level3.txt";
    public static final int SCENE_WIDTH = 1000;
    public static final int SCENE_HEIGHT = 800;
    public static final int BRICK_WIDTH = 100;
    public static final int BRICK_HEIGHT = 50;

    private static Stage myStage;
    private static Scene myLevelOneScene;
    private static Scene myLevelTwoScene;
    private static Scene myLevelThreeScene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        myStage = stage;
        Level levelOne = new Level(myStage, LEVEL_ONE_BRICKS_CONFIG_PATH, LEVEL_ONE_BACKGROUND);
        Level levelTwo = new Level(myStage, LEVEL_TWO_BRICKS_CONFIG_PATH, LEVEL_TWO_BACKGROUND);
        Level levelThree = new Level(myStage, LEVEL_THREE_BRICKS_CONFIG_PATH, LEVEL_THREE_BACKGROUND);

        myLevelOneScene = levelOne.getScene();
        myLevelTwoScene = levelTwo.getScene();
        myLevelThreeScene = levelThree.getScene();

        levelOne.setNextScene(myLevelTwoScene);
        levelTwo.setNextScene(myLevelThreeScene);
        levelThree.setNextScene(myLevelOneScene);

        stage.setScene(myLevelOneScene);
        stage.setTitle("Game");
        stage.show();
    }

}

