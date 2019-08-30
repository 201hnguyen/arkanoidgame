package game;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.stream.Stream;

public interface GameLevel {
    int SCENE_WIDTH = 1000;
    int SCENE_HEIGHT = 800;
    String HARRY_POTTER_IMAGE = "harrypotter.png";
    String DOOR_IMAGE = "door.png";
    int HARRY_POTTER_HEIGHT = 150;
    int HARRY_POTTER_WIDTH = 200;
    int BRICK_WIDTH = 100;
    int BRICK_HEIGHT = 50;

    Scene getLevelScene();
    void setNextScene(Scene nextScene);

    default ImageView getHarryPotter() {
        Image harryPotterImage  = new Image(this.getClass().getClassLoader().getResourceAsStream(HARRY_POTTER_IMAGE));
        ImageView harryPotter = new ImageView(harryPotterImage);
        harryPotter.setFitWidth(HARRY_POTTER_WIDTH);
        harryPotter.setFitHeight(HARRY_POTTER_HEIGHT);
        harryPotter.setX(SCENE_WIDTH / 2 - harryPotter.getBoundsInLocal().getWidth() / 2);
        harryPotter.setY(SCENE_HEIGHT - harryPotter.getBoundsInLocal().getHeight());
        return harryPotter;
    }

    default ImageView getDoor() {
        Image doorImage = new Image(this.getClass().getClassLoader().getResourceAsStream(DOOR_IMAGE));
        ImageView door = new ImageView(doorImage);
        door.setFitHeight(BRICK_HEIGHT-5);
        door.setX(SCENE_WIDTH / 2 - door.getBoundsInLocal().getWidth() / 2);
        return door;
    }

    // TODO: Refractor so it won't be redundant

    default ImageView getBrick(BrickColor color) {
        Image brickImage = new Image(this.getClass().getClassLoader().getResourceAsStream(color.getFileName()));
        ImageView brick = new ImageView(brickImage);
        brick.setFitWidth(BRICK_WIDTH);
        brick.setFitHeight(BRICK_HEIGHT);
        return brick;
    }

    default ImageView createBrickFromId(int id) {
        ImageView brick;
        if (id == 1) {
            brick = getBrick(BrickColor.YELLOW);
        } else if (id == 2) {
            brick = getBrick(BrickColor.PURPLE);
        } else if (id == 3) {
            brick = getBrick(BrickColor.RED);
        } else {
            brick = getBrick(BrickColor.NONE);
        }
        return brick;
    }
}

