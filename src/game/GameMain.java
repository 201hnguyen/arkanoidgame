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

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        myStage = stage;
        resetStage(GameSceneType.INTRO);
    }

    public static void resetStage(GameSceneType gameSceneType) throws Exception {
        GameScene gameScene = new GameScene(gameSceneType);
        myStage.setScene(gameScene.getScene());
        myStage.setTitle("Breakout Harry Potter Adventure");
        myStage.setResizable(false);
        myStage.show();

        if (gameSceneType == GameSceneType.LEVEL1 ||
                gameSceneType == GameSceneType.LEVEL2 ||
                gameSceneType == GameSceneType.LEVEL3) {
            var frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> {
                try {
                    stepThroughLevel(gameScene, SECOND_DELAY);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            var animation = new Timeline();
            animation.setCycleCount(Timeline.INDEFINITE);
            animation.getKeyFrames().add(frame);
            animation.play();
        }
    }

    private static void stepThroughLevel(GameScene gameScene, double elapsedTime) throws Exception {
        if (gameScene.getBrickStructure().getBricksRemaining() == 0) {
            gameScene.clearLevel(elapsedTime);
        } else {
            gameScene.setBallMotion(elapsedTime);
            gameScene.getBrickStructure().reconfigureBricksBasedOnHits(gameScene.getBall(), gameScene.getBallDirection());
        }
    }
}
