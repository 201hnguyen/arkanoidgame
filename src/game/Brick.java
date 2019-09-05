package game;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Brick {

    public static final int BRICK_WIDTH = 100;
    public static final int BRICK_HEIGHT = 50;

    private int myBrickId;
    private int myHitsRemaining;
    private ImageView myBrickImageView;

    public Brick(int hitsId) {
        myBrickId = hitsId;
        myHitsRemaining = hitsId;
        if (myBrickId == 1) {
            myBrickImageView = createBrick(BrickType.BRICK1);
        } else if (myBrickId == 2) {
            myBrickImageView = createBrick(BrickType.BRICK2);
        } else if (myBrickId == 3) {
            myBrickImageView = createBrick(BrickType.BRICK3);
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

    public enum BrickType {
        BRICK1 ("brick1.png"),
        BRICK2 ("brick2.png"),
        BRICK3 ("brick3.png");

        private String myAssociatedFileName;
        BrickType(String fileName) {
            myAssociatedFileName = fileName;
        }

        private String getBrickFileName() {
            return myAssociatedFileName;
        }
    }

}