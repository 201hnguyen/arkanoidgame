package game;

import javafx.animation.PauseTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.ArrayList;

public class Powerup {
    public static final int ICON_SIZE = 50;
    public static final int POWERUP_SPEED = 100;

    private ImageView myPowerupImageView;
    private PowerupType myPowerupType;
    private double[] myPowerBrickCoordinate;

    public Powerup(PowerupType type) {
        myPowerupType = type;
        Image powerupImage = new Image(this.getClass().getClassLoader().getResourceAsStream(type.getPowerupFileName()));
        myPowerupImageView = new ImageView(powerupImage);
        myPowerupImageView.setFitWidth(ICON_SIZE);
        myPowerupImageView.setFitHeight(ICON_SIZE);
        myPowerBrickCoordinate = new double[]{0,0};
    }

    public void revealPowerup(Brick brick, Pane root) {
        myPowerupImageView.setX(brick.getBrickImageView().getBoundsInParent().getCenterX() - ICON_SIZE / 2);
        myPowerupImageView.setY(brick.getBrickImageView().getBoundsInParent().getCenterY() - ICON_SIZE / 2);
        root.getChildren().add(myPowerupImageView);
    }

    public void setPowerupMotion(double elapsedTime, GameScene gameScene) {
        myPowerupImageView.setY(myPowerupImageView.getY() + POWERUP_SPEED * elapsedTime);
        if (myPowerupImageView.getBoundsInParent().intersects(gameScene.getMainCharacter().getCharacterImageView().getBoundsInParent())) {
            activatePowerup(gameScene);
        }
    }

    private void activatePowerup(GameScene gameScene) {
        gameScene.getRoot().getChildren().remove(myPowerupImageView);
        if (myPowerupType == PowerupType.POTION) {
            activatePotionPower();
        } else if (myPowerupType == PowerupType.HORN) {
            activateHornPower(gameScene.getRoot(), gameScene.getMainCharacter());
        } else if (myPowerupType == PowerupType.LIGHTNING) {
            activateLightningPower(gameScene);
        }
    }

    private void activatePotionPower() {

    }

    private void activateHornPower(Pane root, Character character) {
        if (character.getCharacterImageView() != character.getDumbledoresArmyImageView()) {
            character.changeCharacter(character.getDoubleCharacterImageView(), root);
            PauseTransition delay = new PauseTransition(Duration.seconds(10));
            delay.setOnFinished(e -> character.changeCharacter(character.getSingleCharacterImageView(), root));
            delay.play();
        }
    }

    private void activateLightningPower(GameScene gameScene) {
        Image lightningImage = new Image(this.getClass().getClassLoader().getResourceAsStream("lightning.png"));
        ImageView lightning = new ImageView(lightningImage);
        lightning.setFitHeight(150);
        lightning.setFitWidth(150);
        lightning.setX(myPowerBrickCoordinate[0]);
        lightning.setY(myPowerBrickCoordinate[1]);
        gameScene.getRoot().getChildren().add(lightning);
        PauseTransition delay = new PauseTransition(Duration.seconds(0.05));
        delay.setOnFinished(e -> makeLightningAndBricksDisappear(lightning, gameScene));
        delay.play();
    }

    private void makeLightningAndBricksDisappear(ImageView lightning, GameScene gameScene) {
        lightning.setX(lightning.getX());
        ArrayList<Brick> bricksToRemove = new ArrayList<>();
        for (Brick brick : gameScene.getBrickStructure().getBricks().keySet()) {
            if (lightning.getBoundsInParent().intersects(brick.getBrickImageView().getBoundsInParent())) {
                bricksToRemove.add(brick);
            }
        }

        for (Brick brickToRemove : bricksToRemove) {
            gameScene.getRoot().getChildren().remove(brickToRemove.getBrickImageView());
            gameScene.getBrickStructure().removeBrick(brickToRemove);
        }
        gameScene.getRoot().getChildren().remove(lightning);
    }

    public enum PowerupType {
        LIGHTNING ("lightningpower.png"),
        POTION ("potionpower.png"),
        HORN ("hornpower.png");

        private String myAssociatedFileName;
        PowerupType(String fileName) {
            myAssociatedFileName = fileName;
        }

        private String getPowerupFileName() {
            return myAssociatedFileName;
        }
    }

    public void setPowerBrickCoordinate(double x, double y) {
        myPowerBrickCoordinate[0] = x;
        myPowerBrickCoordinate[1] = y;
    }
}
