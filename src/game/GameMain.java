package game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;

public class GameMain extends Application {
    public static final int SCENE_WIDTH = 1000;
    public static final int SCENE_HEIGHT = 800;
    public static final int FRAMES_PER_SECOND = 60;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;

    private static Stage myStage;
    private static GameScene myCurrentGameScene;
    private static Timeline myTimeline;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        myStage = stage;
        resetStage(GameSceneType.INTRO);
        setAnimation();
    }

    public static void resetStage(GameSceneType gameSceneType) {
        myCurrentGameScene = new GameScene(gameSceneType);
        myStage.setScene(myCurrentGameScene.getScene());
        myStage.setTitle("Breakout Harry Potter Adventure");
        myStage.setResizable(false);
        myStage.show();
    }

    public static void setAnimation() {
        var frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> {
            if (myCurrentGameScene.getGameSceneType() == GameSceneType.LEVEL1 ||
                    myCurrentGameScene.getGameSceneType() == GameSceneType.LEVEL2 ||
                    myCurrentGameScene.getGameSceneType() == GameSceneType.LEVEL3) {
                stepThroughLevel(myCurrentGameScene, SECOND_DELAY);
            }
        });

        myTimeline = new Timeline();
        myTimeline.setCycleCount(Timeline.INDEFINITE);
        myTimeline.getKeyFrames().add(frame);
        myTimeline.play();
    }

    private static void stepThroughLevel(GameScene gameScene, double elapsedTime) {
        ArrayList<Powerup> activatedPowerup = new ArrayList<>();
        if (gameScene.getBrickStructure().getBricksRemaining() == 0) {
            gameScene.clearLevel(elapsedTime);
        } else {
            gameScene.getBall().setBallMotion(elapsedTime, gameScene);
            gameScene.getBrickStructure().reconfigureBricksBasedOnHits(gameScene);
            for (Powerup powerup : gameScene.getPresentPowerups()) {
                powerup.setPowerupMotion(elapsedTime, gameScene);
                if (powerup.powerupIsActivated()) {
                    activatedPowerup.add(powerup);
                }
            }

            for (Powerup powerup : activatedPowerup) {
                gameScene.removeActivatedPowerup(powerup);
            }
        }
    }
}
