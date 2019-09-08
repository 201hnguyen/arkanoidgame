package game;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Brick {
    public static final int BRICK_WIDTH = 100;
    public static final int BRICK_HEIGHT = 50;
    public static final int POWER_BRICKS_POINTS_OFFSET = 3;

    private BrickType myBrickType;
    private int myHitsRemaining;
    private ImageView myBrickImageView;
    private Powerup myPowerup;
    private int myRow;
    private int myPointsValue;

    public Brick(int brickId, int row) {
        myRow = row;
        if (brickId == 1) {
            myBrickType = BrickType.ONE_HIT_REGULAR;
        } else if (brickId == 2) {
            myBrickType = BrickType.TWO_HIT_REGULAR;
        } else if (brickId == 3) {
            myBrickType = BrickType.THREE_HIT_REGULAR;
        } else if (brickId == 4) {
            myBrickType = BrickType.ONE_HIT_POWER;
            myPowerup = new Powerup(Powerup.PowerupType.HORN);
        } else if (brickId == 5) {
            myBrickType = BrickType.TWO_HIT_POWER;
            myPowerup = new Powerup(Powerup.PowerupType.POTION);
        } else if (brickId == 6) {
            myBrickType = BrickType.THREE_HIT_POWER;
            myPowerup = new Powerup(Powerup.PowerupType.LIGHTNING);
        }
        myBrickImageView = createBrick(brickId);

    }

    private ImageView createBrick(int brickId) {
        if (myBrickType == BrickType.ONE_HIT_REGULAR || myBrickType == BrickType.TWO_HIT_REGULAR || myBrickType == BrickType.THREE_HIT_REGULAR) {
            myHitsRemaining = brickId;
        } else {
            myHitsRemaining = brickId - POWER_BRICKS_POINTS_OFFSET;
        }
        Image brickImage = new Image(this.getClass().getClassLoader().getResourceAsStream(myBrickType.getBrickImagePath()));
        ImageView brick = new ImageView(brickImage);
        brick.setFitWidth(BRICK_WIDTH);
        brick.setFitHeight(BRICK_HEIGHT);
        myPointsValue = myBrickType.getPointsValue();
        return brick;
    }

    public void downsizeBrick(GameScene gameScene) {
        gameScene.getGameStatus().increaseScore(myPointsValue);
        myHitsRemaining--;
        if (myHitsRemaining == 0) {
            gameScene.getGameStatus().increaseScore(myPointsValue);
            gameScene.getRoot().getChildren().remove(myBrickImageView);
            gameScene.getBricks().remove(this);

            if (myBrickType == BrickType.ONE_HIT_POWER || myBrickType == BrickType.TWO_HIT_POWER || myBrickType == BrickType.THREE_HIT_POWER) {
                myPowerup.setPowerup(this, gameScene);
            }
        }
    }

    public ImageView getBrickImageView() {
        return myBrickImageView;
    }

    public int getRow() {
        return myRow;
    }

    public enum BrickType {
        ONE_HIT_REGULAR ("brick1.png", 500),
        TWO_HIT_REGULAR ("brick2.png", 1000),
        THREE_HIT_REGULAR ("brick3.png", 1500),
        ONE_HIT_POWER ("brick1.png", 2000),
        TWO_HIT_POWER ("brick2.png", 2500),
        THREE_HIT_POWER ("brick3.png", 3000);

        private String myBrickImagePath;
        private int myPointsValue;

        BrickType(String fileName, int pointsValue) {
            myBrickImagePath = fileName;
            myPointsValue = pointsValue;
        }

        private String getBrickImagePath() {
            return myBrickImagePath;
        }
        private int getPointsValue() {
            return myPointsValue;
        }
    }

}