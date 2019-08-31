package game;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Brick {

    public static final int BRICK_WIDTH = 100;
    public static final int BRICK_HEIGHT = 50;

    private static int myHitsRemaining;
    private static ImageView myBrickImageView;

    public Brick(int hitsId) {
        myHitsRemaining = hitsId;
        if (myHitsRemaining == 1) {
            myBrickImageView = createBrick(BrickColor.BROWN);
        } else if (myHitsRemaining == 2) {
            myBrickImageView = createBrick(BrickColor.GREEN);
        } else if (myHitsRemaining == 3) {
            myBrickImageView = createBrick(BrickColor.PURPLE);
        } else {
            myBrickImageView = createBrick(BrickColor.NONE);
        }
    }

    private ImageView createBrick(BrickColor color) {
        Image brickImage = new Image(this.getClass().getClassLoader().getResourceAsStream(color.getBrickFileName()));
        ImageView brick = new ImageView(brickImage);
        brick.setFitWidth(BRICK_WIDTH);
        brick.setFitHeight(BRICK_HEIGHT);
        return brick;
    }

    public int getBrickHitsRemaining() {
        return myHitsRemaining;
    }

    public ImageView getBricksImageView() {
        return myBrickImageView;
    }

    public enum BrickColor {
        NONE ("transparentbrick.png"),
        GREEN ("greenbrick.png"),
        BROWN ("brownbrick.png"),
        PURPLE ("purplebrick.png");

        private String myAssociatedFileName;
        BrickColor(String fileName) {
            myAssociatedFileName = fileName;
        }

        public String getBrickFileName() {
            return myAssociatedFileName;
        }
    }

}