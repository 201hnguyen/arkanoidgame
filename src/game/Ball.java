package game;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Ball {
    private static final String BALL_IMAGE = "ball.png";
    private static final int BALL_SIZE = 30;
    private static final int BALL_SPEED = 500;

    private double[] myBallDirection = {1,1};
    private ImageView myBallImageView;

    public Ball(Pane root, Character paddle) {
        Image ballImage =  new Image(this.getClass().getClassLoader().getResourceAsStream(BALL_IMAGE));
        myBallImageView = new ImageView(ballImage);
        myBallImageView.setFitHeight(BALL_SIZE);
        myBallImageView.setFitWidth(BALL_SIZE);
        resetBall(paddle);
        root.getChildren().add(myBallImageView);
    }

    public ImageView getBallImageView() {
        return myBallImageView;
    }

    public double[] getBallDirection() {
        return myBallDirection;
    }

    public void resetBall(Character paddle) {
        this.getBallImageView().setX(paddle.getCharacterImageView().getBoundsInParent().getCenterX() - BALL_SIZE / 2);
        this.getBallImageView().setY(paddle.getCharacterImageView().getBoundsInParent().getMinY() - BALL_SIZE);
        myBallDirection[0] = 1;
        myBallDirection[1] = 1;
    }

    public void setBallMotion(double elapsedTime, Character character) {
        //TODO: get the motion right (go to office hour to ask about math) (problems: angles and speed increases at every level)
        ImageView ballImageView = this.getBallImageView();
        ballImageView.setX(ballImageView.getX() + myBallDirection[0] * BALL_SPEED * elapsedTime);
        ballImageView.setY(ballImageView.getY() + myBallDirection[1] * BALL_SPEED * elapsedTime);

        wallCollisionCheck(ballImageView, character);
        paddleCollisionCheck(ballImageView, character.getCharacterImageView());
    }

    private void wallCollisionCheck(ImageView ball, Character character) {
        if (ball.getX() <= 0 || ball.getX() >= (GameMain.SCENE_WIDTH - ball.getBoundsInLocal().getWidth())) {
            myBallDirection[0] *= -1;
        }
        if (ball.getY() <= 0) {
            myBallDirection[1] *= -1;
        } else if (ball.getY() >= (GameMain.SCENE_HEIGHT - ball.getBoundsInLocal().getHeight())) {
            resetBall(character);
        }
    }

    private void paddleCollisionCheck(ImageView ball, ImageView character) {
        if (ball.getBoundsInParent().intersects(character.getBoundsInParent()) &&
                ball.getBoundsInParent().getMaxY() <= character.getBoundsInParent().getMinY()) {
            myBallDirection[1] *= -1;
        }
    }
}
