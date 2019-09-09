package game;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Ball {
    public static final String BALL_IMAGE = "ball.png";
    public static final int BALL_SIZE = 50;
    public static final int BALL_SPEED = 400;

    private int[] myDirection;
    private ImageView myBallImageView;

    /**
     * Used to control the movements and position of the ball in the game. Assumes that the image for
     * the ball, "ball.png," exists in the resource folder; thus, these files cannot be removed. For example, this class
     * can be used for resetting the ball, resetting its movement at every new frame, reflecting the ball, and checking
     * its intersections with other objects in the game.
     * @param character
     */
    public Ball(Character character) {
        Image ballImage =  new Image(this.getClass().getClassLoader().getResourceAsStream(BALL_IMAGE));
        myBallImageView = new ImageView(ballImage);
        myBallImageView.setFitHeight(BALL_SIZE);
        myBallImageView.setFitWidth(BALL_SIZE);
        resetBall(character);
    }

    /**
     * Resets the ball to its original position in the center of the paddle and its original direction of going straight up.
     * @param character the paddle, used to set the ball's position relative to the paddle
     */
    public void resetBall(Character character) {
        myBallImageView.setX(character.getCharacterImageView().getBoundsInParent().getCenterX() - BALL_SIZE / 2);
        myBallImageView.setY(character.getCharacterImageView().getBoundsInParent().getMinY() - BALL_SIZE);
        myDirection = new int[]{0, -1};
    }

    /**
     * Controls the motion/movement of the ball by setting the X coordinate slightly away from the previous
     * X coordinate depending on the ball's speed, direction, and the elapsed time. Also checks if, in this new
     * position, whether or not the ball collides with the paddle, the wall, or any of the dead zones. If the
     * ball hits the paddle or the wall, it will reflect back; if it hits the bottom, the remaining lives will be creased;
     * if it hits the dead zones, the player will get 5000 subtracted from their score, will lose a life, and will have
     * to play with a blurry main character for the rest of the game.
     * @param elapsedTime the time elapsed, used for calculating how much the ball has moved in that time in order to set
     *                    its new position
     * @param gameScene the GameScene of which the ball is a part of, used for accessing other elements such as characters,
     *                  dead zones, etc.
     */
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
            character.reflectBall(this);
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
            if (gameScene.getGameStatus().getLivesRemaining() == 0) {
                gameScene.loseLevel();
            } else {
                resetBall(gameScene.getMainCharacter());
            }
        }
    }

    private void deadzoneCollisionCheck(GameScene gameScene) {
        for (BackgroundStructure deadZone : gameScene.getDeadZones()) {
            if (myBallImageView.getBoundsInParent().intersects(deadZone.getStructureImageView().getBoundsInParent())) {
                gameScene.getMainCharacter().setBlur();
                gameScene.getGameStatus().decreaseScore(5000);
                gameScene.getGameStatus().decreaseLives(gameScene);
                if (gameScene.getGameStatus().getLivesRemaining() == 0) {
                    gameScene.loseLevel();
                } else {
                    resetBall(gameScene.getMainCharacter());
                }
            }
        }
    }

    /**
     * Relfects the ball off of the brick it hits; if the ball hits the brick in the corner,
     * then both the X and Y direction are reflected; if the ball hits the brick in from the top
     * or bottom, the the Y direction is reflected; if the ball hits the brick from the sides,
     * then the X direction is reflected.
     * @param brickToDownsize the brick that the ball has hit and will have to reflect off of
     */
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

    /**
     * Gets the ImageView for this Ball, useful for when trying to add this object ot the root of the scene.
     * @return ImageView representation of the ball.
     */
    public ImageView getBallImageView() {
        return myBallImageView;
    }

    public int[] getDirection() {
        return myDirection;
    }
}
