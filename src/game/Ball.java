package game;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Ball {
    public static final String BALL_IMAGE = "ball.png";
    public static final int BALL_SIZE = 50;
    public static final int BALL_SPEED = 400;

    private int[] myDirection;
    private ImageView myBallImageView;


    public Ball(Character character) {
        Image ballImage =  new Image(this.getClass().getClassLoader().getResourceAsStream(BALL_IMAGE));
        myBallImageView = new ImageView(ballImage);
        myBallImageView.setFitHeight(BALL_SIZE);
        myBallImageView.setFitWidth(BALL_SIZE);
        resetBall(character);
    }

    public void resetBall(Character character) {
        myBallImageView.setX(character.getCharacterImageView().getBoundsInParent().getCenterX() - BALL_SIZE / 2);
        myBallImageView.setY(character.getCharacterImageView().getBoundsInParent().getMinY() - BALL_SIZE);
        myDirection = new int[]{0, -1};
    }

    public void setBallMotion(double elapsedTime, GameScene gameScene) {
        myBallImageView.setX(myBallImageView.getX() + myDirection[0] * BALL_SPEED * elapsedTime);
        myBallImageView.setY(myBallImageView.getY() + myDirection[1] * BALL_SPEED * elapsedTime);
        paddleCollisionCheck(gameScene.getMainCharacter());
        wallCollisionCheck(gameScene);
        deadzoneCollisionCheck(gameScene);
    }

    private void paddleCollisionCheck(Character character) {
        if (myBallImageView.getBoundsInParent().intersects(character.getCharacterImageView().getBoundsInParent()) &&
                myBallImageView.getBoundsInParent().getMaxY() <= character.getCharacterImageView().getBoundsInParent().getMinY()) {
            if (myDirection[0] == 0) {
                myDirection[0] = -1;
            }
            if (myBallImageView.getBoundsInParent().getCenterX() <= character.getCharacterImageView().getBoundsInParent().getCenterX() - Character.THIRD_DIVISION) {
                myDirection[0] = -1;
                myDirection[1] = -1;
            } else if (myBallImageView.getBoundsInParent().getCenterX() >= character.getCharacterImageView().getBoundsInParent().getCenterX() + Character.THIRD_DIVISION) {
                myDirection[0] = 1;
                myDirection[1] = -1;
            } else {
                myDirection[1] *= -1;
            }
        }
    }

    private void wallCollisionCheck(GameScene gameScene) {
        if (myBallImageView.getX() <= 0 || myBallImageView.getX() >= GameMain.SCENE_WIDTH - myBallImageView.getBoundsInLocal().getWidth()) {
            myDirection[0] *= -1;
        }
        if (myBallImageView.getY() <= 0) {
            myDirection[1] *= -1;
        } else if (myBallImageView.getY() >= (GameMain.SCENE_HEIGHT)) {
            gameScene.getGameStatus().decreaseLives(gameScene);
            resetBall(gameScene.getMainCharacter());
        }
    }

    private void deadzoneCollisionCheck(GameScene gameScene) {
        for (BackgroundStructure deadZone : gameScene.getDeadZones()) {
            if (myBallImageView.getBoundsInParent().intersects(deadZone.getStructureImageView().getBoundsInParent())) {
                gameScene.getMainCharacter().blurCharacter(gameScene.getRoot());
                gameScene.getGameStatus().decreaseScore(5000);
                gameScene.getGameStatus().decreaseLives(gameScene);
                resetBall(gameScene.getMainCharacter());
            }
        }
    }

    public void reflectBall(Brick brickToDownsize) {
        boolean ballComesFromTop = myBallImageView.getBoundsInParent().getCenterY() <= brickToDownsize.getBrickImageView().getBoundsInParent().getMinY();
        boolean ballComesFromBottom = myBallImageView.getBoundsInParent().getCenterY() >= brickToDownsize.getBrickImageView().getBoundsInParent().getMaxY();
        boolean ballComesFromLeft = myBallImageView.getBoundsInParent().getCenterX() <= brickToDownsize.getBrickImageView().getBoundsInParent().getMinX();
        boolean ballComesFromRight = myBallImageView.getBoundsInParent().getCenterX() >= brickToDownsize.getBrickImageView().getBoundsInParent().getMaxX();

        if (ballComesFromTop && ballComesFromLeft || ballComesFromTop && ballComesFromRight ||
                ballComesFromBottom && ballComesFromLeft || ballComesFromBottom && ballComesFromRight) {
            myDirection[0] *= -1;
            myDirection[1] *= -1;
        } else if (ballComesFromBottom || ballComesFromTop) {
            myDirection[1] *= -1;
        } else if (ballComesFromLeft || ballComesFromRight) {
            myDirection[0] *= -1;
        }
    }

    public ImageView getBallImageView() {
        return myBallImageView;
    }
}
