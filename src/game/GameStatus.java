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
    public static final int FULL_LIVES = 4;

    private ArrayList<ImageView> myHearts = new ArrayList<>();
    private int myCurrentXCoordinateForHearts;
    private int myScore;
    private Label myScoreLabel;

    /**
     * Represents the current status of the game; this is used for keeping track of the number of lives remaining as
     * well as the player's score. Assumes that "heart.png" exists in the resource folder. For example, this class
     * can be used to reset the score when a new game is initiated, reset the lives at a new level, or increasing/
     * decreasing the remaining lives because the player hit a powerup or a deadzone or the ball goes of the bottom edge
     * of the screen.
     */
    public GameStatus() {
        resetGameStatus();
    }

    /**
     * Resets the status of the game by clearing all the lives/hearts and setting the score back to zero.
     */
    public void resetGameStatus() {
        myHearts.clear();
        myCurrentXCoordinateForHearts = X_OFFSET;
        myScore=0;
        myScoreLabel = new Label();

    }

    /**
     * Resets the lives for a level when a new level is initiated.
     * @param root the root of the GameScene, used for adding the hearts that represent the lives into the scene itself.
     */
    public void resetLivesAndAddImageViewToRoot(Pane root) {
        myCurrentXCoordinateForHearts = X_OFFSET;
        for (int i = 0; i < FULL_LIVES; i++) {
            addHeart(root);
        }
    }

    private void addHeart(Pane root) {
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

    /**
     * Decreases the number of lives the player has left by one.
     * @param gameScene the current gameScene whose life needs to be altered; the parameter allows us to keep a reference
     *                  to the gameScene and its roots in order to remove the hearts/lives from the roots as well as lose
     *                  the game if no more hearts/lives are left.
     */
    public void decreaseLives(GameScene gameScene) {
        myCurrentXCoordinateForHearts -= HEART_WIDTH + 5;
        if (myHearts.size() > 0) {
            gameScene.getRoot().getChildren().remove(myHearts.get(myHearts.size()-1));
            myHearts.remove(myHearts.size() -1);
        } else {
            gameScene.loseLevel();
        }
    }

    /**
     * Increases the number of lives the player has left by one.
     * @param root the root of the gameScene, which is used for adding in the ImageView of the heart/life.
     */
    public void increaseLives(Pane root) {
        addHeart(root);
    }

    /**
     * Gets the number of lives remaining; used for checking conditions to lose level.
     * @return the number of lives remaining.
     */
    public int getLivesRemaining() {
        return myHearts.size();
    }

    /**
     * Get the score label for the level, used for displaying the score at the top right when in a level.
     * @return the label settings for the score label to be used in a level
     */
    public Label getScoreLabelForLevel() {
        myScoreLabel.setText("" + myScore);
        myScoreLabel.setLayoutX(785);
        myScoreLabel.setLayoutY(7);
        myScoreLabel.setTextFill(Color.LIGHTGRAY);
        myScoreLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 25));
        return myScoreLabel;
    }

    /**
     * Get the score label to be displayed during a win/lose scene
     * @return the label settings for the score label to be used in a win/lose scene
     */
    public Label getScoreLabelForNonLevel() {
        myScoreLabel.setLayoutX(500);
        myScoreLabel.setLayoutY(450);
        myScoreLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 40));
        return myScoreLabel;
    }

    /**
     * Increase the global score of the player, used for when the player destroyed a brick, if they have remaining lives
     * after a level has been cleared, etc.
     * @param value the value of how much the score should be incremented by
     */
    public void increaseScore(int value) {
        myScore += value;
        myScoreLabel.setText("" + myScore);
    }

    /**
     * Decreases the global score of the player, used for when the player hit a dead zone, etc.
     * @param value the value of how much the score should be decreased by
     */
    public void decreaseScore(int value) {
        myScore -= value;
        myScoreLabel.setText("" + myScore);
    }
}
