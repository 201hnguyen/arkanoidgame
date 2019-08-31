package game;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Ball {
    private static final String BALL_IMAGE = "ball.png";
    private static final int BALL_SIZE = 30;

    private ImageView myBallImageView;

    public Ball() {
        Image ballImage =  new Image(this.getClass().getClassLoader().getResourceAsStream(BALL_IMAGE));
        myBallImageView = new ImageView(ballImage);
        myBallImageView.setFitHeight(BALL_SIZE);
        myBallImageView.setFitWidth(BALL_SIZE);
    }

    public ImageView getBallImageView() {
        return myBallImageView;
    }

    public void addBallToScreen(Group root) {
        root.getChildren().add(myBallImageView);
    }

    public void resetBall(Character character) {
       this.getBallImageView().setX(GameMain.SCENE_WIDTH / 2 - this.getBallImageView().getBoundsInLocal().getWidth() / 2);
       this.getBallImageView().setY(GameMain.SCENE_HEIGHT - character.getCharacterImageView().getBoundsInLocal().getHeight() - this.getBallImageView().getBoundsInLocal().getHeight());
    }

}
