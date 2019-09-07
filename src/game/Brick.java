package game;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Brick {

    public static final int BRICK_WIDTH = 100;
    public static final int BRICK_HEIGHT = 50;
    public static final int POWER_BRICKS_ID_OFFSET = 3;

    private int myBrickId;
    private int myHitsRemaining;
    private ImageView myBrickImageView;
    private Powerup myPowerup;

    public Brick(int hitsId) {
        myBrickId = hitsId;
        if (myBrickId == 1) {
            myBrickImageView = createBrick(BrickType.ONE_HIT);
            myHitsRemaining = hitsId;
        } else if (myBrickId == 2) {
            myBrickImageView = createBrick(BrickType.TWO_HIT);
            myHitsRemaining = hitsId;
        } else if (myBrickId == 3) {
            myBrickImageView = createBrick(BrickType.THREE_HIT);
            myHitsRemaining = hitsId;
        } else if (myBrickId == 4) {
            myBrickImageView = createBrick(BrickType.ONE_HIT);
            myHitsRemaining = hitsId - POWER_BRICKS_ID_OFFSET;
            myPowerup = new Powerup(Powerup.PowerupType.HORN);
        } else if (myBrickId == 5) {
            myBrickImageView = createBrick(BrickType.TWO_HIT);
            myHitsRemaining = hitsId - POWER_BRICKS_ID_OFFSET;
            myPowerup = new Powerup(Powerup.PowerupType.POTION);
        } else if (myBrickId == 6) {
            myBrickImageView = createBrick(BrickType.THREE_HIT);
            myHitsRemaining = hitsId - POWER_BRICKS_ID_OFFSET;
            myPowerup = new Powerup(Powerup.PowerupType.LIGHTNING);
        }
    }

    private ImageView createBrick(BrickType type) {
        Image brickImage = new Image(this.getClass().getClassLoader().getResourceAsStream(type.getBrickFileName()));
        ImageView brick = new ImageView(brickImage);
        brick.setFitWidth(BRICK_WIDTH);
        brick.setFitHeight(BRICK_HEIGHT);
        return brick;
    }

    public void decreaseHitsRemaining() {
        myHitsRemaining--;
    }

    public int getHitsRemaining() {
        return myHitsRemaining;
    }

    public ImageView getBrickImageView() {
        return myBrickImageView;
    }

    public int getBrickId() {
        return myBrickId;
    }

    public Powerup getPowerup() {
        return myPowerup;
    }

    public enum BrickType {
        ONE_HIT ("brick1.png"),
        TWO_HIT ("brick2.png"),
        THREE_HIT ("brick3.png");

        private String myAssociatedFileName;
        BrickType(String fileName) {
            myAssociatedFileName = fileName;
        }

        private String getBrickFileName() {
            return myAssociatedFileName;
        }
    }

}