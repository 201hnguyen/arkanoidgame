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

    private ArrayList<ImageView> myHearts = new ArrayList<>();
    private int myLives;

    public GameStatus(Pane root) {
        myHearts.clear();
        myLives = 3;
        int xCoordinate = X_OFFSET;
        for (int i = 0; i < myLives; i++) {
            Image heart = new Image(this.getClass().getClassLoader().getResourceAsStream(HEART_IMAGE));
            ImageView heartImageView = new ImageView(heart);
            heartImageView.setFitWidth(HEART_WIDTH);
            heartImageView.setFitHeight(HEART_HEIGHT);
            heartImageView.setY(Y_OFFSET);
            heartImageView.setX(xCoordinate);
            root.getChildren().add(heartImageView);
            myHearts.add(heartImageView);
            xCoordinate += HEART_WIDTH + 5;
        }
    }

    public void decreaseLives(GameScene gameScene) throws Exception {
//        myLives --;
//        if (myLives > 0) {
//            gameScene.getRoot().getChildren().remove(myHearts.get(myHearts.size()-1));
//            myHearts.remove(myHearts.size() -1);
//        }
    }

    public int getLivesRemaining() {
        return myLives;
    }
}
