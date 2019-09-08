package game;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;

public class GameStatus {
    public static final String HEART_IMAGE = "heart.png";
    public static final int HEART_WIDTH = 25;
    public static final int HEART_HEIGHT = 30;
    public static final int X_OFFSET = 95;
    public static final int Y_OFFSET = 5;
    public static final int INITIAL_LIVES = 3;

    private ArrayList<ImageView> myHearts = new ArrayList<>();
    private int myCurrentXCoordinateForHearts;
    private int myScore;
    private Label myScoreLabel;

    public GameStatus() {
        resetGameStatus();
    }

    public void resetGameStatus() {
        myHearts.clear();
        myCurrentXCoordinateForHearts = X_OFFSET;
        myScore=0;
        myScoreLabel = new Label();

    }

    public void resetLivesAndAddImageViewToRoot(Pane root) {
        myCurrentXCoordinateForHearts = X_OFFSET;
        for (int i = 0; i < INITIAL_LIVES; i++) {
            addHeartImageView(root);
        }
    }

    private void addHeartImageView(Pane root) {
        Image heart = new Image(this.getClass().getClassLoader().getResourceAsStream(HEART_IMAGE));
        ImageView heartImageView = new ImageView(heart);
        heartImageView.setFitWidth(HEART_WIDTH);
        heartImageView.setFitHeight(HEART_HEIGHT);
        heartImageView.setY(Y_OFFSET);
        heartImageView.setX(myCurrentXCoordinateForHearts);
        root.getChildren().add(heartImageView);
        myHearts.add(heartImageView);
        myCurrentXCoordinateForHearts += HEART_WIDTH + 5;
    }

    public void decreaseLives(GameScene gameScene) {
        myCurrentXCoordinateForHearts -= HEART_WIDTH + 5;
        if (myHearts.size() > 0) {
            gameScene.getRoot().getChildren().remove(myHearts.get(myHearts.size()-1));
            myHearts.remove(myHearts.size() -1);
            gameScene.getBall().resetBall(gameScene.getMainCharacter());
        }
    }

    public void increaseLives(Pane root) {
        addHeartImageView(root);
    }

    public int getLivesRemaining() {
        return myHearts.size();
    }

    public Label getScoreLabelForLevel() {
        myScoreLabel.setText("" + myScore);
        myScoreLabel.setLayoutX(785);
        myScoreLabel.setLayoutY(7);
        myScoreLabel.setTextFill(Color.LIGHTGRAY);
        myScoreLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 25));
        return myScoreLabel;
    }

    public Label getScoreLabelForNonLevel() {
        myScoreLabel.setLayoutX(500);
        myScoreLabel.setLayoutY(450);
        myScoreLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 40));
        return myScoreLabel;
    }

    public void increaseScore(int value) {
        myScore += value;
        myScoreLabel.setText("" + myScore);
    }

    public void decreaseScore(int value) {
        myScore -= value;
        myScoreLabel.setText("" + myScore);
    }

    public int getRemainingLives() {
        return myHearts.size();
    }

}
