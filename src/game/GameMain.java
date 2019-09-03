package game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameMain extends Application {
    public static final String LEVEL_ONE_BACKGROUND = "background1.jpg";
    public static final String LEVEL_TWO_BACKGROUND = "background2.jpg";
    public static final String LEVEL_THREE_BACKGROUND = "background3.jpg";
    public static final String INTRO_BACKGROUND = "backgroundintro.jpg";
    public static final String LOSE_BACKGROUND = "backgroundlose.jpg";
    public static final String WIN_BACKGROUND = "backgroundwin.jpg";
    public static final String LEVEL_ONE_BRICKS_CONFIG_PATH = "resources/level1.txt";
    public static final String LEVEL_TWO_BRICKS_CONFIG_PATH = "resources/level2.txt";
    public static final String LEVEL_THREE_BRICKS_CONFIG_PATH = "resources/level3.txt";
    public static final int SCENE_WIDTH = 1000;
    public static final int SCENE_HEIGHT = 800;
    public static final int FRAMES_PER_SECOND = 60;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;

    private static Stage myStage;
    private GameScene myGameSceneOne;
    private GameScene myGameSceneTwo;
    private GameScene myGameSceneThree;



    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        myStage = stage;
        myGameSceneOne = new GameScene(myStage, LEVEL_ONE_BRICKS_CONFIG_PATH, LEVEL_ONE_BACKGROUND);
        myGameSceneTwo = new GameScene(myStage, LEVEL_TWO_BRICKS_CONFIG_PATH, LEVEL_TWO_BACKGROUND);
        myGameSceneThree = new GameScene(myStage, LEVEL_THREE_BRICKS_CONFIG_PATH, LEVEL_THREE_BACKGROUND);

        myGameSceneOne.setNextGameScene(myGameSceneTwo);
        myGameSceneTwo.setNextGameScene(myGameSceneThree);
        myGameSceneThree.setNextGameScene(myGameSceneOne);

        resetStage(myGameSceneOne);
    }

    public static void resetStage(GameScene gameSceneGameScene) {
        myStage.setScene(gameSceneGameScene.getScene());
        myStage.setTitle("TODO: Change title");
        myStage.setResizable(false);
        myStage.show();

        var frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> stepThroughLevel(gameSceneGameScene, SECOND_DELAY));
        var animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
    }

    private static void stepThroughLevel(GameScene gameScene, double elapsedTime) {
        gameScene.getBall().setBallMotion(elapsedTime, gameScene.getMainCharacter());
        gameScene.bounceOffBricks();
    }

}

