package game;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

public class GameStatus {
    public static final String HEART_IMAGE = "heart.png";
    public static final int HEART_WIDTH = 25;
    public static final int HEART_HEIGHT = 30;
    public static final int X_OFFSET = 95;
    public static final int Y_OFFSET = 5;
    public static final int INITIAL_LIVES = 3;

    private ArrayList<ImageView> myHearts = new ArrayList<>();
    private int myCurrentXCoordinate;

    public GameStatus(Pane root) {
        myHearts.clear();
        myCurrentXCoordinate = X_OFFSET;
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
        heartImageView.setX(myCurrentXCoordinate);
        root.getChildren().add(heartImageView);
        myHearts.add(heartImageView);
        myCurrentXCoordinate += HEART_WIDTH + 5;
    }

    public void decreaseLives(GameScene gameScene) {
        myCurrentXCoordinate -= HEART_WIDTH + 5;
        if (myHearts.size() > 0) {
            gameScene.getRoot().getChildren().remove(myHearts.get(myHearts.size()-1));
            myHearts.remove(myHearts.size() -1);
        }
        if (gameScene.getGameStatus().getLivesRemaining() == 0) {
            gameScene.loseLevel();
        } else {
            gameScene.getBall().resetBall(gameScene.getMainCharacter());
        }
    }

    public void increaseLives(Pane root) {
        addHeartImageView(root);
    }

    public int getLivesRemaining() {
        return myHearts.size();
    }
}
