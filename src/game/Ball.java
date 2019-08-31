package game;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Ball {
    private static final String BALL_IMAGE = "ball.png";
    private static final int BALL_SIZE = 30;
    private static final int BALL_SPEED = 500;
    int[] ballDirection = {1,-1};

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
        this.getBallImageView().setX(character.getCharacterImageView().getBoundsInParent().getCenterX() - BALL_SIZE / 2);
        this.getBallImageView().setY(character.getCharacterImageView().getBoundsInParent().getMinY() - BALL_SIZE);
    }

    public void setBallMotion(Ball ball, double elapsedTime, Character character) {

        ImageView ballImageView = ball.getBallImageView();
        ballImageView.setX(ballImageView.getX() + ballDirection[0] * BALL_SPEED * elapsedTime);
        ballImageView.setY(ballImageView.getY() + ballDirection[1] * BALL_SPEED * elapsedTime);

        wallCollisionCheck(ballImageView);
        paddleCollisionCheck(ballImageView, character.getCharacterImageView());
    }

    private void wallCollisionCheck(ImageView ball) {
        if (ball.getX() <= 0 || ball.getX() >= (GameMain.SCENE_WIDTH - ball.getBoundsInLocal().getWidth())) {
            ballDirection[0] *= -1;
        }
        if (ball.getY() <= 0) {
            ballDirection[1] *= -1;
        } else if (ball.getY() >= (GameMain.SCENE_HEIGHT - ball.getBoundsInLocal().getHeight())) {
            //TODO: Finish for death.
        }
    }

    private void paddleCollisionCheck(ImageView ball, ImageView character) {
        if (ball.getBoundsInParent().intersects(character.getBoundsInParent()) &&
                ball.getBoundsInParent().getMaxY() <= character.getBoundsInParent().getMinY()) {
            ballDirection[1] *= -1;
        }
    }

}
