package game;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Ball {
    public static final String BALL_IMAGE = "ball.png";
    public static final int BALL_SIZE = 50;
    public static final int BALL_SPEED = 350;

    private int[] myDirection;
    private ImageView myBallImageView;


    public Ball(Pane root, Character character) {
        Image ballImage =  new Image(this.getClass().getClassLoader().getResourceAsStream(BALL_IMAGE));
        myBallImageView = new ImageView(ballImage);
        myBallImageView.setFitHeight(BALL_SIZE);
        myBallImageView.setFitWidth(BALL_SIZE);
        root.getChildren().add(myBallImageView);
        resetBall(character);
    }

    public void resetBall(Character character) {
        myBallImageView.setX(character.getCharacterImageView().getBoundsInParent().getCenterX() - BALL_SIZE / 2);
        myBallImageView.setY(character.getCharacterImageView().getBoundsInParent().getMinY() - BALL_SIZE);
        myDirection = new int[]{0, -1};
    }

    public void setBallMotion(double elapsedTime, GameScene gameScene) throws Exception {
        myBallImageView.setX(myBallImageView.getX() + myDirection[0] * BALL_SPEED * elapsedTime);
        myBallImageView.setY(myBallImageView.getY() + myDirection[1] * BALL_SPEED * elapsedTime);
        paddleCollisionCheck(gameScene.getMainCharacter());
        wallCollisionCheck(gameScene);
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

    private void wallCollisionCheck(GameScene gameScene) throws Exception {
        if (myBallImageView.getX() <= 0 || myBallImageView.getX() >= GameMain.SCENE_WIDTH - myBallImageView.getBoundsInLocal().getWidth()) {
            myDirection[0] *= -1;
        }

        if (myBallImageView.getY() <= 0) {
            myDirection[1] *= -1;
        } else if (myBallImageView.getY() >= (GameMain.SCENE_HEIGHT)) {
//            System.out.println("my lives remaining: " + gameScene.getGameStatus().getLivesRemaining());
            gameScene.getGameStatus().decreaseLives(gameScene);
//            System.out.println("my lives remaining: " + gameScene.getGameStatus().getLivesRemaining());
            if (gameScene.getGameStatus().getLivesRemaining() == 0) {
//                System.out.println("Losing level");
                gameScene.loseLevel();
            } else {
                resetBall(gameScene.getMainCharacter());
            }
        }
    }

    public ImageView getBallImageView() {
        return myBallImageView;
    }

    public int getXDirection() {
        return myDirection[0];
    }

    public int getYDirection() {
        return myDirection[1];
    }

    public void setXDirection(int x) {
        myDirection[0] = x;
    }

    public void setYDirection(int y) {
        myDirection[1] = y;
    }
}
