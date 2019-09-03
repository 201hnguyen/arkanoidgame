package game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Optional;

public class GameMain extends Application {
    public static final String LEVEL_ONE_BACKGROUND = "background1.jpg";
    public static final String LEVEL_TWO_BACKGROUND = "background2.jpg";
    public static final String LEVEL_THREE_BACKGROUND = "background3.jpg";
    public static final String INTRO_BACKGROUND = "backgroundintro.jpg";
    public static final String HOW_TO_PLAY_BACKGROUND = "backgroundhowtoplay.jpg";
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
    private GameScene myIntroScene;
    private GameScene myHowToPlayScene;
    private GameScene myLoseScene;
    private GameScene myWinScene;



    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        myStage = stage;

        myIntroScene = new GameScene(myStage, INTRO_BACKGROUND, GameScene.GameSceneType.INTRO, Optional.empty());
        myHowToPlayScene = new GameScene(myStage, HOW_TO_PLAY_BACKGROUND, GameScene.GameSceneType.HOW_TO_PLAY, Optional.empty());
        myGameSceneOne = new GameScene(myStage, LEVEL_ONE_BACKGROUND, GameScene.GameSceneType.LEVEL, Optional.of(LEVEL_ONE_BRICKS_CONFIG_PATH));
        myGameSceneTwo = new GameScene(myStage, LEVEL_TWO_BACKGROUND, GameScene.GameSceneType.LEVEL, Optional.of(LEVEL_TWO_BRICKS_CONFIG_PATH));
        myGameSceneThree = new GameScene(myStage, LEVEL_THREE_BACKGROUND, GameScene.GameSceneType.LEVEL, Optional.of(LEVEL_THREE_BRICKS_CONFIG_PATH));
        myLoseScene = new GameScene(myStage, LOSE_BACKGROUND, GameScene.GameSceneType.LOSE, Optional.empty());
        myWinScene = new GameScene(myStage, WIN_BACKGROUND, GameScene.GameSceneType.WIN, Optional.empty());

        myIntroScene.setNextGameScene(myHowToPlayScene);
        myHowToPlayScene.setNextGameScene(myGameSceneOne);
        myGameSceneOne.setNextGameScene(myGameSceneTwo);
        myGameSceneTwo.setNextGameScene(myGameSceneThree);
        myGameSceneThree.setNextGameScene(myWinScene);
        myLoseScene.setNextGameScene(myIntroScene);
        myWinScene.setNextGameScene(myIntroScene);

        resetStage(myIntroScene);
    }

    public static void resetStage(GameScene gameScene) {
        myStage.setScene(gameScene.getScene());
        myStage.setTitle("TODO: Change title");
        myStage.setResizable(false);
        myStage.show();

        if (gameScene.getGameSceneType() == GameScene.GameSceneType.LEVEL) {
            var frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> stepThroughLevel(gameScene, SECOND_DELAY));
            var animation = new Timeline();
            animation.setCycleCount(Timeline.INDEFINITE);
            animation.getKeyFrames().add(frame);
            animation.play();
        }
    }

    private static void stepThroughLevel(GameScene gameScene, double elapsedTime) {
        gameScene.getBall().setBallMotion(elapsedTime, gameScene.getMainCharacter());
        gameScene.setBricksHits();
    }

}

