package game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameMain extends Application {
    public static final Paint LEVEL_ONE_BACKGROUND = Color.DARKGRAY.darker().darker();
    public static final Paint LEVEL_TWO_BACKGROUND = Color.BLACK;
    public static final Paint LEVEL_THREE_BACKGROUND = Color.DARKBLUE.darker().darker();
    public static final String LEVEL_ONE_BRICKS_CONFIG_PATH = "resources/level1.txt";
    public static final String LEVEL_TWO_BRICKS_CONFIG_PATH = "resources/level2.txt";
    public static final String LEVEL_THREE_BRICKS_CONFIG_PATH = "resources/level3.txt";
    public static final int SCENE_WIDTH = 1000;
    public static final int SCENE_HEIGHT = 800;
    public static final int FRAMES_PER_SECOND = 60;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;

    private static Stage myStage;
    private static Level myLevelOne;
    private static Level myLevelTwo;
    private static Level myLevelThree;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        myStage = stage;
        myLevelOne = new Level(myStage, LEVEL_ONE_BRICKS_CONFIG_PATH, LEVEL_ONE_BACKGROUND);
        myLevelTwo = new Level(myStage, LEVEL_TWO_BRICKS_CONFIG_PATH, LEVEL_TWO_BACKGROUND);
        myLevelThree = new Level(myStage, LEVEL_THREE_BRICKS_CONFIG_PATH, LEVEL_THREE_BACKGROUND);

        myLevelOne.setNextLevel(myLevelTwo);
        myLevelTwo.setNextLevel(myLevelThree);
        myLevelThree.setNextLevel(myLevelOne);

        resetStage(myLevelOne);
    }

    public static void resetStage(Level level) {
        myStage.setScene(level.getScene());
        myStage.setTitle("TODO: Change title");
        myStage.setResizable(false);
        myStage.show();

        var frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> stepThroughLevel(level, SECOND_DELAY));
        var animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
    }

    private static void stepThroughLevel(Level level, double elapsedTime) {
        level.getBall().setBallMotion(elapsedTime, level.getMainCharacter());
        level.getBall().bounceOffBricks(level.getBricks(), level.getRoot());
    }
}

