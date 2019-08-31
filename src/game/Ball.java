package game;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Ball {
    private static final String BALL_IMAGE = "ball.png";
    private static final int BALL_SIZE = 30;
    int[] ballDirection = {1,-1};
    int ballSpeed = 500;

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
        ballDirection[0] = 1;
        ballDirection[1] = -1;
        ballSpeed = 1000;
       this.getBallImageView().setX(GameMain.SCENE_WIDTH / 2 - this.getBallImageView().getBoundsInLocal().getWidth() / 2);
       this.getBallImageView().setY(GameMain.SCENE_HEIGHT - character.getCharacterImageView().getBoundsInLocal().getHeight() - this.getBallImageView().getBoundsInLocal().getHeight());
    }

    public void setBallMotion(Ball ball, double elapsedTime) {

        ImageView ballImageView = ball.getBallImageView();
        ballImageView.setX(ballImageView.getX() + ballDirection[0] * ballSpeed * elapsedTime);
        ballImageView.setY(ballImageView.getY() + ballDirection[1] * ballSpeed * elapsedTime);

        if (ballImageView.getX() <= 0 || ballImageView.getX() >= (GameMain.SCENE_WIDTH - ballImageView.getBoundsInLocal().getWidth())) {
            ballDirection[0] *= -1;
        }

        if (ballImageView.getY() <= 0 || ballImageView.getY() >= (GameMain.SCENE_HEIGHT - ballImageView.getBoundsInLocal().getHeight())) {
            ballDirection[1] *= -1;
        }

    }

}
