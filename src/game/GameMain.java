package game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;

import javafx.stage.Stage;
import javafx.util.Duration;

public class GameMain extends Application {
    public static final int SCENE_WIDTH = 1000;
    public static final int SCENE_HEIGHT = 800;
    public static final int FRAMES_PER_SECOND = 60;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;

    private static GameScene myCurrentGameScene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        GameStatus gameStatus = new GameStatus();
        resetStage(stage, GameScene.GameSceneType.INTRO, gameStatus);
        setAnimation();
    }

    public static void resetStage(Stage stage, GameScene.GameSceneType gameSceneType, GameStatus gameStatus) {
        myCurrentGameScene = new GameScene(stage, gameSceneType, gameStatus);
        if (myCurrentGameScene.getGameSceneType() == GameScene.GameSceneType.LEVEL1 ||
                myCurrentGameScene.getGameSceneType() == GameScene.GameSceneType.LEVEL2 ||
                myCurrentGameScene.getGameSceneType() == GameScene.GameSceneType.LEVEL3) {
        }
        stage.setScene(myCurrentGameScene.getScene());
        stage.setTitle("Breakout Harry Potter Adventure");
        stage.setResizable(false);
        stage.show();
    }

    public static void setAnimation() {
        var frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> {
            if (myCurrentGameScene.getGameSceneType() == GameScene.GameSceneType.LEVEL1 ||
                    myCurrentGameScene.getGameSceneType() == GameScene.GameSceneType.LEVEL2 ||
                    myCurrentGameScene.getGameSceneType() == GameScene.GameSceneType.LEVEL3) {
                stepThroughLevel(myCurrentGameScene, SECOND_DELAY);
            }
        });

        var timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(frame);
        timeline.play();
    }

    private static void stepThroughLevel(GameScene gameScene, double elapsedTime) {
        if (gameScene.getBricks().size() == 0) {
            gameScene.clearLevel(elapsedTime);
        } else {
            gameScene.getBall().setBallMotion(elapsedTime, gameScene);
            gameScene.reconfigureBricksBasedOnHits();
            gameScene.handlePowerup(elapsedTime);
        }
    }

}
