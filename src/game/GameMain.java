package game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
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
    private static int score;
    private static Label scoreLabel;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        myStage = stage;
        initializeScore();
        resetStage(GameScene.GameSceneType.INTRO);
        setAnimation();
    }

    private static void initializeScore() {
        score = 0;
        scoreLabel = new Label();
        scoreLabel.setText("" + score);
        scoreLabel.setLayoutX(785);
        scoreLabel.setLayoutY(7);
        scoreLabel.setTextFill(Color.LIGHTGRAY);
        scoreLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 25));
    }

    public static void resetStage(GameScene.GameSceneType gameSceneType) {
        myCurrentGameScene = new GameScene(gameSceneType);
        if (myCurrentGameScene.getGameSceneType() == GameScene.GameSceneType.INTRO) {
            score = 0;
        } else if (myCurrentGameScene.getGameSceneType() == GameScene.GameSceneType.WIN || myCurrentGameScene.getGameSceneType() == GameScene.GameSceneType.LOSE) {
            scoreLabel.setLayoutX(500);
            scoreLabel.setLayoutY(450);
            scoreLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 40));
            myCurrentGameScene.getRoot().getChildren().add(scoreLabel);
        }
        if (myCurrentGameScene.getGameSceneType() == GameScene.GameSceneType.LEVEL1 ||
                myCurrentGameScene.getGameSceneType() == GameScene.GameSceneType.LEVEL2 ||
                myCurrentGameScene.getGameSceneType() == GameScene.GameSceneType.LEVEL3) {
            myCurrentGameScene.getRoot().getChildren().add(scoreLabel);
        }
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

    public static void increaseScore(int value) {
        score += value;
        scoreLabel.setText("" + score);
    }

    public static void decreaseScore(int value) {
        score -= value;
        scoreLabel.setText("" + score);
    }
}
