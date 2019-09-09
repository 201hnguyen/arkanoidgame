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

    /**
     * The bricks that create the barriers of which the ball need to clear in order to advance to the next level.
     * Assumes that "brick1.png," "brick2.png," and "brick3.png" exists in the resource folder. For example, this class
     * is used to downsize the brick when the ball hits it and to get the row needed for destroying the brick, as well
     * as for holding powerups associated with each brick. There are six brick types currently supported; the types depend
     * on how many hits it takes to break (1 & 4 take 1 hit to break, 2 & 5 take 2 hits, 3 & 6 take 3 hits), as well as
     * whether or not they have powers (4, 5, and 6) do. This will determine their points value.
     * @param brickId
     * @param row
     */
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

    /**
     * Decreases the amount of hits needed to break the bricks. This is used when the ball comes in contact with the brick.
     * If the hits remaining is zero, then the bricks will be destroyed and will disappear from the game scene. If the
     * brick has a power up, upon destruction, the power up will be activated.
     * @param gameScene the current GameScene for which the brick is a part of; used for removing the brick from
     *                  the gameScene's collection of bricks, for alternating the score based on the brick hit, etc.
     */
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

    /**
     * Gets the ImageView representation of the current brick; used for adding it to the root of the scene.
     * @return the ImageView representation of the current brick
     */
    public ImageView getBrickImageView() {
        return myBrickImageView;
    }

    /**
     * Gets the brick's row, useful for when trying to determine which row is the lowest to delete upon
     * pressing the C key
     * @return the row that the brick is on
     */
    public int getRow() {
        return myRow;
    }

    /**
     * Used to specify the types of bricks allowed in the game, the paths to their images to be used, and
     * the points value of each of the bricks.
     */
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