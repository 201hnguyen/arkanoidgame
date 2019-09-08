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

    private static Stage myStage;
    private static GameScene myCurrentGameScene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        myStage = stage;
        resetStage(GameScene.GameSceneType.INTRO);
        setAnimation();
    }

    public static void resetStage(GameScene.GameSceneType gameSceneType) {
        myCurrentGameScene = new GameScene(gameSceneType);
        myStage.setScene(myCurrentGameScene.getScene());
        myStage.setTitle("Breakout Harry Potter Adventure");
        myStage.setResizable(false);
        myStage.show();
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
